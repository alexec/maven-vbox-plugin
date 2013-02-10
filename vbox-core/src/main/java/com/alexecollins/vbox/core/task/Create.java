package com.alexecollins.vbox.core.task;

import com.alexecollins.util.ExecUtils;
import com.alexecollins.util.FileUtils2;
import com.alexecollins.util.ImageUtils;
import com.alexecollins.vbox.core.Snapshot;
import com.alexecollins.vbox.core.VBox;
import com.alexecollins.vbox.core.Work;
import com.alexecollins.vbox.mediaregistry.DVDImage;
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
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * Create a new virtual box.
 */
public class Create extends AbstractTask {
	private static final Logger LOGGER = LoggerFactory.getLogger(Create.class);

	public Create(final Work work, final VBox box) {
		super(work, box);
	}

	public Void call() throws Exception {

		verifySignature();

		final Snapshot snapshot = Snapshot.POST_CREATION;
		if (box.exists()) {

			box.powerOff();

			if (box.getSnapshots().contains(snapshot)) {
				LOGGER.info("restoring '" + box.getName() + "' from snapshot " + snapshot);
				box.restoreSnapshot(snapshot);
				return null;
			}
			// the box may have been created, but this was incomplete,
			// hence the lack of snapshot, delete and try again
			box.unregister();
		}

		LOGGER.info("creating '" + box.getName() + "'");

		final VirtualBox vb = box.getVirtualBox();
		final VirtualBox.Machine machine = vb.getMachine();

		assert machine != null;

		final File t = getTarget();
		if (t.exists()) {
			LOGGER.info("deleting " + t);
			FileUtils.deleteDirectory(t);
		}
		if (!t.mkdirs()) throw new IllegalStateException("failed to create " + t);

		FileUtils.writeByteArrayToFile(getSignatureFile(), getSignature());

		final String osType = machine.getOSType();
		final Set<VBox.OSType> osTypes = VBox.getOSTypes();
		if (!osTypes.contains(new VBox.OSType(osType))) {
			throw new IllegalStateException("invalid OS " +osType +",  must be one of " + osTypes);
		}

		// set-up media before we create box, so any problems are easier to fix
		final Map<Object, File> idToFile = createMedia(box, t, box.getMediaRegistry());

        ExecUtils.exec("vboxmanage", "createvm", "--name", box.getName(), "--ostype", osType, "--register", "--basefolder", t.getParentFile().getCanonicalPath());

        configureMachine(box, machine.getHardware());

		setupStorage(box, machine.getStorageControllers(), idToFile);

		box.takeSnapshot(snapshot);
		return null;
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
					"--sataportcount", String.valueOf(s.getPortCount()),
					"--hostiocache", s.isUseHostIOCache() ? "on" : "off"
			);

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

		final Map<Object, File> idToFile = new HashMap<Object, File>();

		for (final Image hd : mr.getHardDisks().getHardDisk()) {
			LOGGER.debug("creating HD " + hd.getUuid());
			final File hdImg = new File(target, hd.getUuid() + ".vdi");
			if (hdImg.exists() && !hdImg.delete()) throw new IllegalStateException();
			ExecUtils.exec("vboxmanage", "createhd", "--filename", hdImg.getCanonicalPath(),
					"--size", String.valueOf(hd.getSize()));
			idToFile.put(hd.getUuid(), hdImg);
		}

		for (DVDImage dvd : mr.getDVDImages().getDVDImage()) {
			idToFile.put(dvd.getUuid(), acquireImage(box, dvd));
		}

		for (final Image floppy : mr.getFloppyImages().getFloppyImage()) {
			idToFile.put(floppy.getUuid(), acquireImage(box, floppy));
		}

		LOGGER.debug("images " + idToFile);

		return idToFile;
	}

	private void configureMachine(final VBox box, final VirtualBox.Machine.Hardware hardware) throws IOException, InterruptedException, ExecutionException {
		LOGGER.info("configuring machine");

		final List<String> modifyVm = new ArrayList<String>(Arrays.asList("vboxmanage", "modifyvm", box.getName()));
		modifyVm.addAll(Arrays.asList("--memory", String.valueOf(hardware.getMemory().getRAMSize())));

		final VirtualBox.Machine.Hardware.CPU c = hardware.getCPU();
		final int cpus = c != null ? c.getCount() : 1;
		modifyVm.add("--cpus");
		modifyVm.add(String.valueOf(cpus));

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
		boolean ioAcpi = false;
		if (b != null) {
			final VirtualBox.Machine.Hardware.BIOS.ACPI a = b.getACPI();
			if (a != null) {
				modifyVm.add("--acpi");
				modifyVm.add(a.isEnabled() ? "on" : "off");
			}
			final VirtualBox.Machine.Hardware.BIOS.IOACPI i = b.getIOACPI();
			if (i != null) {
				modifyVm.add("--ioapic");
				modifyVm.add(i.isEnabled() ? "on" : "off");
				ioAcpi = i.isEnabled();
			}
		}

		if (cpus > 1 && !ioAcpi)
			LOGGER.warn("multiple CPUs require IOACPI enabled, you probably want to enable this");



		final Set<Integer> usedSlots = new HashSet<Integer>();
		for (VirtualBox.Machine.Hardware.Network.Adapter a : hardware.getNetwork().getAdapter()) {

			if (!usedSlots.add(a.getSlot())) {
				throw new IllegalStateException("used slot "  +a.getSlot() + " twice");
			}

			final int n = a.getSlot() + 1;

			modifyVm.addAll(Arrays.asList("--cableconnected" + n, a.isCable() ? "on" : "off"));

			if (a.getNAT() != null)
				modifyVm.addAll(Arrays.asList("--nic" + n, "nat"));
			else if (a.getBridgedInterface() != null)
				modifyVm.addAll(Arrays.asList("--nic" + n, "bridged", "--bridgeadapter" + n, a.getBridgedInterface().getName()));
			else if (a.getHostOnlyInterface() != null) {
                final String adapter = a.getHostOnlyInterface().getName();
                if (!VBox.findHostOnlyInterfaces().contains(adapter)) {
                    throw new IllegalStateException("host-only interface '" + adapter + "' does not exist, you will need to create it in the VirtualBox settings");
                }
                modifyVm.addAll(Arrays.asList("--nic" + n, "hostonly", "--hostonlyadapter" + n, adapter));
            } else if (a.getInternalNetwork() != null)
				modifyVm.addAll(Arrays.asList("--nic" + n, "intnet", "--intnet" + n, a.getInternalNetwork().getName()));
			else
				throw new UnsupportedOperationException();
		}

		final VirtualBox.Machine.Hardware.IO io = hardware.getIO();
		if (io != null) {
			final VirtualBox.Machine.Hardware.IO.IoCache ic = io.getIoCache();
			if (ic != null) {
				// TODO nop
			}
		}

		ExecUtils.exec(modifyVm.toArray(new String[modifyVm.size()]));
	}

	File acquireImage(VBox box, Image image) throws IOException, URISyntaxException, InterruptedException, ExecutionException {
		String location = subst(image.getLocation());

		if (location.startsWith("http://") || location.startsWith("ftp://")) {
            // 1. make sure the file is in the download cache, this means if we have multiple machines
            // then we won't re-download the same file
            final File cache = new File(work.getCacheDir(), URLEncoder.encode(location, "UTF-8"));
            if (!cache.exists()) {
                if (!cache.getParentFile().exists() && !cache.getParentFile().mkdirs()) throw new IllegalStateException();
                LOGGER.info("downloading " + location + " to " + cache);
                FileUtils2.copyURLToFile(new URL(location), cache);
            }
            // 2. copy the cached version to the dest, if the dest might need freshening
            final File dest = new File(getTarget(), image.getUuid() + ".iso");
            if (!dest.exists() || dest.lastModified() != cache.lastModified()) {
                LOGGER.info("copying " + cache + " to " + dest);
                FileUtils.copyFile(cache, dest);
                if (!dest.setLastModified(cache.lastModified())) {
	                throw new IllegalStateException("failed to set last modified of " + dest);
                }
            }
			location = dest.toString();
		} else if (location.startsWith("file://")) {
			location = new URI(location).getPath();
		}

		final File src = new File(box.getSrc().toURL().getFile(), location);

		if (src.isDirectory())
            if (image instanceof FloppyImage) {
                final File dest = new File(getTarget(), image.getUuid() + ".img");

                LOGGER.info("creating floppy image for " + src + " as " + dest);

                ImageUtils.createImage(work, src, dest);

			    location = dest.toString();
            } else if (image instanceof DVDImage) {
                final File dest = new File(getTarget(), image.getUuid() + ".iso");

                LOGGER.info("creating ISO image for " + src + " as " + dest);

                ImageUtils.createImage(work, src, dest);

                location = dest.toString();

            } else {
                throw new UnsupportedOperationException("do not know how to create image for " + src);
		}

		final File file = new File(location);

		if (!file.exists()) {
			throw new IllegalStateException(file + " does not exist");
		}

		return file;
	}
}