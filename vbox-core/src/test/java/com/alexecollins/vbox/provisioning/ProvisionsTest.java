package com.alexecollins.vbox.provisioning;

import com.alexecollins.vbox.core.task.AbstractTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static junit.framework.Assert.assertTrue;

/**
 * @author alex.e.c@gmail.com
 */
@RunWith(Parameterized.class)
public class ProvisionsTest extends AbstractTest {
	public ProvisionsTest(final String name) throws Exception {
		super(name);
	}

	@Test
	public void testGetPortForwardOrKeyboardPutScanCodesOrSleep() throws Exception {
		final Provisioning sut = getBox().getProvisioning();

		assertTrue(sut.getTarget().size() > 0);
		for (Provisioning.Target target : sut.getTarget()) {
			assert target.getName() != null;
		}

	}
}
