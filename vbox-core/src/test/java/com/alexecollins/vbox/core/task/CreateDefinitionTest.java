package com.alexecollins.vbox.core.task;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
@RunWith(Parameterized.class)
public class CreateDefinitionTest extends AbstractTest {

	public CreateDefinitionTest(final String name) {
		super(name);
	}

	@Test
	public void test() throws Exception {
		final File tmp = new File(System.getProperty("java.io.tmpdir"), "test");
		final CreateDefinition sut = new CreateDefinition(name, tmp);

		FileUtils.deleteDirectory(tmp);
		System.out.println("tmp=" + tmp);
		sut.call();

		assert new File(tmp, "VirtualBox.xml").exists();
	}
}
