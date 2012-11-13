package com.alexecollins.vbox.manifest;

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
public class ManifestTest extends AbstractTest {
	public ManifestTest(final String name) throws JAXBException, IOException, SAXException, URISyntaxException {
		super(name);
	}

	@Test
	public void testGetFile() throws Exception {

		final Manifest sut = new VBox(src.toURI()).getManifest();

		assert sut.getFile().size() >= 0;
	}
}
