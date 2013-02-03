package com.alexecollins.vbox.core.task;

import com.alexecollins.vbox.core.VBox;
import com.alexecollins.vbox.core.Work;
import com.alexecollins.vbox.core.patch.Patch;

import java.util.List;

/**
 * Apply multiple patches.
 *
 * @author alexec (alex.e.c@gmail.com)
 * @since 2.0.0
 */
public class PatchDefinition extends AbstractTask {
	private final List<Patch> patches;

	public PatchDefinition(Work work, VBox box, List<Patch> patches) {
		super(work, box);
		if (patches == null) {throw new IllegalArgumentException();}
		this.patches = patches;
	}

	public Void call() throws Exception {
		for (Patch patch : patches) {
			patch.apply(box);
		}

		return null;
	}
}
