package com.alexecollins.vbox.core.task;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.Assert.assertFalse;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
@RunWith(Parameterized.class)
public class CleanIT extends AbstractTest {

	private Clean sut;

	public CleanIT(final String name) throws JAXBException, IOException, SAXException, URISyntaxException {
		super(name);
	}

	@Before
	public void setUp() throws Exception {
		sut = new Clean(work,box);
	}

	@Test
	public void test() throws Exception {
		sut.call();
		assertFalse("vbox exists", target.exists());
		new Create(work,box).call();
		sut.call();
		assertFalse("vbox exists", target.exists());
		sut.call();
	}
}
