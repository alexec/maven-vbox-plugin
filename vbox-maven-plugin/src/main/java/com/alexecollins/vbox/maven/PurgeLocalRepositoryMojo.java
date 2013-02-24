package com.alexecollins.vbox.maven;

import com.alexecollins.vbox.core.task.PurgeLocalRepository;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import java.io.IOException;

/**
 * Purge the local repository.
 *
 * @goal purge-local-repository
 * @phase clean
 * @author alexec (alex.e.c@gmail.com)
 * @since 3.0.0
 */
public class PurgeLocalRepositoryMojo extends AbstractVBoxMojo {
	public void execute() throws MojoExecutionException, MojoFailureException {
		try {
			new PurgeLocalRepository().call();
		} catch (IOException e) {
			throw new MojoExecutionException("failed to execute", e);
		}
	}
}
