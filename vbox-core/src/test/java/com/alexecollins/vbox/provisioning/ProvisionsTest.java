package com.alexecollins.vbox.provisioning;

import com.alexecollins.vbox.core.VBox;
import com.alexecollins.vbox.core.task.AbstractTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * @author alex.e.c@gmail.com
 */
@RunWith(Parameterized.class)
public class ProvisionsTest extends AbstractTest {
	public ProvisionsTest(final String name) {
		super(name);
	}

	@Test
	public void testGetPortForwardOrKeyboardPutScanCodesOrSleep() throws Exception {
		final Provisioning sut = new VBox(src.toURI()).getProvisioning();

		assert sut.getTarget().size() > 0;
		for (Provisioning.Target target : sut.getTarget()) {
			assert target.getName() != null;
		}

	}
}
