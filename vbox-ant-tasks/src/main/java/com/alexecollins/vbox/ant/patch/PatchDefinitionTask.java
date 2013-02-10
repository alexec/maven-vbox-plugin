package com.alexecollins.vbox.ant.patch;


import com.alexecollins.vbox.ant.AbstractTask;
import com.alexecollins.vbox.core.VBox;
import com.alexecollins.vbox.core.patch.ArchPatch;
import com.alexecollins.vbox.core.patch.Patch;
import com.alexecollins.vbox.core.patch.UserDefinedPatch;
import com.alexecollins.vbox.core.task.PatchDefinition;
import org.apache.tools.ant.BuildException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author alexec (alex.e.c@gmail.com)
 * @since 2.0.0
 */
public class PatchDefinitionTask extends AbstractTask {

	private List<Patch> patches = new ArrayList<Patch>();

	public ArchPatch createArchPatch() throws IOException {
		ArchPatch patch = new ArchPatch();
		patches.add(patch);
		return patch;
	}
	public com.alexecollins.vbox.ant.patch.PredefinedPatch createPredefinedPatch() {
		com.alexecollins.vbox.ant.patch.PredefinedPatch patch = new com.alexecollins.vbox.ant.patch.PredefinedPatch();
		patches.add(patch);
		return patch;
	}
	public UserDefinedPatch createUserDefinedPatch() throws IOException {
		UserDefinedPatch patch = new UserDefinedPatch();
		patches.add(patch);
		return patch;
	}

	@Override
	public void execute() throws BuildException {
		if (dir == null) {
			throw new BuildException("dir is null");
		}

		try {
			new PatchDefinition(work(), new VBox(dir.toURI()), patches).call();
		} catch (Exception e) {
			throw new BuildException(e);
		}
	}

}
