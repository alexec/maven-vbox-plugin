package com.alexecollins.maven.plugins.vbox.util;

import com.alexecollins.maven.plugins.vbox.util.DurationUtils;
import org.junit.Test;

/**
 * @author alex.e.c@gmail.com
 */
public class DurationUtilsTest {
	@Test
	public void testForString() throws Exception {

		assert 0 == DurationUtils.secondsForString("");
		assert 1 == DurationUtils.secondsForString("1 second");
		assert 2 == DurationUtils.secondsForString("2 seconds");
		assert 60 == DurationUtils.secondsForString("1 minute");
		assert 120 == DurationUtils.secondsForString("2 minutes");
		assert 122 == DurationUtils.secondsForString("2 minutes 2 seconds");
	}
}
