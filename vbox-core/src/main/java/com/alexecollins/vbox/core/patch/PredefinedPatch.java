package com.alexecollins.vbox.core.patch;

import com.alexecollins.vbox.core.VBox;
import com.google.common.io.Resources;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Set;
import java.util.TreeSet;

/**
 * A pre-defined patch based on unified diff format.
 *
 * @author alexec (alex.e.c@gmail.com)
 * @since 2.0.0
 */
public class PredefinedPatch implements Patch {
	private final UnifiedPatch unifiedPatch;
	private final String name;

	public PredefinedPatch(String name) {
		this.name = name;
		if (name == null) {throw new IllegalArgumentException("name is null");}
		final Set<String> l = list();
		if (!l.contains(name)) {throw new IllegalArgumentException(name +" not in " + l);}
		try {
			unifiedPatch = new UnifiedPatch(Resources.toByteArray(getClass().getResource(name + ".patch")), 1);
		} catch (IOException e) {
			throw new AssertionError(e);
		}
	}

	public void apply(VBox box) throws Exception {
		unifiedPatch.apply(box);
	}

	public String getName() {
		return name;
	}


	public static Set<String> list() {
		try {
			return new TreeSet<String>(Resources.readLines(PredefinedPatch.class.getResource("manifest.txt"), Charset.forName("UTF-8")));
		} catch (IOException e) {
			throw new AssertionError(e);
		}
	}
}
