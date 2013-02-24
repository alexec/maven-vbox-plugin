package com.alexecollins.vbox.ant;

import com.alexecollins.vbox.core.VBox;
import com.alexecollins.vbox.core.task.DeleteDefinition;
import org.apache.tools.ant.BuildException;

/**
 * Delete a definition, along with it's box.
 *
 * @author alexec (alex.e.c@gmail.com)
 * @since 3.0.0
 */
public class DeleteDefinitionTask extends AbstractTask {

	@Override
	public void execute() throws BuildException {
		if (dir == null) {
			throw new BuildException("dir is null");
		}
		try {
			if (dir.exists()) {
				new DeleteDefinition(work(), new VBox(context(), dir.toURI())).call();
			}
		} catch (Exception e) {
			throw new BuildException(e);
		}
	}
}