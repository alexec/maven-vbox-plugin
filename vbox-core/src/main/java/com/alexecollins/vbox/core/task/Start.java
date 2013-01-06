package com.alexecollins.vbox.core.task;

import com.alexecollins.vbox.core.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class Start implements Callable<Void> {
	private static final Logger LOGGER = LoggerFactory.getLogger(Start.class);
	private final VBox box;

    public Start(VBox box) {
		this.box = box;
    }

	public Void call() throws Exception {
		LOGGER.info("starting " + box.getName());
		box.start();
		LOGGER.info("started " + box.getName());
		return null;
	}
}
