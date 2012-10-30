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
		A = getClass().getResource("/UbuntuServer_12_10").toURI();
		assert A != null;
		sut = new ProvisionMojo();
	}

	@After        // http://192.168.1.75:10350/preseed.cfg
	public void tearDown() throws Exception {
		sut.stopServer();
		// TODO new CleanMojo().execute(A);
	}

	@Test
	public void test() throws Exception {
		new CleanMojo().execute(A);
		new CreateMojo().execute(A);
		sut.execute(A);
	}

	@Test
	public void testServer() throws Exception {
		sut.startServer(A);
		Thread.sleep(60000);
		sut.stopServer();
	}
}
