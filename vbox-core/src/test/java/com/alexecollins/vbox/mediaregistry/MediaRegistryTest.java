package com.alexecollins.vbox.mediaregistry;


import com.alexecollins.vbox.core.task.AbstractTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static junit.framework.Assert.assertTrue;

/**
 * @author alex.e.c@gmail.com
 */
@RunWith(Parameterized.class)
public class MediaRegistryTest extends AbstractTest {

	public MediaRegistryTest(final String name) throws Exception {
		super(name);
	}

	@Test
	public void testName() throws Exception {
		final MediaRegistry sut = getBox().getMediaRegistry();

		assertTrue(sut.getDVDImages().getDVDImage() != null);
	}
}
