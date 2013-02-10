package com.alexecollins.vbox.profile;

import com.alexecollins.vbox.core.task.AbstractTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static junit.framework.Assert.assertNotNull;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
@RunWith(Parameterized.class)
public class ProfileTest extends AbstractTest {
	public ProfileTest(final String name) throws Exception {
		super(name);
	}

	@Test
	public void testProfile() throws Exception {
		final Profile sut = getBox().getProfile();

		assertNotNull(sut.getType());
	}

}
