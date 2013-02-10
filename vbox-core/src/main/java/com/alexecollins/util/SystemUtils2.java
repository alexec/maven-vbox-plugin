package com.alexecollins.util;

/**
 * @author alexec (alex.e.c@gmail.com)
 * @since 2.0.0
 */
public class SystemUtils2 {
	private SystemUtils2() {}

	/**
	 * @return If the _think_ the underlying OS is 64-bit.
	 */
	public static boolean is64Bit() {
		return System.getProperty("sun.arch.data.model", System.getProperty("os.arch")).contains("64");
	}
}
