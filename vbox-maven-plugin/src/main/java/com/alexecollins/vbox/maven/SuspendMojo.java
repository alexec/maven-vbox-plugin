package com.alexecollins.vbox.maven;

import com.alexecollins.vbox.core.VBox;
import com.alexecollins.vbox.core.task.Suspend;

/**
 * Suspend the VM.
 *
 * @goal suspend
 * @author alexec (alex.e.c@gmail.com)
 * @since 3.0.0
 */
public class SuspendMojo extends AbstractVBoxesMojo {
	@Override
	protected void execute(VBox box) throws Exception {
		new Suspend(box).call();
	}
}
