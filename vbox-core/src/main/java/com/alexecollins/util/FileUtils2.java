package com.alexecollins.util;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class FileUtils2 {

	/**
	 * With resume for large downloads.
	 *
	 * @see org.apache.commons.io.FileUtils#copyURLToFile(java.net.URL, java.io.File)
	 */
	public static void copyURLToFile(URL url, File file) throws IOException {
		final File marker = new File(file + ".marker");
        final String markerMsg = "marker file that indicates " + url + " downloaded OK";

		if (marker.exists() && FileUtils.readFileToString(marker).equals(markerMsg)) {return;}

		URLConnection connection = url.openConnection();
		if (connection instanceof HttpURLConnection) {
			getHttpUrl(file, (HttpURLConnection)connection);
		} else {
			FileUtils.copyURLToFile(url, file);
		}
        FileUtils.writeStringToFile(marker, markerMsg);
	}

	private static void getHttpUrl(File file, HttpURLConnection connection) throws IOException {
		final long downloaded = file.exists() ? file.length() : 0;
		// LOGGER.info("requesting " + url + " range >= " + downloaded);
		connection.setRequestProperty("Range", "bytes=" + downloaded + "-");
		connection.setDoInput(true);
		connection.setDoOutput(true);
		final BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
		try {
			final BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file, true), 1024);
			try {
				final byte[] data = new byte[1024];
				int x;
				while ((x = in.read(data, 0, 1024)) >= 0) {
					out.write(data, 0, x);
				}
			} finally {
				out.close();
			}
		} finally {
			in.close();
		}
	}
}
