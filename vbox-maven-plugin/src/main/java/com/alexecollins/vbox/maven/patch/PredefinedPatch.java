package com.alexecollins.vbox.maven.patch;

import com.alexecollins.vbox.core.VBox;
import com.alexecollins.vbox.core.patch.Patch;

/**
 * A predefined patch.
 *
 * @author alexec (alex.e.c@gmail.com)
 * @since 2.0.0
 */
public class PredefinedPatch implements Patch {
	/**
	 * The full patch name.
	 *
	 * E.g.
	 * CentOS_6_3--tomcat6
	 */
	private String name;

	public void apply(VBox box) throws Exception {
		new com.alexecollins.vbox.core.patch.PredefinedPatch(name).apply(box);
	}

	public String getName() {
		return name;
	}
}
