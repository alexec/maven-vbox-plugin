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
		try {
			LOGGER.info(new ListDefinitions().call().toString());
		} catch (Exception e) {
			throw new MojoExecutionException("failed to create definition", e);
		}
	}
}
