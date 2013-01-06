package com.alexecollins.util;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * @author alex.e.c@gmail.com
 */
public class DurationUtilsTest {
	@Test
	public void testForString() throws Exception {

		assert 0 == DurationUtils.millisForString("");
		assert 1000 == DurationUtils.millisForString("1 second");
		assert 2000 == DurationUtils.millisForString("2 seconds");
		assert 60000 == DurationUtils.millisForString("1 minute");
		assert 120000 == DurationUtils.millisForString("2 minutes");
		assert 122000 == DurationUtils.millisForString("2 minutes 2 seconds");
	}


	@Test
	public void testPrettyDuration() throws Exception {
		assertEquals("1 minute(s)", DurationUtils.prettyPrint(60000));
		assertEquals("30 second(s)", DurationUtils.prettyPrint(30000));

	}
}
