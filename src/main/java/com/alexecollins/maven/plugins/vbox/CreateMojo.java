package com.alexecollins.maven.plugins.vbox;

import com.alexecollins.maven.plugins.vbox.mediaregistry.FloppyImage;
import com.alexecollins.maven.plugins.vbox.mediaregistry.Image;
import com.alexecollins.maven.plugins.vbox.mediaregistry.MediaRegistry;
import com.alexecollins.maven.plugins.vbox.virtualbox.AttachedDeviceType;
import com.alexecollins.maven.plugins.vbox.virtualbox.StorageControllerType;
import com.alexecollins.maven.plugins.vbox.virtualbox.VirtualBox;
import de.waldheinz.fs.fat.FatFile;
import de.waldheinz.fs.fat.FatFileSystem;
import de.waldheinz.fs.fat.FatLfnDirectoryEntry;
import de.waldheinz.fs.fat.SuperFloppyFormatter;
import de.waldheinz.fs.util.FileDisk;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
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

		final MediaRegistry mr = m.getMediaRegistry();
		final Image hd = mr.getHardDisks().getHardDisk();

		getLog().debug("creating HD " + hd.getUuid());
		final File hdImg = new File(t, hd.getUuid() + ".vdi");
		if (hdImg.exists() && !hdImg.delete()) throw new IllegalStateException();
		exec("vboxmanage", "createhd", "--filename", hdImg.getCanonicalPath(),
				"--size", String.valueOf(hd.getSize()));

		final Map<Object, File> idToFile = new HashMap<Object, File>();

		idToFile.put(hd.getUuid(), hdImg);

		final Image dvd = mr.getDVDImages().getDVDImage();
		idToFile.put(dvd.getUuid(), acquireImage(name, dvd));

		final Image floppy = mr.getFloppyImages().getFloppyImage();
		idToFile.put(floppy.getUuid(), acquireImage(name, floppy));

		getLog().debug("images " + idToFile);

		exec("vboxmanage", "modifyvm", name, "--memory", String.valueOf(m.getHardware().getMemory().getRAMSize()));

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

		final Map<StorageControllerType, String> x = new HashMap<StorageControllerType, String>();
		x.put(StorageControllerType.PIIX_4, "ide");
		x.put(StorageControllerType.AHCI, "sata");
		x.put(StorageControllerType.I_82078, "floppy");

		for (VirtualBox.Machine.StorageControllers.StorageController s : m.getStorageControllers().getStorageController()) {
			final String n = s.getName();
			getLog().debug("creating controller " + n);
			exec("vboxmanage", "storagectl", name, "--name", n, "--add", x.get(s.getType()),
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

	private File acquireImage(String name, Image image) throws IOException {
		String location = image.getLocation();

		if (location.startsWith("http://")) {
			final File dest = new File("target/vbox/downloads/" + name + "/" + image.getUuid() + ".iso");
			if (!dest.exists()) {
				if (!dest.getParentFile().exists() && !dest.getParentFile().mkdirs()) throw new IllegalStateException();
				getLog().info("downloading " + location + " to " + dest);
				FileUtils.copyURLToFile(new URL(location), dest);
			} else {
				getLog().info(dest + " already downloaded");
			}
			location = dest.toString();
		}
		final File i = new File(location);

		if (i.isDirectory() && image instanceof FloppyImage) {
			getLog().info("creating floppy image for " + i);
			final File dest = new File("target/vbox/downloads/" + name + "/" + image.getUuid() + ".img");

			createFloppyImage(i, dest);
			location = dest.toString();
		}

		return new File(location);
	}

	private void createFloppyImage(File i, File dest) throws IOException {
		final FileDisk disk = FileDisk.create(dest, 1440 * 1024);
		final FatFileSystem fs = SuperFloppyFormatter.get(disk).format();
		for (File file : i.listFiles()) {
			final FatLfnDirectoryEntry fe = fs.getRoot().addFile(file.getName());
			final FatFile floppyFile = fe.getFile();
			if (file.isFile()) {
				FileInputStream fis = new FileInputStream(file);

				FileChannel fci = fis.getChannel();
				ByteBuffer buffer = ByteBuffer.allocate(1024);

				long counter = 0;

				//   http://www.kodejava.org/examples/49.html
				// Here we start to read the source file and write it
				// to the destination file. We repeat this process
				// until the read method of input stream channel return
				// nothing (-1).
				while (true) {
					// read a block of data and put it in the buffer
					int read = fci.read(buffer);

					// did we reach the end of the channel? if yes
					// jump out the while-loop
					if (read == -1) {
						break;
					}

					// flip the buffer
					buffer.flip();

					// write to the destination channel
					System.out.print(".");
					floppyFile.write(counter * 1024, buffer);
					counter++;

					// clear the buffer and user it for the next read
					// process
					buffer.clear();
				}

				floppyFile.flush();

				fis.close();
			}
		}
		fs.close();
		disk.close();
	}
}
