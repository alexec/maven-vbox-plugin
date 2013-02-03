package com.alexecollins.vbox.ant.patch;

import com.alexecollins.vbox.core.VBox;
import com.alexecollins.vbox.core.patch.Patch;

import java.util.HashMap;
import java.util.Map;

/**
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
	private Map<String,String> properties = new HashMap<String, String>();

	public void apply(VBox box) throws Exception {
		new com.alexecollins.vbox.core.patch.PredefinedPatch(name, properties).apply(box);
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setProperties(String properties) {
		for (String pair : properties.split(",")) {
			final int i = pair.indexOf('=');
			this.properties.put(pair.substring(0,i), pair.substring(i+1));
		}
	}
}
