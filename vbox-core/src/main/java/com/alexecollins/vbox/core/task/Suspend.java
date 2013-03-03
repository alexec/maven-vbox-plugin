package com.alexecollins.vbox.core.task;

import com.alexecollins.vbox.core.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

/**
 * Suspends a VMs.
 *
 * @since 3.0.0
 */
public class Suspend implements Callable<Void> {
	private static final Logger LOGGER = LoggerFactory.getLogger(Suspend.class);
	private final VBox box;

	public Suspend(VBox box) {
		this.box = box;
	}

	public Void call() throws Exception {
		final String state = box.getProperties().getProperty("VMState");
		if (!state.equals("saved")) {
			LOGGER.info("suspending "+ box.getName());
			box.suspend();
			box.awaitState(15000l, "saved");
			LOGGER.info("suspended "+ box.getName());
		} else {
			LOGGER.info("not suspending " + box.getName() + ", already suspended");
		}
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}
}