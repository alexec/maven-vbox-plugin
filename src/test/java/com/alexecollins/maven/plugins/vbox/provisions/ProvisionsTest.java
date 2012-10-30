package com.alexecollins.maven.plugins.vbox.provisions;

import org.junit.Test;

import javax.xml.bind.JAXB;

/**
 * @author alex.collins
 */
public class ProvisionsTest {
	@Test
	public void testGetPortForwardOrKeyboardPutScanCodesOrSleep() throws Exception {
		final Provisions sut = JAXB.unmarshal(Provisions.class.getResource("/UbuntuServer_12_10/Provisions.xml"), Provisions.class);

		assert sut.getPortForwardOrKeyboardPutScanCodesOrSleep() != null;
	}
}
