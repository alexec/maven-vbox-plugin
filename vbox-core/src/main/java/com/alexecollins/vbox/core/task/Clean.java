package com.alexecollins.vbox.core.task;

import com.alexecollins.util.ExecUtils;
import com.alexecollins.vbox.core.VBox;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class Clean extends AbstractTask {
	private static final Logger LOGGER = LoggerFactory.getLogger(Clean.class);

	public Clean(File work, VBox box) {
		super(work, box);
	}

	public Void call() throws Exception {

		LOGGER.info("cleaning '" + box.getName() + "'");

		box.unregister();

		final Matcher m = Pattern.compile("Location:[ \t]*(.*)\n").matcher(ExecUtils.exec("vboxmanage", "list", "hdds"));
		final List<String> disks = new ArrayList<String>();
		while (m.find()) {
			if (m.group().contains(getTarget().getPath())) {
				disks.add(m.group(1).trim());
			}
		}

		Collections.reverse(disks);

		for (String disk : disks) {
			LOGGER.info("closing " + disk);
			ExecUtils.exec("vboxmanage", "closemedium", "disk", disk);
		}


		LOGGER.debug("deleting " + getTarget());
		FileUtils.deleteDirectory(getTarget());
		return null;
	}
}