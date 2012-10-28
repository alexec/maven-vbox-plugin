package com.alexecollins.maven.plugins.vbox;

import com.alexecollins.maven.plugins.vbox.schema.VirtualBox;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

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


	public void execute() throws MojoExecutionException {

		for (File f : new File(basedir, "src/main/vbox").listFiles(new FileFilter() {
			public boolean accept(File file) {
				return file.isDirectory();
			}
		})) {
			try {
				execute(f.toURI());
			} catch (Exception e) {
				throw new MojoExecutionException("failed to create " + f, e);
			}
		}
	}

	protected abstract void execute(URI src) throws Exception;

	protected String getName(URI dir) {
		final String p = dir.getPath();
		final String q = p.endsWith("/") ? p.substring(0, p.length() - 1) : p;
		return q.substring(q.lastIndexOf('/') + 1);
	}

	protected VirtualBox getCfg(URI src) throws IOException, URISyntaxException {
		return JAXB.unmarshal(new URI(src.toString() + "/VirtualBox.xml").toURL().openStream(), VirtualBox.class);
	}

	protected File getTarget(String name) {
		return new File(outputDirectory, "vbox/" + name);
	}

	protected void exec(String... strings) throws IOException, InterruptedException {

		final ProcessBuilder b = new ProcessBuilder(strings);
		getLog().debug("executing " + b.command());
		final Process p = b.start();

		// stdout
		log(p.getInputStream());
		// stderr
		log(p.getErrorStream());

		if (p.waitFor() != 0) {
			throw new RuntimeException("failed to execute " + b.command() + " exitValue=" + p.exitValue());
		}
	}

	private void log(final InputStream inputStream) throws IOException {
		final BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
		try {
			String l;
			while ((l = r.readLine()) != null) {
				getLog().debug(l);
			}
		} finally {
			r.close();
		}
	}
}
