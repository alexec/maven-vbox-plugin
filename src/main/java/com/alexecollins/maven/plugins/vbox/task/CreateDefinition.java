package com.alexecollins.maven.plugins.vbox.task;

import com.alexecollins.maven.plugins.vbox.Invokable;
import com.alexecollins.maven.plugins.vbox.VBox;
import com.google.common.collect.ImmutableSet;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Arrays;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class CreateDefinition implements Invokable {
	private final VBox box;

	public CreateDefinition(VBox box) {
		this.box = box;
	}

	public void invoke() throws Exception {

		if (!box.exists() && !new File(box.getSrc().toURL().getFile()).mkdirs())
			throw new IllegalStateException();

		for (String f : ImmutableSet.<String>builder()
				.addAll(Arrays.asList("MediaRegistry.xml", "VirtualBox.xml", "Manifest.xml", "Provisioning.xml"))
				.addAll(box.getManifest().getFile()).build()) {
			FileUtils.copyURLToFile(getClass().getResource("/" + box.getName() + "/" + f), new File(box.getTarget(), f));
		}
	}
}
