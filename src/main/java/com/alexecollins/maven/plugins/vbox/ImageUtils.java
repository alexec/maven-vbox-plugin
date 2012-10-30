package com.alexecollins.maven.plugins.vbox;

import de.waldheinz.fs.fat.FatFile;
import de.waldheinz.fs.fat.FatFileSystem;
import de.waldheinz.fs.fat.FatLfnDirectoryEntry;
import de.waldheinz.fs.fat.SuperFloppyFormatter;
import de.waldheinz.fs.util.FileDisk;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author alex.collins
 */
public class ImageUtils {

	public static void createFloppyImage(final File source, final File dest) throws IOException, InterruptedException {

		assert dest.getName().endsWith(".img");

		// TODO - non-Windows support!

		final File f = new File("target/vbox/downloads", "bfi10.zip");

		if (!f.exists())
			FileUtils.copyURLToFile(new URL("ftp://dl.xs4all.nl/pub/mirror/nu2files/bfi10.zip"), f);

		assert f.exists();

		ZipUtils.unzip(f, f.getParentFile());

		ExecUtils.exec(new File(f.getParentFile(), "bfi.exe").getCanonicalPath(), "-f=" + dest.getCanonicalPath(), source.getCanonicalPath());
	}

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
}
