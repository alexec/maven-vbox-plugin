package com.alexecollins.vbox.provisioning;

import com.alexecollins.vbox.core.VBox;
import com.alexecollins.vbox.core.task.AbstractTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.net.URISyntaxException;

import static junit.framework.Assert.assertTrue;

/**
 * @author alex.e.c@gmail.com
 */
@RunWith(Parameterized.class)
public class ProvisionsTest extends AbstractTest {
	public ProvisionsTest(final String name) throws JAXBException, IOException, SAXException, URISyntaxException {
		super(name);
	}

	@Test
	public void testGetPortForwardOrKeyboardPutScanCodesOrSleep() throws Exception {
		final Provisioning sut = new VBox(src.toURI()).getProvisioning();

		assertTrue(sut.getTarget().size() > 0);
		for (Provisioning.Target target : sut.getTarget()) {
			assert target.getName() != null;
		}

	}
}
