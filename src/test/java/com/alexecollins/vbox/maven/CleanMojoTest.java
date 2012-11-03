package com.alexecollins.vbox.maven;

import com.alexecollins.vbox.core.VBox;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

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
		sut.execute(new VBox(src.toURI()));
		assertFalse("vbox exists", target.exists());
		new CreateMojo().execute(new VBox(src.toURI()));
		sut.execute(new VBox(src.toURI()));
		assertFalse("vbox exists", target.exists());
		sut.execute(new VBox(src.toURI()));
	}
}
