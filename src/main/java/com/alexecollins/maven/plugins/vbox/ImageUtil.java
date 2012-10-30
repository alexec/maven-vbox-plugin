package com.alexecollins.maven.plugins.vbox;

import de.waldheinz.fs.fat.FatFile;
import de.waldheinz.fs.fat.FatFileSystem;
import de.waldheinz.fs.fat.FatLfnDirectoryEntry;
import de.waldheinz.fs.fat.SuperFloppyFormatter;
import de.waldheinz.fs.util.FileDisk;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author alex.collins
 */
public class ImageUtil {

	public static void createFloppyImage(final File source, final File dest) throws IOException {

		assert dest.getName().endsWith(".img");

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
