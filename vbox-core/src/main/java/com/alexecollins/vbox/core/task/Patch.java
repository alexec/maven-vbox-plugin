package com.alexecollins.vbox.core.task;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Apply multiple patches.
 *
 * @author alexec (alex.e.c@gmail.com)
 * @since 2.0.0
 */
public class Patch implements Callable<Void> {
	private final List<Patch> patches;

	public Patch(List<Patch> patches) {
		if (patches == null) {throw new IllegalArgumentException();}
		this.patches = patches;
	}

	public Void call() throws Exception {
		for (Patch patch : patches) {
			patch.call();
		}

		return null;
	}
}
