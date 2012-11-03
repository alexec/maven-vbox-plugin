package com.alexecollins.vbox.core.task;

import com.alexecollins.vbox.core.VBox;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
@RunWith(Parameterized.class)
public class CreateTest extends AbstractTest {

	private Create sut;
	private final VBox box = new VBox(src.toURI());

	public CreateTest(final String name) {
		super(name);
	}

	@Before
	public void setUp() throws Exception {
		new Clean(work,box).invoke();
		sut = new Create(work, box);
	}

	@After
	public void tearDown() throws Exception {
		// TODO new Clean().execute(A);
	}

	@Test
	public void test() throws Exception {
		sut.invoke();
		sut.invoke(); // snapshot
	}
}
