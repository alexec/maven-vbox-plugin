package com.alexecollins.vbox.core.patch;

import com.alexecollins.vbox.core.VBox;
import com.alexecollins.vbox.core.task.CreateDefinition;
import com.google.common.io.Files;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;

import static junit.framework.Assert.assertTrue;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class PredefinedPatchTest {
	@Test
	public void testApply() throws Exception {
		final File tmp = Files.createTempDir();
		new CreateDefinition("CentOS_6_3", tmp).call();
		new PredefinedPatch("CentOS_6_3--tomcat6").apply(new VBox(tmp.toURI()));
		assertTrue(FileUtils.readFileToString(new File(tmp, "floppy0/post-install.sh")).contains("tomcat6"));
	}

	@Test
	public void testList() throws Exception {
		assertTrue(PredefinedPatch.list().size() > 0);
	}
}
