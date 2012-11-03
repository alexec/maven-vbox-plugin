package com.alexecollins.vbox.maven;

import com.alexecollins.vbox.core.task.CreateDefinition;
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
			new CreateDefinition(name, new File("src/main/vbox/" + name)).invoke();
		} catch (Exception e) {
			throw new MojoExecutionException("failed to create definition", e);
		}
	}
}
