package com.alexecollins.vbox.maven.patch;

import com.alexecollins.vbox.maven.AbstractVBoxMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * List built in patches for use with the patch-definition mojo.
 *
 * @goal list-predefined-patches
 * @author alexec (alex.e.c@gmail.com)
 * @since 2.0.0
 */
public class ListPredefinedPatchesMojo extends AbstractVBoxMojo {
	public void execute() throws MojoExecutionException, MojoFailureException {
		for (String patch : com.alexecollins.vbox.core.patch.PredefinedPatch.list()) {
			getLog().info(patch);
		}
	}
}
