package com.alexecollins.vbox.core.task;

import com.alexecollins.vbox.core.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class Stop implements Callable<Void> {
	private static final Logger LOGGER = LoggerFactory.getLogger(Stop.class);
	private final VBox box;

	public Stop(VBox box) {
		this.box = box;
	}

	public Void call() throws Exception {
		box.stop();
		return null;
	}
}
