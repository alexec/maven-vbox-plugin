package com.alexecollins.vbox.core;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A repository for storing VMs in.
 *
 * @author alexec (alex.e.c@gmail.com)
 * @since 3.0.0
 */
public class Repo {
	private static Repo INSTANCE;

	private final File path;

	private Repo(File path) throws IOException {
		checkNotNull(path, "path");
		this.path = path;
		if (!path.exists() && !path.mkdirs()) {throw new IllegalStateException("failed to create " + path);}
		if (!getCacheDir().exists()) {
			FileUtils.forceMkdir(getCacheDir());
		}
		if (!getContextsDir().exists()) {
			FileUtils.forceMkdir(getContextsDir());
		}
	}

	public static Repo getInstance() {
		if (INSTANCE == null) {
			try {
				INSTANCE = new Repo(new File(System.getProperty("user.home"), ".vbox"));
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}
		}
		return INSTANCE;
	}

	public File getPath() {
		return path;
	}

	public File pathOf(Context context, VBox box) {
		return new File(getContextsDir(), context.getName().replaceAll("[^a-zA-Z]", "_") + "/"+ box.getName().replaceAll("/", "_"));
	}

	public File getCacheDir() {
		return new File(getPath(), "cache");
	}

	private File getContextsDir() {
		return new File(path, "contexts");
	}
}
