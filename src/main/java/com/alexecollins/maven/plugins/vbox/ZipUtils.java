package com.alexecollins.maven.plugins.vbox;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipUtils {

	private static final void copyInputStream(InputStream in, OutputStream out)
			throws IOException {
		byte[] buffer = new byte[1024];
		int len;

		while ((len = in.read(buffer)) >= 0)
			out.write(buffer, 0, len);

		in.close();
		out.close();
	}

	public static final void unzip(final File file, final File toDir) throws IOException {
		ZipFile zipFile = new ZipFile(file);

		Enumeration entries = zipFile.entries();

		while (entries.hasMoreElements()) {
			ZipEntry entry = (ZipEntry) entries.nextElement();

			if (entry.isDirectory()) {
				// Assume directories are stored parents first then children.
				// This is not robust, just for demonstration purposes.
				new File(entry.getName()).mkdir();
				continue;
			}

			copyInputStream(zipFile.getInputStream(entry),
					new BufferedOutputStream(new FileOutputStream(new File(toDir, entry.getName()))));
		}

		zipFile.close();
	}
}
