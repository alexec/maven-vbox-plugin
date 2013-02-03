package com.alexecollins.vbox.core.patch;

import com.alexecollins.util.Bytes2;
import com.alexecollins.vbox.core.VBox;
import com.google.common.io.Resources;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A pre-defined patch based on unified diff format.
 *
 * @author alexec (alex.e.c@gmail.com)
 * @since 2.0.0
 */
public class PredefinedPatch implements Patch {
	private static final Map<String,List<String>> nameToArgs = new HashMap<String, List<String>>();
	static {
		try {
			for (String l : Resources.readLines(PredefinedPatch.class.getResource("manifest.txt"), Charset.forName("UTF-8"))) {
					final Matcher m = Pattern.compile("([^()]*)\\(([^()]*)\\)").matcher(l);
					if (!m.find()) {throw new IllegalStateException(l + " invalid");}
					nameToArgs.put(
							m.group(1),
							Arrays.asList(m.group(2).split(","))
					);
				}
		} catch (IOException e) {
			throw new AssertionError(e);
		}
	}

	private final UnifiedPatch unifiedPatch;
	private final String name;

	public PredefinedPatch(String name, Map<String, String> properties) throws IOException {
		if (name == null) {throw new IllegalArgumentException("name is null");}
		if (!nameToArgs.containsKey(name)) {throw new IllegalArgumentException(name +" not in " + nameToArgs);}
		if (properties == null) {throw new IllegalArgumentException(properties + " is null");}
		for (String arg : nameToArgs.get(name)) {
			if (!properties.containsKey(arg)) {
				throw new IllegalArgumentException("properties " + properties + " missing " + arg + " (contains " + properties + ")");
			}
		}

		this.name = name;
		try {
			byte[] patch = Resources.toByteArray(getClass().getResource(name + ".patch"));

			for (Map.Entry<String, String> e : properties.entrySet()) {
				LoggerFactory.getLogger(PredefinedPatch.class).info("substituting " + e);
				patch = Bytes2.searchAndReplace(patch, ("${" +e.getKey() +"}").getBytes("UTF-8"), e.getValue().getBytes("UTF-8"));
			}

			unifiedPatch = new UnifiedPatch(patch, 1);
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


	public static Map<String, List<String>> list() {
		return nameToArgs;
	}
}
