package com.alexecollins.vbox.core.task;

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
public class CleanTest extends AbstractTest {

	private Clean sut;
	private VBox box;

	public CleanTest(final String name) {
		super(name);
	}

	@Before
	public void setUp() throws Exception {
		box = new VBox(src.toURI());
		sut = new Clean(work,box);
	}

	@Test
	public void test() throws Exception {
		sut.invoke();
		assertFalse("vbox exists", target.exists());
		new Create(work,box).invoke();
		sut.invoke();
		assertFalse("vbox exists", target.exists());
		sut.invoke();
	}
}
