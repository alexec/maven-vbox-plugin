package com.alexecollins.vbox.core.task;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collections;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
@RunWith(Parameterized.class)
public class ProvisionTest extends AbstractTest {

	private Provision sut;

	public ProvisionTest(final String name) {
		super(name);
	}

	@Before
	public  void setUp() throws Exception {
		sut = new Provision(work, box, Collections.<String>singleton("*"));
	}

	@After
	public void tearDown() throws Exception {
		sut.stopServer();

		// TODO new CleanMojo().execute(A);
	}

	@Test
	public void test() throws Exception {
		new Clean(work,box).invoke();
		sut.invoke();
	}

	@Test
	public void testServer() throws Exception {
		sut.startServer();
		// TODO Thread.sleep(60000);
		sut.stopServer();
	}
}
