package com.alexecollins.vbox.ant;

import com.alexecollins.vbox.core.VBox;
import com.alexecollins.vbox.core.task.Clean;
import org.apache.tools.ant.BuildException;

import java.io.File;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class CleanTask extends AbstractTask {

	@Override
	public void execute() throws BuildException {
		if (dir == null) {
			throw new BuildException("dir is null");
		}
		try {
			new Clean(new VBox(new File(dir).toURI())).invoke();
		} catch (Exception e) {
			throw new BuildException(e);
		}
	}
}
