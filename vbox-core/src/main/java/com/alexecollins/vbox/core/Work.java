package com.alexecollins.vbox.core;

import javax.annotation.Nullable;
import java.io.File;

/**
 * A simple facade that allows us to determine where to cache files.
 *
 * @author alexec (alex.e.c@gmail.com)
 */
public class Work {
	private final File baseDir;
	private final File cacheDir;

	public Work(File baseDir, @Nullable File cacheDir) {
		this.baseDir = baseDir;
		this.cacheDir = cacheDir != null ? cacheDir : getDefaultCache(baseDir);

		if (!this.cacheDir.exists() && !this.cacheDir.mkdirs()) {
			throw new IllegalStateException("failed to create " + this.cacheDir);
		}
	}

	public Work(File baseDir) {
		this(baseDir,null);
	}

	public File getBaseDir() {
		return baseDir;
	}

	public File getCacheDir() {
		return cacheDir;
	}

	/**
	 * @param work A working directory.
	 * @return Where to cache files.
	 */
	public static File getDefaultCache(File work) {
		return new File(work, "vbox/downloads");
	}
}
