package com.alexecollins.vbox.core.task;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
@RunWith(Parameterized.class)
public class StopIT extends AbstractTest {

	public StopIT(final String name) throws JAXBException, IOException, SAXException, URISyntaxException {
		super(name);
	}

	@Test
	public void testInvoke() throws Exception {
	 	new Stop(box).call();
	}
}
