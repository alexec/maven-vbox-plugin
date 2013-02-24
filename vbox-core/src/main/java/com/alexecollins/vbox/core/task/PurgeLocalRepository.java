package com.alexecollins.vbox.core.task;

import com.alexecollins.vbox.core.Repo;
import com.google.common.collect.ImmutableSet;
import org.apache.commons.io.FileUtils;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class PurgeLocalRepository implements Callable<Void> {
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PurgeLocalRepository.class);
	public Void call() throws IOException {
		final long minAge = System.currentTimeMillis() - (30l * 24l * 60l * 60l * 1000l);

		LOGGER.info("Searching for contexts and boxes last modified before " + new Date(minAge));

		final Set<File> files = new HashSet<File>(ImmutableSet.copyOf(Repo.getInstance().getDownloadsDir().listFiles()));

		final ImmutableSet<File> elements = ImmutableSet.copyOf(Repo.getInstance().getContextsDir().listFiles());
		for (File contextDir : elements) {
			files.addAll(ImmutableSet.<File>copyOf(contextDir.listFiles()));
		}

		for (File f : files) {
			if (f.lastModified() < minAge) { // shame Java doesn't give access to last access time
				LOGGER.info("Deleting " + f);
				FileUtils.forceDelete(f);
			}
		}

		return null;
	}
}
