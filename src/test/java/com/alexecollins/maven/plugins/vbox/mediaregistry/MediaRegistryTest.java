package com.alexecollins.maven.plugins.vbox.mediaregistry;

import org.junit.Test;

import javax.xml.bind.JAXB;

/**
 * @author alex.collins
 */
public class MediaRegistryTest {

	@Test
	public void testName() throws Exception {
		final MediaRegistry sut = JAXB.unmarshal(MediaRegistry.class.getResource("/UbuntuServer_12_10/MediaRegistry.xml"), MediaRegistry.class);

		assert sut.getDVDImages().getDVDImage() != null;
	}
}
