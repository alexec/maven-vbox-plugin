package com.alexecollins.vbox.manifest;

import com.alexecollins.vbox.core.task.AbstractTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import javax.xml.bind.JAXB;

/**
 * @author alex.e.c@gmail.com
 */
@RunWith(Parameterized.class)
public class ManifestTest extends AbstractTest {
	public ManifestTest(final String name) {
		super(name);
	}

	@Test
	public void testGetFile() throws Exception {

		final Manifest sut = JAXB.unmarshal(Manifest.class.getResource("/" + name + "/VirtualBox.xml"), Manifest.class);

		assert sut.getFile().size() >= 0;
	}
}
