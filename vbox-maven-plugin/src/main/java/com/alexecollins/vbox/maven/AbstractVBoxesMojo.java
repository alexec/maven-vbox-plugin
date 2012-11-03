package com.alexecollins.vbox.maven;

import com.alexecollins.vbox.core.VBox;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A mojo that looks for multiple definition and executes on all of them.
 *
 * @author alexec (alex.e.c@gmail.com)
 */
public abstract class AbstractVBoxesMojo extends AbstractVBoxMojo {

	/**
	 * @parameter expression="${vbox.names}", default="*"
	 */
	protected String names = "*";

	public void execute() throws MojoExecutionException {

		final List<String> names = Arrays.asList(this.names.split(","));

		for (File f : new File("src/main/vbox").listFiles(new FileFilter() {
			public boolean accept(File file) {
				return file.isDirectory();
			}
		})) {
			try {
				final VBox box = new VBox(f.toURI());

				if (names.contains(box.getName()) || names.equals(Collections.singletonList("*"))) {
					execute(box);
				} else {
					getLog().info("skipping " + box.getName());
				}
			} catch (Exception e) {
				throw new MojoExecutionException("failed to create " + f, e);
			}
		}
	}

	protected abstract void execute(VBox box) throws Exception;
}
