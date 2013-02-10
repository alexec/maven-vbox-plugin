package de.innotek.virtualbox_settings;

import com.alexecollins.vbox.core.task.AbstractTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * @author alex.e.c@gmail.com
 */
@RunWith(Parameterized.class)
public class VirtualBoxTest extends AbstractTest {
	public VirtualBoxTest(final String name) throws Exception {
		super(name);
	}

	@Test
	public void testGetMachine() throws Exception {
		final VirtualBox sut = getBox().getVirtualBox();

		assert sut.getMachine() != null;

	}
}
