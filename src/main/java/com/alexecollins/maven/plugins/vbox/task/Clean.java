package com.alexecollins.maven.plugins.vbox.task;

import com.alexecollins.maven.plugins.vbox.Invokable;
import com.alexecollins.maven.plugins.vbox.VBox;
import com.alexecollins.maven.plugins.vbox.util.ExecUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class Clean implements Invokable {
	private static final Logger LOGGER = LoggerFactory.getLogger(Clean.class);
	private final VBox box;

	public Clean(VBox box) {
		this.box = box;
	}

	public void invoke() throws Exception {
		LOGGER.info("cleaning '" + box.getName() + "'");

		if (ExecUtils.exec("vboxmanage", "list", "vms").contains("\"" + box.getName() + "\"")) {
			if (box.getProperties().getProperty("VMState").equals("running")) {
				box.powerOff();
				box.awaitState(10000l, "poweroff");
			}

			box.unregister();
		}

		final Matcher m = Pattern.compile("Location:[ \t]*(.*)\n").matcher(ExecUtils.exec("vboxmanage", "list", "hdds"));
		final List<String> disks = new ArrayList<String>();
		while (m.find()) {
			if (m.group().contains(box.getTarget().getPath())) {
				disks.add(m.group(1).trim());
			}
		}

		Collections.reverse(disks);

		for (String disk : disks) {
			LOGGER.info("closing " + disk);
			ExecUtils.exec("vboxmanage", "closemedium", "disk", disk);
		}


		FileUtils.deleteDirectory(box.getTarget());
	}
}