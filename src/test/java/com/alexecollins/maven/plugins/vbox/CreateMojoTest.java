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
public class CreateMojoTest extends AbstractTest {

	private CreateMojo sut;

	public CreateMojoTest(final String name) {
		super(name);
	}

	@Before
	public void setUp() throws Exception {
		new CleanMojo().execute(new VBox(src.toURI()));
		sut = new CreateMojo();
	}

	@After
	public void tearDown() throws Exception {
		// TODO new CleanMojo().execute(A);
	}

	@Test
	public void test() throws Exception {
		sut.execute(new VBox(src.toURI()));
		sut.execute(new VBox(src.toURI())); // snapshot
	}
}
