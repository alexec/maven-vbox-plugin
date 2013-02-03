package com.alexecollins.vbox.core.patch;

import com.alexecollins.vbox.core.VBox;

/**
 * A single patch.
 *
 * @author alexec (alex.e.c@gmail.com)
 * @since 2.0.0
 */
public interface Patch {
	/**
	 * Apply the patch to a box.
	 */
	void apply(final VBox box) throws Exception;

	String getName();
}
