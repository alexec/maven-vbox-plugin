package com.alexecollins.vbox.core.task;

import com.alexecollins.vbox.core.VBox;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collections;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
@RunWith(Parameterized.class)
public class ProvisionTest extends AbstractTest {

	private Provision sut;
	private final VBox vBox = new VBox(src.toURI());

	public ProvisionTest(final String name) {
		super(name);
	}

	@Before
	public  void setUp() throws Exception {
		sut = new Provision(vBox, Collections.<String>singleton("*"));
	}

	@After
	public void tearDown() throws Exception {
		sut.stopServer();

		// TODO new CleanMojo().execute(A);
	}

	@Test
	public void test() throws Exception {
		new Clean(vBox).invoke();
		new Create(vBox).invoke();
		sut.invoke();
	}

	@Test
	public void testServer() throws Exception {
		sut.startServer();
		// TODO Thread.sleep(60000);
		sut.stopServer();
	}
}
