package com.alexecollins.vbox.mediaregistry;


import com.alexecollins.vbox.core.VBox;
import com.alexecollins.vbox.core.task.AbstractTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author alex.e.c@gmail.com
 */
@RunWith(Parameterized.class)
public class MediaRegistryTest extends AbstractTest {

	public MediaRegistryTest(final String name) throws JAXBException, IOException, SAXException, URISyntaxException {
		super(name);
	}

	@Test
	public void testName() throws Exception {
		final MediaRegistry sut = new VBox(src.toURI()).getMediaRegistry();

		assert sut.getDVDImages().getDVDImage() != null;
	}
}
