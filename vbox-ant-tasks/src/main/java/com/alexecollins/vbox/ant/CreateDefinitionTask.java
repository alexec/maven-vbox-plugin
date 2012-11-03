package com.alexecollins.vbox.ant;

import com.alexecollins.vbox.core.task.CreateDefinition;
import org.apache.tools.ant.BuildException;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class CreateDefinitionTask extends AbstractTask {

	private String name;

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void execute() throws BuildException {
		if (name == null) {
			throw new BuildException("name is null");
		}
		if (dir == null) {
			throw new BuildException("dir is null");
		}
		try {
			new CreateDefinition(name, dir).invoke();
		} catch (Exception e) {
			throw new BuildException(e);
		}
	}
}
