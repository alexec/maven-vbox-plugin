package com.alexecollins.maven.plugins.vbox;

import com.alexecollins.maven.plugins.vbox.schema.AttachedDeviceType;
import com.alexecollins.maven.plugins.vbox.schema.VirtualBox;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @goal create
 * @phase pre-integration-test
 */
public class CreateMojo extends AbstractVBoxesMojo {

	protected void execute(URI src) throws Exception {

		final String name = getName(src);
		final Snapshot snapshot = Snapshot.POST_CREATION;
		if (exists(name) && getSnapshots(name).contains(snapshot.toString())) {
			exec("vboxmanage", "snapshot", name, "restore", snapshot.toString());
			return;
		}

		getLog().info("creating '" + name + "' (" + src + ")");

		final VirtualBox vb = getVirtualBox(src);

		final VirtualBox.Machine m = vb.getMachine();

		assert m != null;

		final File t = getTarget(name);
		if (!t.exists() && !t.mkdirs()) throw new IllegalStateException("failed to create " + t);

		exec("vboxmanage", "createvm", "--name", name, "--ostype", m.getOSType().value(), "--register", "--basefolder", t.getParentFile().getCanonicalPath());

		final VirtualBox.Machine.MediaRegistry mr = m.getMediaRegistry();
		final VirtualBox.Machine.MediaRegistry.HardDisks.HardDisk hd = mr.getHardDisks().getHardDisk();

		getLog().debug("creating HD " + hd.getUuid());
		final File hdImg = new File(t, hd.getUuid() + ".vdi");
		if (hdImg.exists() && !hdImg.delete()) throw new IllegalStateException();
		exec("vboxmanage", "createhd", "--filename", hdImg.getCanonicalPath(),
				"--size", String.valueOf(hd.getSize()));

		final Map<Object, File> idToFile = new HashMap<Object, File>();

		idToFile.put(hd.getUuid(), hdImg);

		final VirtualBox.Machine.MediaRegistry.DVDImages.DVDImage dvd = mr.getDVDImages().getDVDImage();
		String location = dvd.getLocation();

		if (location.startsWith("http://")) {
			final File dest = new File("target/vbox/downloads/" + name + "/dvd0.iso");
			if (!dest.exists()) {
				if (!dest.getParentFile().exists() && !dest.getParentFile().mkdirs()) throw new IllegalStateException();
				getLog().info("downloading " + location + " to " + dest);
				FileUtils.copyURLToFile(new URL(location), dest);
			} else {
				getLog().info(dest + " already downloaded");
			}
			location = dest.toString();
		}

		idToFile.put(dvd.getUuid(), new File(location));

		getLog().debug("images " + idToFile);

		int i = 1;
		for (VirtualBox.Machine.Hardware.Network.Adapter a : m.getHardware().getNetwork().getAdapter()) {
			getLog().info("adding adapter " + i);
			if (a.getNAT() != null)
				exec("vboxmanage", "modifyvm", name, "--nic" + i, "nat");
			else if (a.getBridgedInterface() != null) {
				exec("vboxmanage", "modifyvm", name, "--nic" + i, "bridged", "--bridgeadapter" + i, a.getBridgedInterface().getName());
			}
			i++;
		}

		for (VirtualBox.Machine.StorageControllers.StorageController s : m.getStorageControllers().getStorageController()) {
			final String n = s.getName() != null ? s.getName() : s.getType() + " Controller";
			getLog().debug("creating controller " + n);
			exec("vboxmanage", "storagectl", name, "--name", n, "--add", s.getType().toString().toLowerCase(),
					"--bootable", s.isBootable() ? "on" : "off");

			final VirtualBox.Machine.StorageControllers.StorageController.AttachedDevice a = s.getAttachedDevice();
			final Object u = a.getImage().getUuid();
			File f = idToFile.get(u);
			getLog().debug("attaching " + f + " (UUID " + u + ")");
			if (!f.exists()) throw new IllegalStateException(f + " does not exist");
			exec("vboxmanage", "storageattach", name, "--storagectl", n, "--port", "0", "--device", "0",
					"--type", a.getType() == AttachedDeviceType.DVD ? "dvddrive" : "hdd",
					"--medium", f.getCanonicalPath());
		}

		exec("vboxmanage", "snapshot", name, "take", snapshot.toString());
	}
}
