package com.alexecollins.vbox.core.task;

import com.alexecollins.util.PatchUtils;

import java.io.File;
import java.util.concurrent.Callable;

/**
 * Apply a patch to some file.
 *
 * @author alexec (alex.e.c@gmail.com)
 */
public class ApplyPatch implements Callable<Void> {
	private final File a, b, patch;

	public ApplyPatch(File a, File b, File patch) {
		this.a = a;
		this.b = b;
		this.patch = patch;
	}

	public Void call() throws Exception {
		PatchUtils.apply(a, b,patch);
		return null;
	}
}
