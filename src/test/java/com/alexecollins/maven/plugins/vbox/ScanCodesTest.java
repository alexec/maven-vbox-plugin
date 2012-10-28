package com.alexecollins.maven.plugins.vbox;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class ScanCodesTest {
	@Test
	public void testGetScanCode() throws Exception {

		assertArrayEquals(new int[]{0x01, 0x81}, ScanCodes.forKey("Esc"));
		assertArrayEquals(new int[]{0x02, 0x82}, ScanCodes.forKey("1"));
		assertArrayEquals(new int[]{0x2a, 0x02, 0x82, 0xaa}, ScanCodes.forKey("!"));
		assertArrayEquals(new int[]{0x1c, 0x9c}, ScanCodes.forKey("Enter"));
		assertArrayEquals(new int[]{0x2a, 0x27, 0xa7, 0xaa}, ScanCodes.forKey(":"));
		assertArrayEquals(new int[]{0x1e, 0x9e}, ScanCodes.forKey("a"));
		assertArrayEquals(new int[]{0x2a, 0x1e, 0x9e, 0xaa}, ScanCodes.forKey("A"));
		assertArrayEquals(new int[]{0x31, 0xb1}, ScanCodes.forKey("n"));
		assertArrayEquals(new int[]{0x26, 0xa6}, ScanCodes.forKey("l"));
		assertArrayEquals(new int[]{0x35, 0xb5}, ScanCodes.forKey("/"));
		assertArrayEquals(new int[]{0x22, 0xa2}, ScanCodes.forKey("g"));
		assertArrayEquals(new int[]{57, 185}, ScanCodes.forKey(" "));
		assertArrayEquals(new int[]{13, 141}, ScanCodes.forKey("="));
	}

	@Test
	public void testText() {
		assertArrayEquals(new int[]{0x02, 0x82, 0x2a, 0x02, 0x82, 0xaa}, ScanCodes.forString("1!"));
		assertArrayEquals(new int[]{0x39, 0xb9}, ScanCodes.forString(" "));
	}
}
