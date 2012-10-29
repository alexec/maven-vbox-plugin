package com.alexecollins.maven.plugins.vbox;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class CreateDefinitionMojoTest {

	@Test
	public void test() throws Exception {
		final CreateDefinitionMojo sut = new CreateDefinitionMojo();
		sut.name = "UbuntuServer_12_10";
		final File tmp = new File(System.getProperty("java.io.tmpdir"), "test");

		FileUtils.deleteDirectory(tmp);
		System.out.println("tmp=" + tmp);
		sut.createDefn(tmp);

		assert new File(tmp, "VirtualBox.xml").exists();
		assert new File(tmp, "preseed.cfg").exists();
	}
}
