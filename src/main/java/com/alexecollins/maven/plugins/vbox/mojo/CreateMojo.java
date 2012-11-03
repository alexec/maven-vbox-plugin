package com.alexecollins.maven.plugins.vbox.mojo;

import com.alexecollins.maven.plugins.vbox.task.Create;
import com.alexecollins.maven.plugins.vbox.VBox;

/**
 * @goal create
 * @phase pre-integration-test
 */
public class CreateMojo extends AbstractVBoxesMojo {

	protected void execute(final VBox box) throws Exception {
		new Create(box).invoke();
	}

}
