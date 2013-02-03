package com.alexecollins.vbox.core.patch;

import com.alexecollins.vbox.core.VBox;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public interface Patch {
	void apply(final VBox box) throws Exception;
}
