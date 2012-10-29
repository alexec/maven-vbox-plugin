package com.alexecollins.maven.plugins.vbox.schema;

import org.junit.Test;

import javax.xml.bind.JAXB;

/**
 * @author alex.collins
 */
public class ManifestTest {
	@Test
	public void testGetFile() throws Exception {

		final Manifest sut = JAXB.unmarshal(Manifest.class.getResource("/UbuntuServer_12_10/VirtualBox.xml"), Manifest.class);

		assert sut.getFile().size() >= 0;
	}
}
