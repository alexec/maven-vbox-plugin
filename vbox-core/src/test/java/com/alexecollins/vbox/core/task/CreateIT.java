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
public class CreateIT extends AbstractTest {

	private Create sut;

	public CreateIT(final String name) {
		super(name);
	}

	@Before
	public void setUp() throws Exception {
		new Clean(work,box).call();
		sut = new Create(work, box);
	}

	@After
	public void tearDown() throws Exception {
		// TODO new Clean().execute(A);
	}

	@Test
	public void test() throws Exception {
		sut.call();
		sut.call(); // snapshot
	}
}
