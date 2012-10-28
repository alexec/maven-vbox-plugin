package com.alexecollins.maven.plugins.vbox;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class ScanCodesTest {
	@Test
	public void testGetScanCode() throws Exception {

		assertArrayEquals(new int[]{0x001}, ScanCodes.forKey("Esc"));
		assertArrayEquals(new int[]{0x002}, ScanCodes.forKey("1"));
		assertArrayEquals(new int[]{0x02a, 0x002}, ScanCodes.forKey("!"));
		assertArrayEquals(new int[]{0x01c}, ScanCodes.forKey("Enter"));
		assertArrayEquals(new int[]{0x02a, 0x027}, ScanCodes.forKey(":"));
		assertArrayEquals(new int[]{0x01e}, ScanCodes.forKey("a"));
		assertArrayEquals(new int[]{0x02a, 0x01e}, ScanCodes.forKey("A"));
		assertArrayEquals(new int[]{0x031}, ScanCodes.forKey("n"));
		assertArrayEquals(new int[]{0x026}, ScanCodes.forKey("l"));
		assertArrayEquals(new int[]{0x035}, ScanCodes.forKey("/"));
		assertArrayEquals(new int[]{0x022}, ScanCodes.forKey("g"));
	}

	@Test
	public void testText() {
		assertArrayEquals(new int[]{0x002, 0x02a, 0x002}, ScanCodes.forString("1!"));
		assertArrayEquals(new int[]{0x039}, ScanCodes.forString(" "));
	}
}
