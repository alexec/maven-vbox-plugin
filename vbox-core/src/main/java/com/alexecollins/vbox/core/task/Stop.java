package com.alexecollins.vbox.core.task;

import com.alexecollins.vbox.core.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeoutException;

/**
 * Stop the box. Firstly by sending a ACPID message. If that fails, forcibly terminating it.
 *
 * @author alexec (alex.e.c@gmail.com)
 */
public class Stop implements Callable<Void> {
	private static final Logger LOGGER = LoggerFactory.getLogger(Stop.class);
	private final VBox box;

	public Stop(VBox box) {
		this.box = box;
	}

	public Void call() throws Exception {
		if (box.getProperties().getProperty("VMState").equals("running")) {
			LOGGER.info("stopping '" + box.getName() + "'");
			box.pressPowerButton();
			try {
				box.awaitState(30000l, "poweroff");
			} catch (TimeoutException e) {
				LOGGER.warn("failed to power down in 30 second(s) forcing power off");
				box.powerOff();
				box.awaitState(2000, "poweroff");
			}
			LOGGER.info("stopped '" + box.getName() + "'");
		} else {
			LOGGER.info("not stopping '" + box.getName() + "', already off");
		}
		return null;
	}
}
