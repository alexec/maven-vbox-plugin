package com.alexecollins.vbox.maven.patch;

import com.alexecollins.vbox.core.VBox;
import com.alexecollins.vbox.core.patch.Patch;

/**
 * A no-op patch.
 *
 * @author alexec (alex.e.c@gmail.com)
 * @since 2.0.0
 */
public class NoopPatch implements Patch {
	public void apply(VBox box) throws Exception {
		// nop
	}

	public String getName() {
		return "NoopPatch";
	}
}
