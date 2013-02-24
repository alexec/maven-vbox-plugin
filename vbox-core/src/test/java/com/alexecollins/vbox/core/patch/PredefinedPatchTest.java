package com.alexecollins.vbox.core.patch;

import com.alexecollins.vbox.core.Context;
import com.alexecollins.vbox.core.TestContext;
import com.alexecollins.vbox.core.VBox;
import com.alexecollins.vbox.core.task.CreateDefinition;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.Files;
import org.apache.commons.io.FileUtils;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.util.Collections;

import static junit.framework.Assert.assertTrue;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class PredefinedPatchTest {
	@Ignore // TODO - meant to test properties, but this no longer needs them
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidPatch() throws Exception {
		new PredefinedPatch("CentOS_6_3--tomcat6", Collections.<String, String>emptyMap());
	}

	@Test
	public void testApply() throws Exception {
		final File tmp = Files.createTempDir();
		final Context context = new TestContext();
		new CreateDefinition(context, "CentOS_6_3", tmp).call();
		new PredefinedPatch("CentOS_6_3--tomcat6", ImmutableMap.of("hostname", "localhost")).apply(new VBox(context, tmp.toURI()));
		assertTrue(FileUtils.readFileToString(new File(tmp, "floppy0/post-install.sh")).contains("tomcat6"));
	}

	@Test
	public void testList() throws Exception {
		assertTrue(PredefinedPatch.list().size() > 0);
	}
}
