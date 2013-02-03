package com.alexecollins.vbox.maven.patch;

import com.alexecollins.vbox.core.VBox;
import com.alexecollins.vbox.core.patch.Patch;
import com.alexecollins.vbox.core.task.PatchDefinition;
import com.alexecollins.vbox.maven.AbstractVBoxesMojo;

import java.util.Collections;
import java.util.List;

/**
 * Apply a sequence of patches to a definition.
 *
 * @goal patch-definition
 * @phase generate-resources
 * @author alexec (alex.e.c@gmail.com)
 * @since 2.0.0
 */
public class PatchDefinitionMojo extends AbstractVBoxesMojo {

	/**
	 * The patches to apply in order.
	 *
	 * @parameter
	 * @required
	 */
	private List<Patch> patches = Collections.emptyList();

	@Override
	protected void execute(VBox box) throws Exception {
		new PatchDefinition(getWork(), box,  patches).call();
	}
}
