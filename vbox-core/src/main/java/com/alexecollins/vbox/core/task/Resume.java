package com.alexecollins.vbox.core.task;

import com.alexecollins.vbox.core.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Resumes a box.
 *
 * @since 3.0.0
 */
public class Resume extends Start {
	private static final Logger LOGGER = LoggerFactory.getLogger(Resume.class);
	private final VBox box;

	public Resume(VBox box) {
		super(box);
		this.box = box;
	}

	@Override
	public Void call() throws Exception {
		LOGGER.info("resuming " + box.getName());
		startAndWait();
		LOGGER.info("resumed " + box.getName());
		return null;
	}
}
