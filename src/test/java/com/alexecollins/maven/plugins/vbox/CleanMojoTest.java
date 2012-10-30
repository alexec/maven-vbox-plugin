package com.alexecollins.maven.plugins.vbox;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;

import static org.junit.Assert.assertFalse;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
@RunWith(Parameterized.class)
public class CleanMojoTest extends AbstractTest {

	private CleanMojo sut;

	public CleanMojoTest(final String name) {
		super(name);
	}

	@Before
	public void setUp() throws Exception {
		sut = new CleanMojo();
	}

	@Test
	public void test() throws Exception {
		sut.execute(src.toURI());
		assertFalse("vbox exists", new File(target, name + ".vbox").exists());
		new CreateMojo().execute(src.toURI());
		sut.execute(src.toURI());
		assertFalse("vbox exists", new File(target, name + ".vbox").exists());
		sut.execute(src.toURI());
	}
}
