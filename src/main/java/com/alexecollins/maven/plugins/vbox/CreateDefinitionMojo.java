package com.alexecollins.maven.plugins.vbox;

import com.google.common.collect.ImmutableSet;
import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;

/**
 * @author alexec (alex.e.c@gmail.com)
 * @goal create-definition
 */
public class CreateDefinitionMojo extends AbstractVBoxMojo {

	/**
	 * @parameter expression="${vbox.name}"
	 * @required
	 */
	protected String name;

	public void execute() throws MojoExecutionException, MojoFailureException {
		try {
			createDefn(new VBox(new File("src/main/vbox/" + name).toURI()));
		} catch (Exception e) {
			throw new MojoExecutionException("failed to create definition", e);
		}
	}

	void createDefn(VBox box) throws IOException, URISyntaxException {

		if (!box.exists() && !new File(box.getSrc().toURL().getFile()).mkdirs())
			throw new IllegalStateException();

		for (String f : ImmutableSet.<String>builder()
				.addAll(Arrays.asList("MediaRegistry.xml", "VirtualBox.xml", "Manifest.xml", "Provisioning.xml"))
				.addAll(box.getManifest().getFile()).build()) {
			FileUtils.copyURLToFile(getClass().getResource("/" + name + "/" + f), new File(box.getTarget(), f));
		}
	}
}
