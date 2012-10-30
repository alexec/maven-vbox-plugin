package com.alexecollins.maven.plugins.vbox.virtualbox;

import org.junit.Test;

import javax.xml.bind.JAXB;

/**
 * @author alex.collins
 */
public class VirtualBoxTest {
	@Test
	public void testGetMachine() throws Exception {
		final VirtualBox sut = JAXB.unmarshal(VirtualBox.class.getResource("/UbuntuServer_12_10/VirtualBox.xml"), VirtualBox.class);

		assert sut.getMachine() != null;

	}
}
