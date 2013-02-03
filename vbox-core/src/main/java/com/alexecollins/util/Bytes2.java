package com.alexecollins.util;

import com.google.common.primitives.Bytes;

import java.util.Arrays;

/**
 * @author alexec (alex.e.c@gmail.com)
 * @since 2.0.0
 */
public class Bytes2 {
	private Bytes2() {}

	/**
	 * @return A copy of the src array with each of the values replaced.
	 */
	public static byte[] searchAndReplace(final byte[] src, final byte[] search, final byte[] replace) {
		int i;
		byte x[] = Arrays.copyOf(src, src.length);
		while ((i = Bytes.indexOf(x, search)) >= 0) {
			byte[] y = new byte[x.length - search.length + replace.length];

			/*
			   i=3,w=3
				123...789
				123....89
			 */
			// System.out.println(new String(x));

			System.arraycopy(x, 0, y, 0, i);
			System.arraycopy(replace, 0, y, i, replace.length);
			System.arraycopy(x, i + search.length, y, i + replace.length, x.length - search.length - i);
			x = y;
		}
		return x;
	}
}
