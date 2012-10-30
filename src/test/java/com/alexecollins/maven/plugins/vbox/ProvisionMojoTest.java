package com.alexecollins.maven.plugins.vbox;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
@RunWith(Parameterized.class)
public class ProvisionMojoTest extends AbstractTest {

	private ProvisionMojo sut;

	public ProvisionMojoTest(final String name) {
		super(name);
	}

	@Before
	public void setUp() throws Exception {
		sut = new ProvisionMojo();
	}

	@After        // http://192.168.1.75:10350/preseed.cfg
	public void tearDown() throws Exception {
		sut.stopServer();
		// TODO new CleanMojo().execute(A);
	}

	@Test
	public void test() throws Exception {
		new CleanMojo().execute(src.toURI());
		new CreateMojo().execute(src.toURI());
		sut.execute(src.toURI());
	}

	@Test
	public void testServer() throws Exception {
		sut.startServer(src.toURI());
		// TODO Thread.sleep(60000);
		sut.stopServer();
	}
}
