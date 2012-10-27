package com.alexecollins.maven.plugins.vbox;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URI;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class CleanMojoTest {


	public static final URI A = new File("target/vbox/UbuntuServer").toURI();
	public static final File B = new File("src/test/vbox/UbuntuServer");

	@Before
	public void setUp() throws Exception {
		new CreateMojo().execute(B.toURI());
	}

	@Test
	public void test() throws Exception {
		final File f = new File(A);

		new CleanMojo().execute(f.toURI());

		assert !f.exists();
	}
}
