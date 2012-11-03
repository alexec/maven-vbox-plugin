package com.alexecollins.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Calculate timeout values.
 *
 * @author alex.e.c@gmail.com
 */
public class DurationUtils {

	/**
	 * @return The period, in seconds.
	 */
	public static long secondsForString(final String string) {

		final Matcher m = Pattern.compile("([0-9]+) (minute|second)s?").matcher(string);
		long t = 0;

		while (m.find()) {
			t += (m.group(2).equals("minute") ? 60 : 1 ) * Long.parseLong(m.group(1));
		}

		return t;
	}
}
