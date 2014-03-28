package com.alexecollins.vbox.core.patch;

import com.alexecollins.util.Bytes2;
import com.alexecollins.vbox.core.VBox;
import com.google.common.io.Resources;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;
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
				nameToArgs.put(m.group(1), (m.group(2).length() > 0 ? Arrays.<String>asList(m.group(2).split(",")) : Collections.<String>emptyList()));
				}
		} catch (IOException e) {
			throw new AssertionError(e);
		}
	}

	/**
	 * The full patch name.
	 *
	 * E.g.
	 * CentOS_6_5--tomcat6
	 */
	private String name;

	/**
	 * Thing that need to be mapped.
	 */
	private Map<String,String> properties = Collections.emptyMap();

	public PredefinedPatch() {}

	public PredefinedPatch(String name, Map<String, String> properties) {
		this.name = name;
		this.properties = properties;
	}

	public void apply(VBox box) throws Exception {
		if (name == null) {throw new IllegalArgumentException("name is null");}
		if (!nameToArgs.containsKey(name)) {throw new IllegalArgumentException(name +" not in " + nameToArgs);}
		if (properties == null) {throw new IllegalArgumentException(properties + " is null");}
		for (String arg : nameToArgs.get(name)) {
			if (!properties.containsKey(arg)) {
				throw new IllegalArgumentException("properties " + properties.keySet() + " missing '" + arg + "' (requires " + nameToArgs.get(name) + ")");
			}
		}

		final URL resource = getClass().getResource("/com/alexecollins/vbox/core/patch/" + name + ".patch");
		if (resource == null) {
			throw new IllegalStateException("unable to find patch name " + name);
		}
		byte[] patch = Resources.toByteArray(resource);

		for (Map.Entry<String, String> e : properties.entrySet()) {
			LoggerFactory.getLogger(PredefinedPatch.class).info("substituting " + e);
			patch = Bytes2.searchAndReplace(patch, ("${" +e.getKey() +"}").getBytes("UTF-8"), e.getValue().getBytes("UTF-8"));
		}

		new UnifiedPatch(patch, 1).apply(box);
	}

	public String getName() {
		return name;
	}


	public static Map<String, List<String>> list() {
		return nameToArgs;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}
}
