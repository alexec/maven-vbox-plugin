package com.alexecollins.util;


import de.tu_darmstadt.informatik.rbg.hatlak.iso9660.ISO9660RootDirectory;
import de.tu_darmstadt.informatik.rbg.hatlak.iso9660.impl.CreateISO;
import de.tu_darmstadt.informatik.rbg.hatlak.iso9660.impl.ISO9660Config;
import de.tu_darmstadt.informatik.rbg.hatlak.iso9660.impl.ISOImageFileHandler;
import de.tu_darmstadt.informatik.rbg.hatlak.joliet.impl.JolietConfig;
import de.tu_darmstadt.informatik.rbg.hatlak.rockridge.impl.RockRidgeConfig;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.StreamHandler;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utils for creating disk images.
 *
 * @author alex.e.c@gmail.com
 */
public class ImageUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(ImageUtils.class);


	/**
     * Create an image based on file extension.
     */
    public static void createImage(final File work, final File src, final File dest) throws InterruptedException, ExecutionException, IOException {
        if (work == null || !work.isDirectory()) {throw new IllegalArgumentException("work is null or is not directory");}
        if (src == null || !src.isDirectory()) {throw new IllegalArgumentException("src is null or is not directory");}
        if (dest == null) {throw new IllegalArgumentException("dest must not be null and end in '.iso'");}

        if (dest.getName().endsWith(".img")) {
            createFloppyImage(work, src, dest);
        } else if (dest.getName().endsWith(".iso")) {
            createDVDImage(src, dest);
        } else {
            throw new UnsupportedOperationException("unknown file extension on " + dest);
        }
    }

	private static void createFloppyImage(File work, final File source, final File dest) throws IOException, InterruptedException, ExecutionException {

		// http://wiki.osdev.org/Disk_Images
        if (dest == null || !dest.getName().endsWith(".img")) {throw new IllegalArgumentException("dest must not be null and end in '.iso'");}

		final String os = System.getProperty("os.name");
		if (os.contains("Windows")) {
			final File f = new File(work, "vbox/downloads/bfi10.zip");

			if (!f.exists())
				FileUtils.copyURLToFile(new URL("ftp://dl.xs4all.nl/pub/mirror/nu2files/bfi10.zip"), f);

			assert f.exists();

			ZipUtils.unzip(f, f.getParentFile());

			ExecUtils.exec(new File(f.getParentFile(), "bfi.exe").getCanonicalPath(), "-f=" + dest.getCanonicalPath(), source.getCanonicalPath());
		} else if (os.contains("Mac")) {
			// http://www.jedi.be/blog/2009/11/17/commandline-creation-of-msdos-floppy-on-macosx/
			ExecUtils.exec("dd", "bs=512", "count=2880", "if=/dev/zero", "of=" + dest.getPath());
			final String dev = ExecUtils.exec("hdid", "-nomount", dest.getPath()).trim();
			try {
				ExecUtils.exec("newfs_msdos", dev);
			} finally {
				ExecUtils.exec("hdiutil", "detach",  dev);
			}
			final String out = ExecUtils.exec("hdid", dest.getPath());
			final Matcher m = Pattern.compile("  *(.*)").matcher(out);
			if (!m.find()) {
				throw new IllegalStateException("failed to find mount point in " + out);
			}
			final File device = new File(m.group(1).trim());
			try {
				FileUtils.copyDirectory(source, device);
			}   finally {
				ExecUtils.exec("hdiutil", "detach", device.getPath());
			}
		} else {
			LOGGER.warn("unsupported OS " + os + ", hoping it's *NIX and executing some un-tested code, email me at  alex.e.g@gmail.com if you see this message and you have any problems");

			// http://stackoverflow.com/questions/11202706/create-a-virtual-floppy-image-without-mount
			ExecUtils.exec("dd", "if=/dev/zero", "of=" + dest.getPath(), "count=1440","bs=1k");
			ExecUtils.exec("mkfs.msdos", dest.getPath());
			for (File f : source.listFiles()) {
				ExecUtils.exec("mcopy", "-s", "-i", dest.getPath(), f.getPath(), "::/");
			}
		}
	}

    /**
     * Create an ISO image for src.
     */
    private static void createDVDImage(final File src, final File dest) {

        if (dest == null || !dest.getName().endsWith(".iso")) {throw new IllegalArgumentException("dest must not be null and end in '.iso'");}

        final ISO9660RootDirectory root = new ISO9660RootDirectory();
        try {
            root.addContentsRecursively(src);
            final StreamHandler streamHandler = new ISOImageFileHandler(dest);
            CreateISO iso = new CreateISO(streamHandler, root);
            iso.process(new ISO9660Config(), new RockRidgeConfig(), new JolietConfig(), null);
        } catch (Exception e) {
            throw new RuntimeException("failed to create image", e);
        }
    }

	/*
	private static void createFloppyImage1(final File source, final File dest) throws Exception {

		final FileDisk disk = FileDisk.create(dest, (long) 1440 * 1024);
		final FatFileSystem fs = SuperFloppyFormatter.get(disk).format();
		for (final File file : source.listFiles()) {

			if (file.isFile()) {
				final FatLfnDirectoryEntry fe = fs.getRoot().addFile(file.getName());
				final FatFile floppyFile = fe.getFile();

				final FileInputStream fis = new FileInputStream(file);

				final FileChannel fci = fis.getChannel();
				final ByteBuffer buffer = ByteBuffer.allocate(1024);

				long counter = 0;
				long len = 0;

				//   http://www.kodejava.org/examples/49.html
				// Here we start to read the source file and write it
				// to the destination file. We repeat this process
				// until the read method of input stream channel return
				// nothing (-1).
				while (true) {
					// read a block of data and put it in the buffer
					final int read = fci.read(buffer);

					// did we reach the end of the channel? if yes
					// jump out the while-loop
					if (read == -1) {
						break;
					}

					// flip the buffer
					buffer.flip();

					// write to the destination channel
					floppyFile.write(counter * 1024, buffer);
					counter++;
					len += read;

					// clear the buffer and user it for the next read
					// process
					buffer.clear();
				}

				if (len != file.length()) {
					throw new AssertionError("expected to copy " + file.length() + ", copied " + len);
				}

				floppyFile.flush();

				fis.close();
			} else if (file.isDirectory()) {
				fs.getRoot().addDirectory(file.getName());
			} else {
				throw new UnsupportedOperationException();
			}
		}
		fs.close();
		disk.close();
	}
	*/
}
