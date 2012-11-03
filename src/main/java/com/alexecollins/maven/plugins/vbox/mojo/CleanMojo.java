package com.alexecollins.maven.plugins.vbox.mojo;

import com.alexecollins.maven.plugins.vbox.VBox;
import com.alexecollins.maven.plugins.vbox.task.Clean;

/**
 * @goal clean
 * @phase clean
 */
public class CleanMojo extends AbstractVBoxesMojo {

	protected void execute(VBox box) throws Exception {
		new Clean(box).invoke();
	}

}
