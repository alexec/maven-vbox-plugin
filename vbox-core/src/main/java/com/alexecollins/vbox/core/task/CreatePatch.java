package com.alexecollins.vbox.core.task;

import com.alexecollins.util.PatchUtils;

import java.io.File;
import java.util.concurrent.Callable;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Create a diff from the two files and write it to the third.
 *
 * @author alexec (alex.e.c@gmail.com)
 */
public class CreatePatch implements Callable<Void> {

	private final File a,b,
		/** The output file, should end with .patch  */
		patch;

	public CreatePatch(File a, File b, File patch) {
		checkArgument(a != null && a.exists());
		checkArgument(b != null && b.exists());
		checkNotNull(patch);
		this.a = a;
		this.b = b;
		this.patch = patch;
	}


	public Void call() throws Exception {
		PatchUtils.create(a, b, patch);
		return null;
	}
}
