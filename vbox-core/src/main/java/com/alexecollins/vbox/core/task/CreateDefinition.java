package com.alexecollins.vbox.core.task;

import com.alexecollins.vbox.core.Context;
import com.alexecollins.vbox.core.VBox;
import com.google.common.collect.ImmutableSet;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.concurrent.Callable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Create a definition from a named template.
 *
 * @author alexec (alex.e.c@gmail.com)
 */
public class CreateDefinition implements Callable<VBox> {
	private static final Logger LOGGER = LoggerFactory.getLogger(CreateDefinition.class);
	/** The template to use. */
	private final String templateName;
	/** Where to create the definition. */
	private final File target;
	private final URL resource;
	private final Context context;

	public CreateDefinition(Context context, String templateName, File target) {
		this.context = context;
		checkNotNull(templateName, "templateName");
		checkNotNull(target, "target");
		this.templateName = templateName;
		this.target = target;
		resource = getClass().getResource("/" + templateName);
		if (resource == null) {
			throw new IllegalArgumentException("cannot find template " + templateName);
		}
	}

	public VBox call() throws Exception {

		if (!target.exists() && !target.mkdirs())
			throw new IllegalStateException(target + " does not exit and cannot create");

		final VBox box = new VBox(context, resource.toURI());

		for (String f : ImmutableSet.<String>builder()
				.addAll(Arrays.asList("MediaRegistry.xml", "VirtualBox.xml", "Manifest.xml", "Provisioning.xml", "Profile.xml"))
				.addAll(box.getManifest().getFile()).build()) {
			FileUtils.copyURLToFile(getClass().getResource("/" + templateName + "/" + f), new File(target, f));
		}

		LOGGER.info("created " + target);
		return new VBox(context, target.toURI());
	}
}
