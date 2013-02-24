package com.alexecollins.vbox.core.task;

import com.alexecollins.vbox.core.TestContext;
import com.alexecollins.vbox.core.VBox;
import com.alexecollins.vbox.core.Work;
import com.google.common.io.Files;
import org.junit.Test;

import java.io.File;

import static junit.framework.Assert.assertFalse;
import static junit.framework.TestCase.assertTrue;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class DeleteDefinitionTest {
	@Test
	public void testCall() throws Exception {
		final File tmp = Files.createTempDir();
		final File target = new File(tmp, "test");
		final VBox box = new CreateDefinition(new TestContext(), "CentOS_6_3", target).call();

		System.out.println("src="+box.getSrc());

		assertTrue(target.exists());
		new DeleteDefinition(new Work(new TestContext()), box).call();
		assertFalse(target.exists());
	}
}
