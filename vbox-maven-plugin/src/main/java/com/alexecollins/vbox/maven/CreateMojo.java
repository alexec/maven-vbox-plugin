package com.alexecollins.vbox.maven;

import com.alexecollins.vbox.core.VBox;
import com.alexecollins.vbox.core.task.Create;

/**
 * Create all the boxes based on the definitions.
 *
 * Normally you'd use "provision" instead.
 *
 * @goal create
 * @phase pre-integration-test
 * @see ProvisionMojo
 */
public class CreateMojo extends AbstractVBoxesMojo {

	protected void execute(final VBox box) throws Exception {
		new Create(getWork(), box).call();
	}

}
