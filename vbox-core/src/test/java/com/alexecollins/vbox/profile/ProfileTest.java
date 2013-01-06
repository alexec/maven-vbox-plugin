package com.alexecollins.vbox.profile;

import com.alexecollins.vbox.core.VBox;
import com.alexecollins.vbox.core.task.AbstractTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.net.URISyntaxException;

import static junit.framework.Assert.assertNotNull;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
@RunWith(Parameterized.class)
public class ProfileTest extends AbstractTest {
	public ProfileTest(final String name) throws JAXBException, IOException, SAXException, URISyntaxException {
		super(name);
	}

	@Test
	public void testProfile() throws Exception {
		final Profile sut = new VBox(src.toURI()).getProfile();

		assertNotNull(sut.getType());
	}

}
