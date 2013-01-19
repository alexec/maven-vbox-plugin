package com.alexecollins.vbox.maven;

import com.alexecollins.vbox.core.VBox;
import com.alexecollins.vbox.core.task.Stop;

/**
 * Stop all the boxes defined in src/main/vbox.
 *
 * @goal stop
 * @phase post-integration-test
 * @see StartMojo
 */
public class StopMojo extends AbstractVBoxesMojo {

	protected void execute(VBox box) throws Exception {
		new Stop(box).call();
	}
}
