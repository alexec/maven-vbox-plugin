package com.alexecollins.util;

import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

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

			f.delete();

			System.out.println(f.getCanonicalPath());

			final Thread t = new Thread(new Runnable() {
				public void run() {
					try {
						FileUtils2.copyURLToFile(u, f);
					} catch (IOException e) {
						e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
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
	}
