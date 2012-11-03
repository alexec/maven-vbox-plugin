package com.alexecollins.vbox.maven;

import com.alexecollins.vbox.core.task.Create;
import com.alexecollins.vbox.core.VBox;

/**
 * @goal create
 * @phase pre-integration-test
 */
public class CreateMojo extends AbstractVBoxesMojo {

	protected void execute(final VBox box) throws Exception {
		new Create(work, box).invoke();
	}

}
