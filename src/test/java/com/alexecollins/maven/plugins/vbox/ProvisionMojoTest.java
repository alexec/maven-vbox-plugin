package com.alexecollins.maven.plugins.vbox;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class ProvisionMojoTest {

	private URI A;
	private ProvisionMojo sut;

	@Before
	public void setUp() throws Exception {
		A = getClass().getResource("/UbuntuServer").toURI();
		assert A != null;
		new CleanMojo().execute(A);
		new CreateMojo().execute(A);
		sut = new ProvisionMojo();
	}

	@After
	public void tearDown() throws Exception {
		sut.stopServer();
		new CleanMojo().execute(A);
	}

	@Test
	public void test() throws Exception {
		sut.execute(A);

		System.out.println("sleeping 60s");
		Thread.sleep(60000);
	}

	@Test
	public void testServer() throws Exception {
		sut.startServer(A);
		// Thread.sleep(10000);
	}
}
