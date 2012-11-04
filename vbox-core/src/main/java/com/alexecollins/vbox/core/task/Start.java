package com.alexecollins.vbox.core.task;

import com.alexecollins.util.ExecUtils;
import com.alexecollins.util.Invokable;
import com.alexecollins.vbox.core.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class Start implements Invokable {
	private static final Logger LOGGER = LoggerFactory.getLogger(Start.class);
	private final VBox box;

	public Start(VBox box) {
		this.box = box;
	}

	public void invoke() throws Exception {
		LOGGER.info("starting " + box.getName());
		ExecUtils.exec("vboxmanage", "startvm", box.getName());
		LOGGER.info("started " + box.getName());
	}
}
