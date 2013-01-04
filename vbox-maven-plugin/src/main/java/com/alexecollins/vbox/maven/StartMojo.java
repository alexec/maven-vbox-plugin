package com.alexecollins.vbox.maven;

import com.alexecollins.vbox.core.VBox;
import com.alexecollins.vbox.core.task.Start;

/**
 * @goal start
 * @phase pre-integration-test
 */
public class StartMojo extends AbstractVBoxesMojo {

    /**
     * @parameter expression="${vbox.type}", default="gui"
     */
    protected String type;

	protected void execute(VBox box) throws Exception {
		new Start(box, Start.Type.valueOf(type)).call();
	}
}
