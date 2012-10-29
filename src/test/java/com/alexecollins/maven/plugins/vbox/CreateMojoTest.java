package com.alexecollins.maven.plugins.vbox;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class CreateMojoTest {

	private CreateMojo sut;
	private URI A;

	@Before
	public void setUp() throws Exception {
		A = getClass().getResource("/UbuntuServer_12_10").toURI();
		assert A != null;
		new CleanMojo().execute(A);
		sut = new CreateMojo();
	}

	@After
	public void tearDown() throws Exception {
		//new CleanMojo().execute(A);
	}

	@Test
	public void test() throws Exception {
		sut.execute(A);
		sut.execute(A); // snapshot
	}
}
