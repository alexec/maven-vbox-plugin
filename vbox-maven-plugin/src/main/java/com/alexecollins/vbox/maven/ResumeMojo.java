package com.alexecollins.vbox.maven;

import com.alexecollins.vbox.core.VBox;
import com.alexecollins.vbox.core.task.Resume;

/**
 * Resume a suspended box.
 *
 * @goal resume
 * @author alexec (alex.e.c@gmail.com)
 * @since 3.0.0
 */
public class ResumeMojo extends  AbstractVBoxesMojo {
	@Override
	protected void execute(VBox box) throws Exception {
		new Resume(box).call();
	}
}
