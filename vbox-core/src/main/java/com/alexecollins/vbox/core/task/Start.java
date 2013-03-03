package com.alexecollins.vbox.core.task;

import com.alexecollins.vbox.core.VBox;
import com.alexecollins.vbox.profile.Profile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * Start a box and await it's start-up to complete.
 *
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
		startAndWait();
		LOGGER.info("started " + box.getName());
		return null;
	}

	protected void startAndWait() throws IOException, InterruptedException, ExecutionException, TimeoutException, URISyntaxException {
		box.start();
		box.awaitState(30000l, "running");

		for (Profile.Ping p : box.getProfile().getPing()) {
			final URI u = new URI(p.getUrl());
			Provision.awaitPort(u.getHost(), u.getPort(), p.getTimeout());
		}
	}
}
