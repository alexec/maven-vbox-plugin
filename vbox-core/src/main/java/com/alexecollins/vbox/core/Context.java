package com.alexecollins.vbox.core;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A context for a box to be created in, usually project coordinates.
 *
 * @author alexec (alex.e.c@gmail.com)
 * @since 3.0.0
 */
public class Context {
	private final String name;

	public Context(String name) {
		checkNotNull(name, "name");
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
