package com.alexecollins.maven.plugins.vbox;

import org.apache.maven.plugin.MojoExecutionException;

import java.io.File;
import java.io.FileFilter;

/**
 * A mojo that looks for multiple definition and executes on all of them.
 *
 * @author alexec (alex.e.c@gmail.com)
 */
public abstract class AbstractVBoxesMojo extends AbstractVBoxMojo {

	public void execute() throws MojoExecutionException {

		for (File f : new File(basedir, "src/main/vbox").listFiles(new FileFilter() {
			public boolean accept(File file) {
				return file.isDirectory();
			}
		})) {
			try {
				execute(new VBox(f.toURI()));
			} catch (Exception e) {
				throw new MojoExecutionException("failed to create " + f, e);
			}
		}
	}

	protected abstract void execute(VBox box) throws Exception;
}
