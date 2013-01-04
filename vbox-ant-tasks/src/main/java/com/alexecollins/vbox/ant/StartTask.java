package com.alexecollins.vbox.ant;

import com.alexecollins.vbox.core.VBox;
import com.alexecollins.vbox.core.task.Start;
import org.apache.tools.ant.BuildException;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class StartTask extends AbstractTask {

    String type;

	@Override
	public void execute() throws BuildException {
		if (dir == null) {
			throw new BuildException("dir is null");
		}
        if (type == null) {
            throw new BuildException("type is null");
        }
		try {
			new Start(new VBox(dir.toURI()), Start.Type.valueOf(type)).call();
		} catch (Exception e) {
			throw new BuildException(e);
		}
	}
}
