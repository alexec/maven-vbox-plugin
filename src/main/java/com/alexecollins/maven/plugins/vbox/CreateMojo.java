package com.alexecollins.maven.plugins.vbox;

import com.alexecollins.maven.plugins.vbox.mediaregistry.FloppyImage;
import com.alexecollins.maven.plugins.vbox.mediaregistry.Image;
import com.alexecollins.maven.plugins.vbox.mediaregistry.MediaRegistry;
import de.innotek.virtualbox_settings.AttachedDeviceType;
import de.innotek.virtualbox_settings.OrderDevice;
import de.innotek.virtualbox_settings.StorageControllerType;
import de.innotek.virtualbox_settings.VirtualBox;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

/**
 * @goal create
 * @phase pre-integration-test
 */
public class CreateMojo extends AbstractVBoxesMojo {

	protected void execute(final VBox box) throws Exception {

		final Snapshot snapshot = Snapshot.POST_CREATION;
		if (box.exists(outputDirectory) && box.getSnapshots().contains(snapshot)) {
			box.restoreSnapshot(snapshot);
			return;
		}

		getLog().info("creating '" + box.getName() + "'");

		final VirtualBox vb = box.getVirtualBox();
		final VirtualBox.Machine machine = vb.getMachine();

		assert machine != null;

		final File t = box.getTarget(outputDirectory);
		if (!t.exists() && !t.mkdirs()) throw new IllegalStateException("failed to create " + t);

		ExecUtils.exec("vboxmanage", "createvm", "--name", box.getName(), "--ostype", machine.getOSType().value(), "--register", "--basefolder", t.getParentFile().getCanonicalPath());

		// set-up media
		final Map<Object, File> idToFile = createMedia(box, t, box.getMediaRegistry());

		configureMachine(box, machine);

		setupStorage(box, machine, idToFile);

		box.takeSnapshot(snapshot);
	}


	private void setupStorage(final VBox box, final VirtualBox.Machine m, final Map<Object, File> idToFile) throws IOException, InterruptedException {
		final Map<StorageControllerType, String> x = new HashMap<StorageControllerType, String>();
		x.put(StorageControllerType.PIIX_4, "ide");
		x.put(StorageControllerType.AHCI, "sata");
		x.put(StorageControllerType.I_82078, "floppy");

		final Map<AttachedDeviceType, String> y = new HashMap<AttachedDeviceType, String>();
		y.put(AttachedDeviceType.DVD, "dvddrive");
		y.put(AttachedDeviceType.HARD_DISK, "hdd");
		y.put(AttachedDeviceType.FLOPPY, "fdd");

		for (VirtualBox.Machine.StorageControllers.StorageController s : m.getStorageControllers().getStorageController()) {
			final String n = s.getName();
			getLog().debug("creating controller " + n);
			ExecUtils.exec("vboxmanage", "storagectl", box.getName(), "--name", n, "--add", x.get(s.getType()),
					"--bootable", s.isBootable() ? "on" : "off");

			for (VirtualBox.Machine.StorageControllers.StorageController.AttachedDevice a : s.getAttachedDevice()) {
				final String u = a.getImage().getUuid();
				final File f = idToFile.get(u);
				getLog().debug("attaching " + f + " (UUID " + u + ")");
				if (!f.exists()) throw new IllegalStateException(f + " does not exist");
				ExecUtils.exec("vboxmanage", "storageattach", box.getName(), "--storagectl", n,
						"--port", String.valueOf(a.getPort()),
						"--device", String.valueOf(a.getDevice()),
						"--type", y.get(a.getType()),
						"--medium", f.getCanonicalPath());
			}
		}
	}

	private Map<Object, File> createMedia(final VBox box, final File target, final MediaRegistry mr) throws IOException, InterruptedException, URISyntaxException {

		VBox.installAdditions();

		final Image hd = mr.getHardDisks().getHardDisk();

		getLog().debug("creating HD " + hd.getUuid());
		final File hdImg = new File(target, hd.getUuid() + ".vdi");
		if (hdImg.exists() && !hdImg.delete()) throw new IllegalStateException();
		ExecUtils.exec("vboxmanage", "createhd", "--filename", hdImg.getCanonicalPath(),
				"--size", String.valueOf(hd.getSize()));

		final Map<Object, File> idToFile = new HashMap<Object, File>();

		idToFile.put(hd.getUuid(), hdImg);

		final Image dvd = mr.getDVDImages().getDVDImage();
		idToFile.put(dvd.getUuid(), acquireImage(box, dvd));

		final Image floppy = mr.getFloppyImages().getFloppyImage();
		idToFile.put(floppy.getUuid(), acquireImage(box, floppy));

		getLog().debug("images " + idToFile);

		return idToFile;
	}

	private void configureMachine(final VBox box, final VirtualBox.Machine m) throws IOException, InterruptedException {
		getLog().info("configuring machine");

		final List<String> modifyVm = new ArrayList<String>(Arrays.asList("vboxmanage", "modifyvm", box.getName()));
		modifyVm.addAll(Arrays.asList("--memory", String.valueOf(m.getHardware().getMemory().getRAMSize())));

		if (m.getHardware().getBoot() != null) {
			final Map<OrderDevice, String> odm = new HashMap<OrderDevice, String>();
			getLog().info("setting boot order");
			odm.put(OrderDevice.NONE, "none");
			odm.put(OrderDevice.FLOPPY, "floppy");
			odm.put(OrderDevice.DVD, "dvd");
			odm.put(OrderDevice.HARD_DISK, "disk");

			for (VirtualBox.Machine.Hardware.Boot.Order o : m.getHardware().getBoot().getOrder()) {
				modifyVm.add("--boot" + o.getPosition());
				modifyVm.add(odm.get(o.getDevice()));
			}
		}

		for (VirtualBox.Machine.Hardware.Network.Adapter a : m.getHardware().getNetwork().getAdapter()) {
			if (a.getNAT() != null)
				modifyVm.addAll(Arrays.asList("--nic" + (a.getSlot() + 1), "nat"));
			else if (a.getBridgedInterface() != null)
				modifyVm.addAll(Arrays.asList("--nic" + (a.getSlot() + 1), "bridged", "--bridgeadapter" + a.getSlot(), a.getBridgedInterface().getName()));
			else
				throw new UnsupportedOperationException();
		}

		ExecUtils.exec(modifyVm.toArray(new String[modifyVm.size()]));
	}

	private File acquireImage(VBox box, Image image) throws IOException, URISyntaxException, InterruptedException {
		String location = image.getLocation();

		if (location.startsWith("http://")) {
			final File dest = new File(outputDirectory + "/vbox/downloads/" + box.getName() + "/" + image.getUuid() + ".iso");
			if (!dest.exists()) {
				if (!dest.getParentFile().exists() && !dest.getParentFile().mkdirs()) throw new IllegalStateException();
				getLog().info("downloading " + location + " to " + dest);
				FileUtils.copyURLToFile(new URL(location), dest);
			} else {
				getLog().info(dest + " already downloaded");
			}
			location = dest.toString();
		} else if (location.startsWith("file://")) {
			location = new URI(location).getPath();
		}

		final File src = new File(basedir + "/src/main/vbox/" + box.getName(), location);

		if (src.isDirectory() && image instanceof FloppyImage) {
			final File dest = new File(box.getTarget(outputDirectory) + "/" + image.getUuid() + ".img");

			getLog().info("creating floppy image for " + src + " as " + dest);

			ImageUtils.createFloppyImage(src, dest);

			location = dest.toString();
		}

		final File file = new File(location);

		if (!file.exists()) {
			throw new IllegalStateException(file + " does not exist");
		}

		return file;
	}
}
