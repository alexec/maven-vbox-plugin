package com.alexecollins.vbox.ant;

import com.alexecollins.vbox.core.VBox;
import com.alexecollins.vbox.core.task.CreateDefinition;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class CreateDefinitionTask extends Task {

	private String name;

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void execute() throws BuildException {
		if (name == null) {
			throw new BuildException("name is null");
		}
		try {
			new CreateDefinition(new VBox(CreateDefinition.class.getResource("/" + name + "/").toURI())).invoke();
		} catch (Exception e) {
			throw new BuildException(e);
		}
	}
}
