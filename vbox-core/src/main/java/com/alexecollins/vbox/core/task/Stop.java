package com.alexecollins.vbox.core.task;

import com.alexecollins.util.Invokable;
import com.alexecollins.vbox.core.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeoutException;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class Stop implements Invokable {
	private static final Logger LOGGER = LoggerFactory.getLogger(Stop.class);
	private final VBox box;

	public Stop(VBox box) {
		this.box = box;
	}

	public void invoke() throws Exception {
		LOGGER.info("stopping " + box.getName());
		if (box.getProperties().getProperty("VMState").equals("running")) {
			box.pressPowerButton();
			try {
				box.awaitState(20000l, "poweroff");
			} catch (TimeoutException e) {
				LOGGER.warn("failed to power down in 20 second(s) forcing power off");
				box.powerOff();
			}
			LOGGER.info("stopped " + box.getName());
		} else {
			LOGGER.info("not stopping, already off");
		}
	}
}
