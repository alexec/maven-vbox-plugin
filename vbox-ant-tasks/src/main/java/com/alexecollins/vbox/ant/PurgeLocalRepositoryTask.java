package com.alexecollins.vbox.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import java.io.IOException;

/**
 * Purge the local repo of old files.
 *
 * @author alexec (alex.e.c@gmail.com)
 * @since 3.0.0
 */
public class PurgeLocalRepositoryTask extends Task {

	@Override
	public void execute() throws BuildException {
		try {
			new com.alexecollins.vbox.core.task.PurgeLocalRepository().call();
		} catch (IOException e) {
			throw new BuildException(e);
		}
	}
}
