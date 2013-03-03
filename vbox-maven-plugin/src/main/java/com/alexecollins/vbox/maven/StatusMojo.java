package com.alexecollins.vbox.maven;

import com.alexecollins.vbox.core.VBox;
import com.alexecollins.vbox.core.task.Status;

/**
 * List the status of the machine.
 *
 * @goal status
 * @author alexec (alex.e.c@gmail.com)
 * @since 3.0.0
 */
public class StatusMojo extends AbstractVBoxesMojo {
	@Override
	protected void execute(VBox box) throws Exception {
		getLog().info(new Status(box).call());
	}
}
