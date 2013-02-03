package com.alexecollins.vbox.ant;

import com.alexecollins.vbox.core.VBox;
import com.alexecollins.vbox.core.task.Clean;
import org.apache.tools.ant.BuildException;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class CleanTask extends AbstractTask {

	@Override
	public void execute() throws BuildException {
		if (dir == null) {
			throw new BuildException("dir is null");
		}
		if (work == null) {throw new BuildException("work is null");}
		try {
			new Clean(work(), new VBox(dir.toURI())).call();
		} catch (Exception e) {
			throw new BuildException(e);
		}
	}
}
