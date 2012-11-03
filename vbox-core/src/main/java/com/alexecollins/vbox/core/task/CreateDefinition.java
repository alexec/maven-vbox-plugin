package com.alexecollins.vbox.core.task;

import com.alexecollins.util.Invokable;
import com.alexecollins.vbox.core.VBox;
import com.google.common.collect.ImmutableSet;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class CreateDefinition implements Invokable {
	private static final Logger LOGGER = LoggerFactory.getLogger(CreateDefinition.class);
	private final VBox box;

	public CreateDefinition(VBox box) {
		this.box = box;
	}

	public void invoke() throws Exception {

		final File out = new File("src/main/vbox/" + box.getName());
		if (!out.exists() && !out.mkdirs())
			throw new IllegalStateException(box.getName() + " does not exit and cannot create " + out);

		for (String f : ImmutableSet.<String>builder()
				.addAll(Arrays.asList("MediaRegistry.xml", "VirtualBox.xml", "Manifest.xml", "Provisioning.xml"))
				.addAll(box.getManifest().getFile()).build()) {
			FileUtils.copyURLToFile(getClass().getResource("/" + box.getName() + "/" + f), new File(out, f));
		}

		LOGGER.info("created " + out);
	}
}
