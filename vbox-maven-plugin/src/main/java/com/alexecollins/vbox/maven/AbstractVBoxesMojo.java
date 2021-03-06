package com.alexecollins.vbox.maven;

import com.alexecollins.vbox.core.Context;
import com.alexecollins.vbox.core.VBox;
import com.alexecollins.vbox.core.Work;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A mojo that looks for multiple definition and executes on all of them.
 *
 * @author alexec (alex.e.c@gmail.com)
 */
public abstract class AbstractVBoxesMojo extends AbstractVBoxMojo {

	/**
     * Boxes to create, in order.
	 * @parameter property="vbox.names", default="*"
	 */
	private String names = "*";

	public void execute() throws MojoExecutionException {

        final File src = new File(project.getBasedir(), "src/main/vbox");
        final List<String> names = new ArrayList<String>();
        if (this.names.equals("*")) {
	        final File[] boxes = src.listFiles(new FileFilter() {
		        public boolean accept(File file) {
			        return file.isDirectory();
		        }
	        });
	        if (boxes != null) {
		        for (File f : boxes) {
	                names.add(f.getName());
	            }
	        }
        } else {
		    names.addAll(Arrays.asList(this.names.split(",")));
        }

		for (final String n : names) {
			try {
                execute(new VBox(getContext(), new File(src, n).toURI()));
			} catch (Exception e) {
				throw new MojoExecutionException("failed to create " + n, e);
			}
		}
	}

	protected abstract void execute(VBox box) throws Exception;

	public Work getWork() {
		return new Work(new Context(project.getGroupId() + ":" + project.getArtifactId()));
	}
}
