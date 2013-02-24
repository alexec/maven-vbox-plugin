package com.alexecollins.vbox.core;

import java.io.File;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A simple facade that allows us to determine where to cache files.
 *
 * @author alexec (alex.e.c@gmail.com)
 */
public class Work {
	private final Repo repo = Repo.getInstance();
	private final Context context;

	public Work(Context context) {
		checkNotNull(context, "context");
		this.context = context;
	}

	public File getCacheDir() {
		return repo.getDownloadsDir();
	}

	public File targetOf(VBox box) {
		// this allows us to have other data for the box in here in the future,
		// e.g. storing the source for boxes
		return new File(repo.pathOf(context, box), "target");
	}
}
