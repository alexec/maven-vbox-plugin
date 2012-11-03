package com.alexecollins.vbox.core.task;

import com.alexecollins.vbox.core.VBox;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class CreateDefinitionTest extends AbstractTest {

	public CreateDefinitionTest(final String name) {
		super(name);
	}

	@Test
	public void test() throws Exception {
		final File tmp = new File(System.getProperty("java.io.tmpdir"), "test");
		final CreateDefinition sut = new CreateDefinition(new VBox(tmp.toURI()));

		FileUtils.deleteDirectory(tmp);
		System.out.println("tmp=" + tmp);
		sut.invoke();

		assert new File(tmp, "VirtualBox.xml").exists();
		assert new File(tmp, "preseed.cfg").exists();
	}
}
