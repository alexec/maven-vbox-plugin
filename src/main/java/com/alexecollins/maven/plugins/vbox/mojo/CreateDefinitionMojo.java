package com.alexecollins.maven.plugins.vbox.mojo;

import com.alexecollins.maven.plugins.vbox.VBox;
import com.alexecollins.maven.plugins.vbox.task.CreateDefinition;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import java.io.File;

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

	void createDefn(VBox box) throws Exception {
	   new CreateDefinition(box).invoke();
	}
}
