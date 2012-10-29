package com.alexecollins.maven.plugins.vbox;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class RecreateMojoTest {

	private RecreateMojo sut;
	private URI A;

	@Before
	public void setUp() throws Exception {
		A = getClass().getResource("/UbuntuServer_12_10").toURI();
		assert A != null;
		sut = new RecreateMojo();
		sut.remove(A);
	}

	@After
	public void tearDown() throws Exception {
		sut.remove(A);
	}

	@Test
	public void test() throws Exception {
		sut.execute(A);
	}
}
