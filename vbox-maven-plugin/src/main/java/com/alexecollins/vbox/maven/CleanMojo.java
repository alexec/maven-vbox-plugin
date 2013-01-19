package com.alexecollins.vbox.maven;

import com.alexecollins.vbox.core.VBox;
import com.alexecollins.vbox.core.task.Clean;

/**
 * Shuts down and clean boxes.
 *
 * @goal clean
 * @phase clean
 */
public class CleanMojo extends AbstractVBoxesMojo {

	protected void execute(VBox box) throws Exception {
		new Clean(work,box).call();
	}

}
