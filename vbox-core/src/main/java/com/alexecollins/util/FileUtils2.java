package com.alexecollins.util;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
		// System.out.println("requesting " + connection + " range >= " + downloaded);
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

	/**
	 * Generate a cheap signature for the directory using the file names and modification time.
	 */
	public static byte[] getSignature(final File f) throws NoSuchAlgorithmException, IOException {
		final MessageDigest digest = MessageDigest.getInstance("MD5");
		calcSignature(f, f, digest);
		return digest.digest();
	}

	private static void calcSignature(final File root, final File f, final MessageDigest d) throws IOException {
		final String v = root.toURI().relativize(f.toURI()).toString();
		d.update(v.getBytes());
		final long l = f.length(); // not great, but not as variable, I hope...
		// System.out.println(v + "  "  +l);
		d.update(String.valueOf(l).getBytes());
		if (f.isDirectory()) {
			for (File c : f.listFiles()) {
				calcSignature(root, c, d) ;
			}
		}
	}
}
