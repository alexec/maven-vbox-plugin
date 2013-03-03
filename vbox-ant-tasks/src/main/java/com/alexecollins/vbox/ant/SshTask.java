package com.alexecollins.vbox.ant;

import com.alexecollins.vbox.core.VBox;
import com.alexecollins.vbox.core.task.Ssh;
import org.apache.tools.ant.BuildException;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class SshTask extends AbstractTask {

	@Override
	public void execute() throws BuildException {
		if (dir == null) {
			throw new BuildException("dir is null");
		}
		try {
			new Ssh(new VBox(context(), dir.toURI())).call();
		} catch (Exception e) {
			throw new BuildException(e);
		}
	}
}
