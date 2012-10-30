package com.alexecollins.maven.plugins.vbox.provisions;

import com.alexecollins.maven.plugins.vbox.AbstractTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import javax.xml.bind.JAXB;

/**
 * @author alex.collins
 */
@RunWith(Parameterized.class)
public class ProvisionsTest extends AbstractTest {
	public ProvisionsTest(final String name) {
		super(name);
	}

	@Test
	public void testGetPortForwardOrKeyboardPutScanCodesOrSleep() throws Exception {
		final Provisions sut = JAXB.unmarshal(Provisions.class.getResource("/" + name + "/Provisions.xml"), Provisions.class);

		assert sut.getPortForwardOrKeyboardPutScanCodesOrSleep() != null;
	}
}
