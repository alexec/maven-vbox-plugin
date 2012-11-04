package com.alexecollins.vbox.core.task;

import com.alexecollins.util.ExecUtils;
import com.alexecollins.util.ImageUtils;
import com.alexecollins.vbox.core.Snapshot;
import com.alexecollins.vbox.core.VBox;
import com.alexecollins.vbox.mediaregistry.FloppyImage;
import com.alexecollins.vbox.mediaregistry.Image;
import com.alexecollins.vbox.mediaregistry.MediaRegistry;
import com.google.common.collect.ImmutableMap;
import de.innotek.virtualbox_settings.AttachedDeviceType;
import de.innotek.virtualbox_settings.OrderDevice;
import de.innotek.virtualbox_settings.StorageControllerType;
import de.innotek.virtualbox_settings.VirtualBox;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class Create extends AbstractInvokable  {
	private static final Logger LOGGER = LoggerFactory.getLogger(Create.class);
	private final VBox box;

	public Create(final File work, final VBox box) {
		super(work);
		this.box = box;
	}

	public void invoke() throws Exception {

		final Snapshot snapshot = Snapshot.POST_CREATION;
		if (box.exists()) {
			if (box.getSnapshots().contains(snapshot)) {
				box.restoreSnapshot(snapshot);
				return;
			}
			// the box may have been created, but this was incomplete,
			// hence the lack of snapshot, delete and try again
			box.unregister();
		}

		LOGGER.info("creating '" + box.getName() + "'");

		final VirtualBox vb = box.getVirtualBox();
		final VirtualBox.Machine machine = vb.getMachine();

		assert machine != null;

		final File t = getTarget(box);
		if (!t.mkdirs()) throw new IllegalStateException("failed to create " + t);

		ExecUtils.exec("vboxmanage", "createvm", "--name", box.getName(), "--ostype", machine.getOSType().value(), "--register", "--basefolder", t.getParentFile().getCanonicalPath());

		// set-up media
		final Map<Object, File> idToFile = createMedia(box, t, box.getMediaRegistry());

		configureMachine(box, machine.getHardware());

		setupStorage(box, machine.getStorageControllers(), idToFile);

		box.takeSnapshot(snapshot);
	}

	private void setupStorage(final VBox box, final VirtualBox.Machine.StorageControllers controllers, final Map<Object, File> idToFile) throws IOException, InterruptedException, ExecutionException {
		final Map<StorageControllerType, String> x = ImmutableMap.of(
				StorageControllerType.PIIX_4, "ide",
				StorageControllerType.AHCI, "sata",
				StorageControllerType.I_82078, "floppy"
		);

		final Map<AttachedDeviceType, String> y = ImmutableMap.of(
				AttachedDeviceType.DVD, "dvddrive",
				AttachedDeviceType.HARD_DISK, "hdd",
				AttachedDeviceType.FLOPPY, "fdd"
		);

		for (VirtualBox.Machine.StorageControllers.StorageController s : controllers.getStorageController()) {
			final String n = s.getName();
			LOGGER.debug("creating controller " + n);
			ExecUtils.exec("vboxmanage", "storagectl", box.getName(), "--name", n, "--add", x.get(s.getType()),
					"--bootable", s.isBootable() ? "on" : "off",
					"--hostiocache", s.isUseHostIOCache() ? "on" : "off");

			for (VirtualBox.Machine.StorageControllers.StorageController.AttachedDevice a : s.getAttachedDevice()) {
				final String u = a.getImage().getUuid();
				final File f = idToFile.get(u);
				LOGGER.debug("attaching " + f + " (UUID " + u + ")");
				if (!f.exists()) throw new IllegalStateException(f + " does not exist");
				ExecUtils.exec("vboxmanage", "storageattach", box.getName(), "--storagectl", n,
						"--port", String.valueOf(a.getPort()),
						"--device", String.valueOf(a.getDevice()),
						"--type", y.get(a.getType()),
						"--medium", f.getCanonicalPath());
			}
		}
	}

	private Map<Object, File> createMedia(final VBox box, final File target, final MediaRegistry mr) throws IOException, InterruptedException, URISyntaxException, ExecutionException {

		VBox.installAdditions(work);

		final Image hd = mr.getHardDisks().getHardDisk();

		LOGGER.debug("creating HD " + hd.getUuid());
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

		LOGGER.debug("images " + idToFile);

		return idToFile;
	}

	private void configureMachine(final VBox box, final VirtualBox.Machine.Hardware hardware) throws IOException, InterruptedException, ExecutionException {
		LOGGER.info("configuring machine");

		final List<String> modifyVm = new ArrayList<String>(Arrays.asList("vboxmanage", "modifyvm", box.getName()));
		modifyVm.addAll(Arrays.asList("--memory", String.valueOf(hardware.getMemory().getRAMSize())));

		final VirtualBox.Machine.Hardware.CPU c = hardware.getCPU();
		modifyVm.add("--cpus");
		modifyVm.add(String.valueOf(c != null ? c.getCount() : 1));

		if (hardware.getBoot() != null) {
			LOGGER.info("setting boot order");
			final Map<OrderDevice, String> odm = ImmutableMap.of(
					OrderDevice.NONE, "none",
					OrderDevice.FLOPPY, "floppy",
					OrderDevice.DVD, "dvd",
					OrderDevice.HARD_DISK, "disk");

			for (VirtualBox.Machine.Hardware.Boot.Order o : hardware.getBoot().getOrder()) {
				modifyVm.add("--boot" + o.getPosition());
				modifyVm.add(odm.get(o.getDevice()));
			}
		}

		// http://askubuntu.com/questions/82015/shutting-down-ubuntu-server-running-in-headless-virtualbox
		final VirtualBox.Machine.Hardware.BIOS b = hardware.getBIOS();
		if (b != null) {
			final VirtualBox.Machine.Hardware.BIOS.ACPI a = b.getACPI();
			if (a != null) {
				modifyVm.add("--acpi");
				modifyVm.add(a.isEnabled() ? "on" : "off");
			}
		}

		for (VirtualBox.Machine.Hardware.Network.Adapter a : hardware.getNetwork().getAdapter()) {
			if (a.getNAT() != null)
				modifyVm.addAll(Arrays.asList("--nic" + (a.getSlot() + 1), "nat"));
			else if (a.getBridgedInterface() != null)
				modifyVm.addAll(Arrays.asList("--nic" + (a.getSlot() + 1), "bridged", "--bridgeadapter" + a.getSlot(), a.getBridgedInterface().getName()));
			else
				throw new UnsupportedOperationException();
		}

		final VirtualBox.Machine.Hardware.IO io = hardware.getIO();
		if (io != null) {
			final VirtualBox.Machine.Hardware.IO.IOCache ic = io.getIOCache();
			if (ic != null) {
				// TODO nop
			}
		}

		ExecUtils.exec(modifyVm.toArray(new String[modifyVm.size()]));
	}

	public File acquireImage(VBox box, Image image) throws IOException, URISyntaxException, InterruptedException, ExecutionException {
		String location = image.getLocation();

		if (location.startsWith("http://") || location.startsWith("ftp://")) {
			final File dest = new File(work, "vbox/downloads/" + box.getName() + "/" + image.getUuid() + ".iso");
			if (!dest.exists()) {
				if (!dest.getParentFile().exists() && !dest.getParentFile().mkdirs()) throw new IllegalStateException();
				LOGGER.info("downloading " + location + " to " + dest);
				FileUtils.copyURLToFile(new URL(location), dest);
			} else {
				LOGGER.info(dest + " already downloaded");
			}
			location = dest.toString();
		} else if (location.startsWith("file://")) {
			location = new URI(location).getPath();
		}

		final File src = new File("src/main/vbox/" + box.getName(), location);

		if (src.isDirectory() && image instanceof FloppyImage) {
			final File dest = new File(getTarget(box), image.getUuid() + ".img");

			LOGGER.info("creating floppy image for " + src + " as " + dest);

			ImageUtils.createFloppyImage(work, src, dest);

			location = dest.toString();
		}

		final File file = new File(location);

		if (!file.exists()) {
			throw new IllegalStateException(file + " does not exist");
		}

		return file;
	}
}