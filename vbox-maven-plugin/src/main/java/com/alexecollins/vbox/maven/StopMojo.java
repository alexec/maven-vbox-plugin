package com.alexecollins.vbox.maven;

import com.alexecollins.vbox.core.VBox;
import com.alexecollins.vbox.core.task.Stop;

/**
 * @goal stop
 * @phase post-integration-test
 */
public class StopMojo extends AbstractVBoxesMojo {

	protected void execute(VBox box) throws Exception {
		new Stop(box).call();
	}
}
