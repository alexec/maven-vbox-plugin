package com.alexecollins.vbox.maven.patch;

import com.alexecollins.vbox.core.VBox;
import com.alexecollins.vbox.core.patch.Patch;

import java.util.Collections;
import java.util.Map;

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

	/**
	 * Thing that need to be mapped.
	 */
	private Map<String,String> properties = Collections.emptyMap();

	public void apply(VBox box) throws Exception {
		new com.alexecollins.vbox.core.patch.PredefinedPatch(name, properties).apply(box);
	}

	public String getName() {
		return name;
	}
}
