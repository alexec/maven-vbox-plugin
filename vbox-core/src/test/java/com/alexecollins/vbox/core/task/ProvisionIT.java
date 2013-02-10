package com.alexecollins.vbox.core.task;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
@RunWith(Parameterized.class)
public class ProvisionIT extends AbstractTest {

	private Provision sut;

	public ProvisionIT(final String name) throws JAXBException, IOException, SAXException, URISyntaxException {
		super(name);
	}

	@Before
	public  void setUp() throws Exception {
		sut = new Provision(work, box, Collections.<String>singleton("*"));
	}

	@After
	public void tearDown() throws Exception {
		sut.stopServer();
	}

	@Test
	public void test() throws Exception {
		new Clean(work,box).call();
		sut.call();
	}

	@Test
	public void testServer() throws Exception {
		sut.startServer();
		// TODO Thread.sleep(60000);
		sut.stopServer();
	}
}
