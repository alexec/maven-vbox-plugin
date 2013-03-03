package com.alexecollins.util;

import org.apache.commons.io.FileUtils;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

import static junit.framework.Assert.*;
import static org.junit.Assert.assertArrayEquals;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class FileUtils2Test {
	@Test
	@Ignore
	public void testCopyURLToFile() throws Exception {
			final URL u = new URL("http://www.alexecollins.com/sites/default/files/alex.e.collins_2.png");
			final int s = 117741;
			final File f = File.createTempFile("alex", "jpg");

			assert f.delete();

			System.out.println(f.getCanonicalPath());

			final Thread t = new Thread(new Runnable() {
				public void run() {
					try {
						FileUtils2.copyURLToFile(u, f);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			t.start();
			Thread.sleep(150);
			t.stop();

			Thread.sleep(100);

			assertTrue(f.length() > 0);
			assertTrue(f.length() < s) ;

			FileUtils2.copyURLToFile(u, f);

			assertEquals(s, f.length());
		}

	@Ignore
	@Test
	public void testSignature() throws Exception {
		final File f = new File("test");
		FileUtils.touch(f);
		final byte[] x = FileUtils2.getSignature(f);
		assertArrayEquals(x, FileUtils2.getSignature(f));
		assert f.renameTo(new File("test1"));
		final byte[] y = FileUtils2.getSignature(f);
		System.out.println(Arrays.toString(x));
		System.out.println(Arrays.toString(y));
		assertFalse(Arrays.equals(x, y));
		assert f.delete();
	}
}