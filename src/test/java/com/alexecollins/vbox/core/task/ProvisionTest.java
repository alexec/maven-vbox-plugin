package com.alexecollins.vbox.core.task;

import com.alexecollins.vbox.core.VBox;
import com.alexecollins.vbox.maven.AbstractTest;
import com.alexecollins.vbox.maven.ProvisionMojo;
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
		new ProvisionMojo(); // TODO logging
		sut = new Provision(new VBox(src.toURI()), Collections.<String>singleton("*"));
	}

	@After
	public void tearDown() throws Exception {
		sut.stopServer();
	}

	@Test
	public void testServer() throws Exception {
		sut.startServer();
		// TODO Thread.sleep(60000);
		sut.stopServer();
	}
}
