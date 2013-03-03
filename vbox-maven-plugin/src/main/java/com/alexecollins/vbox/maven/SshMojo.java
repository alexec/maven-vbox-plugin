package com.alexecollins.vbox.maven;

import com.alexecollins.vbox.core.VBox;
import com.alexecollins.vbox.core.task.Ssh;

/**
 * Connect to the box.
 *
 * @goal ssh
 * @author alexec (alex.e.c@gmail.com)
 * @since 3.0.0
 */
public class SshMojo extends AbstractVBoxesMojo {

	@Override
	protected void execute(VBox box) throws Exception {
		new Ssh(box).call();
	}
}
