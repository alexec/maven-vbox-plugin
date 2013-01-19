package com.alexecollins.vbox.maven;

import com.alexecollins.vbox.core.VBox;
import com.alexecollins.vbox.core.task.Start;

/**
 * Start all the boxes defined in src/main/vbox.
 *
 * @goal start
 * @phase pre-integration-test
 * @see StopMojo
 */
public class StartMojo extends AbstractVBoxesMojo {

	protected void execute(VBox box) throws Exception {
		new Start(box).call();
	}
}
