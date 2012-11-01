package de.innotek.virtualbox_settings;

import com.alexecollins.maven.plugins.vbox.AbstractTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import javax.xml.bind.JAXB;

/**
 * @author alex.e.c@gmail.com
 */
@RunWith(Parameterized.class)
public class VirtualBoxTest extends AbstractTest {
	public VirtualBoxTest(final String name) {
		super(name);
	}

	@Test
	public void testGetMachine() throws Exception {
		final VirtualBox sut = JAXB.unmarshal(VirtualBox.class.getResource("/" + name + "/VirtualBox.xml"), VirtualBox.class);

		assert sut.getMachine() != null;

	}
}