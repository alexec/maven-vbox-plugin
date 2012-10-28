package com.alexecollins.maven.plugins.vbox;

import com.alexecollins.maven.plugins.vbox.schema.AttachedDeviceType;
import com.alexecollins.maven.plugins.vbox.schema.VirtualBox;

import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * @goal create
 * @phase pre-integration-test
 */
public class CreateMojo extends AbstractVBoxMojo {

	protected void execute(URI src) throws Exception {

		final String name = getName(src);

		getLog().info("creating '" + name + "'");

		final VirtualBox vb = getCfg(src);

		final VirtualBox.Machine m = vb.getMachine();

		exec("vboxmanage", "createvm", "--name", name, "--ostype", m.getOSType().value(), "--register", "--basefolder", getTarget(name).getCanonicalPath());

		final VirtualBox.Machine.MediaRegistry mr = m.getMediaRegistry();
		final VirtualBox.Machine.MediaRegistry.HardDisks.HardDisk hd = mr.getHardDisks().getHardDisk();

		getLog().debug("creating HD " + hd.getUuid());
		final File hdImg = new File(getTarget(name), hd.getUuid() + ".vdi");
		exec("vboxmanage", "createhd", "--filename", hdImg.getCanonicalPath(),
				"--size", String.valueOf(hd.getSize()));

		final Map<Object, File> idToFile = new HashMap<Object, File>();

		idToFile.put(hd.getUuid(), hdImg);

		final VirtualBox.Machine.MediaRegistry.DVDImages.DVDImage dvd = mr.getDVDImages().getDVDImage();

		idToFile.put(dvd.getUuid(), new File(dvd.getLocation()));

		getLog().debug("images " + idToFile);

		getLog().debug("adding network adapters");

		m.getHardware().getNetwork().getAdapter().getNAT();

		exec("vboxmanage", "modifyvm", name, "--nic1", "nat");

		for (VirtualBox.Machine.StorageControllers.StorageController s : m.getStorageControllers().getStorageController()) {
			final String n = s.getName() != null ? s.getName() : s.getType() + " Controller";
			getLog().debug("creating controller " + n);
			exec("vboxmanage", "storagectl", name, "--name", n, "--add", s.getType().toString().toLowerCase(),
					"--bootable", s.isBootable() ? "on" : "off");

			final VirtualBox.Machine.StorageControllers.StorageController.AttachedDevice a = s.getAttachedDevice();
			final Object u = a.getImage().getUuid();
			final File f = idToFile.get(u);
			getLog().debug("attaching " + f + " (UUID " + u + ")");
			exec("vboxmanage", "storageattach", name, "--storagectl", n, "--port", "0", "--device", "0",
					"--type", a.getType() == AttachedDeviceType.DVD ? "dvddrive" : "hdd",
					"--medium", f.getCanonicalPath());
		}
	}
}
