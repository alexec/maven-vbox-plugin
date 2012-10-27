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
		A = getClass().getResource("/UbuntuServer").toURI();
		assert A != null;
		sut = new CreateMojo();
		tearDown();
	}

	@After
	public void tearDown() throws Exception {
		new CleanMojo().execute(A);
	}

	@Test
	public void testCreate() throws Exception {
		sut.execute(A);
	}
}
