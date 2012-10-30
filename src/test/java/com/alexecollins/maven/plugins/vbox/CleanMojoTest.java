package com.alexecollins.maven.plugins.vbox;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class CleanMojoTest {

	public static final File A = new File("target/vbox/boxes/UbuntuServer_12_10");
	public static final File B = new File("src/main/vbox/UbuntuServer_12_10");
	private CleanMojo sut;

	@Before
	public void setUp() throws Exception {
		sut = new CleanMojo();
		new CreateMojo().execute(B.toURI());
	}

	@Test
	public void test() throws Exception {
		sut.execute(B.toURI());
		assert !new File(A, "UbuntuServer_12_10.vbox").exists();
		sut.execute(B.toURI());
	}
}
