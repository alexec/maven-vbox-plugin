package com.alexecollins.maven.plugins.vbox;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

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

		for (String f : new String[]{"MediaRegistry.xml", "VirtualBox.xml", "Manifest.xml", "Provisioning.xml"}) {
			FileUtils.copyURLToFile(getClass().getResource("/" + name + "/" + f), new File(box.getTarget(), f));
		}

		for (String f : box.getManifest().getFile()) {
			FileUtils.copyURLToFile(getClass().getResource("/" + name + "/" + f), new File(box.getTarget(), f));
		}
	}

}
