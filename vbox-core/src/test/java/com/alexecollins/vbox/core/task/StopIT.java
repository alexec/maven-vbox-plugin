package com.alexecollins.vbox.core.task;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
@RunWith(Parameterized.class)
public class StopIT extends AbstractTest {

	public StopIT(final String name) {
		super(name);
	}

	@Test
	public void testInvoke() throws Exception {
	 	new Stop(box).call();
	}
}
