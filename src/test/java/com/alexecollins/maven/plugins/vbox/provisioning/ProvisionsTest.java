package com.alexecollins.maven.plugins.vbox.provisioning;

import com.alexecollins.maven.plugins.vbox.AbstractTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import javax.xml.bind.JAXB;

/**
 * @author alex.collins
 */
@RunWith(Parameterized.class)
public class ProvisionsTest extends AbstractTest {
	public ProvisionsTest(final String name) {
		super(name);
	}

	@Test
	public void testGetPortForwardOrKeyboardPutScanCodesOrSleep() throws Exception {
		final Provisioning sut = JAXB.unmarshal(Provisioning.class.getResource("/" + name + "/Provisioning.xml"), Provisioning.class);

		assert sut.getTarget().size() > 0;
		for (Provisioning.Target target : sut.getTarget()) {
			assert target.getName() != null;
		}

	}
}
