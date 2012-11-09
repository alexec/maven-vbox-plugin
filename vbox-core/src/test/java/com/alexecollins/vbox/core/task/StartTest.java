package com.alexecollins.vbox.core.task;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
@RunWith(Parameterized.class)
public class StartTest extends AbstractTest {


	public StartTest(final String name) {
		super(name);
	}

	@Before
	public void setUp() throws Exception {
		new Stop(box).call();
	}
	@After public void tearDown() throws Exception {
		Thread.sleep(100);
		new Stop(box).call();
	}

	@Test
	public void testInvoke() throws Exception {
	 	new Start(box).call();
	}
}
