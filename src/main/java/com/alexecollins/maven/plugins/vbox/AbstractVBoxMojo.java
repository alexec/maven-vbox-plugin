package com.alexecollins.maven.plugins.vbox;

import com.alexecollins.maven.plugins.vbox.schema.Manifest;
import com.alexecollins.maven.plugins.vbox.schema.Provisions;
import com.alexecollins.maven.plugins.vbox.schema.VirtualBox;
import org.apache.maven.plugin.AbstractMojo;

import javax.xml.bind.JAXB;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public abstract class AbstractVBoxMojo extends AbstractMojo {

	/**
	 * @parameter expression="${project.basedir}"
	 * @required
	 */
	protected File basedir = new File(".");

	/**
	 * @parameter expression="${project.build.directory}"
	 * @required
	 */
	protected File outputDirectory = new File("target");

	protected VirtualBox getVirtualBox(URI src) throws IOException, URISyntaxException {
		return JAXB.unmarshal(new URI(src.toString() + "/VirtualBox.xml").toURL().openStream(), VirtualBox.class);
	}

	protected Manifest getManifest(URI src) throws IOException, URISyntaxException {
		return JAXB.unmarshal(new URI(src.toString() + "/Manifest.xml").toURL().openStream(), Manifest.class);
	}

	protected Provisions getProvisions(URI src) throws IOException, URISyntaxException {
		return JAXB.unmarshal(new URI(src.toString() + "/Provisions.xml").toURL().openStream(), Provisions.class);
	}

	protected String getName(URI dir) {
		final String p = dir.getPath();
		final String q = p.endsWith("/") ? p.substring(0, p.length() - 1) : p;
		return q.substring(q.lastIndexOf('/') + 1);
	}

	protected File getTarget(String name) {
		return new File(outputDirectory, "vbox/boxes/" + name);
	}

	protected boolean exists(String name) {
		return new File(getTarget(name), name + ".vbox").exists();
	}

	protected Set<String> getSnapshots(final String name) throws IOException, InterruptedException {
		final Set<String> s = new HashSet<String>();
		final Properties p = getProperties(name);
		for (Object o : p.keySet()) {
			if (o.toString().startsWith("SnapshotName")) {
				s.add(p.getProperty(o.toString()));
			}
		}
		return s;
	}

	private Properties getProperties(final String name) throws IOException, InterruptedException {
		return getPropertiesFromString(exec("vboxmanage", "showvminfo", name, "--machinereadable"));
	}

	static Properties getPropertiesFromString(final String exec) {
		final Properties p = new Properties();
		final Matcher m = Pattern.compile("([^=\n]*)=\"([^\"]*)\"").matcher(exec);
		while (m.find()) {
			p.setProperty(m.group(1), m.group(2));
		}
		return p;
	}

	protected void awaitPowerOff(final String name, long millis) throws InterruptedException, IOException {
		long s = System.currentTimeMillis();
		while (!getProperties(name).get("VMState").equals("poweroff")) {
			getLog().debug("waiting for power off");
			Thread.sleep(1000);
			if (System.currentTimeMillis() > s + millis) {
				throw new IllegalStateException("failed to power off in " + millis + "ms");
			}
		}
		Thread.sleep(3000);
	}

	protected String exec(String... strings) throws IOException, InterruptedException {

		final ProcessBuilder b = new ProcessBuilder(strings);
		getLog().debug("executing " + b.command());
		final Process p = b.start();

		// stdout
		final String out = log(p.getInputStream());
		// stderr
		final String err = log(p.getErrorStream());

		if (p.waitFor() != 0) {
			throw new RuntimeException("failed to execute " + b.command() + ", exitValue=" + p.exitValue() + (err != null ? ": " + err : ""));
		}

		return out;
	}

	private String log(final InputStream inputStream) throws IOException {
		final BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
		final StringBuffer out = new StringBuffer();
		try {
			String l;
			while ((l = r.readLine()) != null) {
				getLog().debug(l);
				out.append(l).append('\n');
			}
		} finally {
			r.close();
		}
		return out.toString();
	}
}
