package com.alexecollins.maven.plugins.vbox;

import com.alexecollins.maven.plugins.vbox.schema.VirtualBox;
import org.apache.maven.plugin.AbstractMojo;

import javax.xml.bind.JAXB;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

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

	protected VirtualBox getCfg(URI src) throws IOException, URISyntaxException {
		final URI u = new URI(src.toString() + "/VirtualBox.xml");
		return JAXB.unmarshal(u.toURL().openStream(), VirtualBox.class);
	}

	protected String getName(URI dir) {
		final String p = dir.getPath();
		final String q = p.endsWith("/") ? p.substring(0, p.length() - 1) : p;
		return q.substring(q.lastIndexOf('/') + 1);
	}

	protected File getTarget(String name) {
		return new File(outputDirectory, "vbox/" + name);
	}

	protected void remove(URI src) throws IOException, InterruptedException {
		final String name = getName(src);
		try {
			exec("vboxmanage", "controlvm", name, "poweroff");
			Thread.sleep(3000); // a moment or two to shutdown
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		} catch (Exception e) {
			getLog().info("failed to power off box or unregister (probably OK): " + e.getMessage());
		}

		exec("vboxmanage", "unregistervm", name, "--delete");
	}

	protected void exec(String... strings) throws IOException, InterruptedException {

		final ProcessBuilder b = new ProcessBuilder(strings);
		getLog().debug("executing " + b.command());
		final Process p = b.start();

		// stdout
		log(p.getInputStream());
		// stderr
		final String errMsg = log(p.getErrorStream());

		if (p.waitFor() != 0) {
			throw new RuntimeException("failed to execute " + b.command() + ", exitValue=" + p.exitValue() + (errMsg != null ? ": " + errMsg : ""));
		}
	}

	private String log(final InputStream inputStream) throws IOException {
		final BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
		try {
			String l;
			while ((l = r.readLine()) != null) {
				getLog().debug(l);
			}
			return l;
		} finally {
			r.close();
		}
	}
}
