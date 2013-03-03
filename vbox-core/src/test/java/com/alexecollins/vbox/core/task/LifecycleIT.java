package com.alexecollins.vbox.core.task;

import com.alexecollins.vbox.core.patch.ArchPatch;
import org.junit.After;
import org.junit.Test;

import java.util.Collections;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class LifecycleIT extends AbstractTest {

	public LifecycleIT(String name) throws Exception {
		super(name);
	}

	@After
	public void tearDown() throws Exception {
		new Clean(getWork(), getBox()).call();
	}

	@Test
	public void test() throws Exception {

		Clean clean = new Clean(getWork(), getBox());
		clean.call();
		clean.call(); // idempotent
		assertFalse("vbox exists", getTarget().exists());

		new ArchPatch().apply(getBox());

		Create create = new Create(getWork(), getBox());
		create.call();
		assertTrue("vbox exists", getTarget().exists());
		create.call(); // snapshot
		assertTrue("vbox exists", getTarget().exists());

		new Provision(getWork(), getBox(), Collections.<String>singleton("*")).call();

		new Start(getBox()).call();
		new Suspend(getBox()).call();
		new Status(getBox()).call();
		new Resume(getBox()).call();
		new Stop(getBox()).call();
	}

}
