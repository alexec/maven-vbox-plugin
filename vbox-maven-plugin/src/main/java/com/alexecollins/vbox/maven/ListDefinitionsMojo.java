package com.alexecollins.vbox.maven;

import com.alexecollins.vbox.core.task.ListDefinitions;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * List the available template definitions for use with the create-definitions mojo.
 *
 * @author alexec (alex.e.c@gmail.com)
 * @goal list-definitions
 * @see CreateDefinitionMojo
 */
public class ListDefinitionsMojo extends AbstractVBoxMojo {

	private static final Logger LOGGER = LoggerFactory.getLogger(ListDefinitionsMojo.class);

	public void execute() throws MojoExecutionException, MojoFailureException {
		for (String d : new ListDefinitions().call()) {
			LOGGER.info(d);
		}
	}
}
