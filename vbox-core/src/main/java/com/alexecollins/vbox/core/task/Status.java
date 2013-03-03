package com.alexecollins.vbox.core.task;

import com.alexecollins.vbox.core.VBox;
import com.google.common.collect.ImmutableMap;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * Get the status of the machine.
 *
 * A bit of a trivial class, but allows us to print the same output for Maven and Ant.
 *
 * @author alexec (alex.e.c@gmail.com)
 * @since 3.0.0 ¡
 */
public class Status implements Callable<String> {
	private final VBox box;

	public Status(VBox box) {
		this.box = box;
	}

	public String call() throws InterruptedException, ExecutionException, IOException {
		final String state = box.getProperties().getProperty("VMState");
		final ImmutableMap<String, String> aliases = ImmutableMap.of("saved", "suspended");
		return String.format("%-50s %s", box.getName(), aliases.containsKey(state) ? aliases.get(state) : state);
	}
}
