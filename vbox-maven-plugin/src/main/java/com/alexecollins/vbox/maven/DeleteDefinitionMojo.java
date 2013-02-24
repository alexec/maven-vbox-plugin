package com.alexecollins.vbox.maven;

import com.alexecollins.vbox.core.VBox;
import com.alexecollins.vbox.core.Work;
import com.alexecollins.vbox.core.task.DeleteDefinition;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import java.io.File;

/**
 * Deletes a definition, also deleting any VMs.
 *
 * @author alexec (alex.e.c@gmail.com)
 * @since 3.0.0
 */
public class DeleteDefinitionMojo extends AbstractVBoxMojo {

	/**
	 * The name of the template to use, e.g. "app1".
	 *
	 * @parameter property="vbox.name"
	 * @required
	 */
	private String name;

	public void execute() throws MojoExecutionException, MojoFailureException {
		try {
			final File file = new File(project.getBasedir(), "src/main/vbox/" + name);
			if (file.exists()) {
				new DeleteDefinition(new Work(getContext()), new VBox(getContext(), file.toURI())).call();
			}
		} catch (Exception e) {
			throw new MojoExecutionException("failed to create definition", e);
		}
	}
}
