package com.alexecollins.vbox.core.task;

import com.alexecollins.vbox.core.VBox;
import com.google.common.collect.ImmutableSet;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;
import java.util.concurrent.Callable;

/**
 * Create a definition from a named template.
 *
 * @author alexec (alex.e.c@gmail.com)
 */
public class CreateDefinition implements Callable<Void> {
	private static final Logger LOGGER = LoggerFactory.getLogger(CreateDefinition.class);
	/** The template to use. */
	private final String templateName;
	/** Where to create the definition. */
	private final File target;

	public CreateDefinition(String templateName, File target) {
		this.templateName = templateName;
		this.target = target;
	}

	public Void call() throws Exception {

		if (!target.exists() && !target.mkdirs())
			throw new IllegalStateException(target + " does not exit and cannot create");

		final VBox box = new VBox(getClass().getResource("/" + templateName).toURI());

		for (String f : ImmutableSet.<String>builder()
				.addAll(Arrays.asList("MediaRegistry.xml", "VirtualBox.xml", "Manifest.xml", "Provisioning.xml", "Profile.xml"))
				.addAll(box.getManifest().getFile()).build()) {
			FileUtils.copyURLToFile(getClass().getResource("/" + templateName + "/" + f), new File(target, f));
		}

		LOGGER.info("created " + target);
		return null;
	}
}
