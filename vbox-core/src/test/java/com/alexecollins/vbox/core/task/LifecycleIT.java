package com.alexecollins.vbox.core.task;

import com.alexecollins.vbox.core.patch.ArchPatch;
import org.junit.After;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class LifecycleIT extends AbstractTest {
	public LifecycleIT(String name) throws IOException, JAXBException, SAXException, URISyntaxException {
		super(name);
	}

	@After
	public void tearDown() throws Exception {
		new Clean(work,box).call();
	}

	@Test
	public void test() throws Exception {
		Clean clean = new Clean(work, box);
		clean.call();
		clean.call(); // idempotent
		assertFalse("vbox exists", target.exists());

		new ArchPatch().apply(box);

		Create create = new Create(work, newVBox());
		create.call();
		assertTrue("vbox exists", target.exists());
		create.call(); // snapshot
		assertTrue("vbox exists", target.exists());

		new Provision(work, box, Collections.<String>singleton("*")).call();

		new Start(box).call();
		new Stop(box).call();
	}
}
