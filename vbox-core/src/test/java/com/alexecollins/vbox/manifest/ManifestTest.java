package com.alexecollins.vbox.manifest;

import com.alexecollins.vbox.core.task.AbstractTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static junit.framework.Assert.assertTrue;

/**
 * @author alex.e.c@gmail.com
 */
@RunWith(Parameterized.class)
public class ManifestTest extends AbstractTest {
	public ManifestTest(final String name) throws Exception {
		super(name);
	}

	@Test
	public void testGetFile() throws Exception {

		final Manifest sut = getBox().getManifest();

		assertTrue(sut.getFile().size() >= 0);
	}
}
