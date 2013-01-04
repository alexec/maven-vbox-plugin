package com.alexecollins.vbox.maven;

import com.alexecollins.vbox.core.VBox;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.File;
import java.io.FileFilter;
import java.util.*;

/**
 * A mojo that looks for multiple definition and executes on all of them.
 *
 * @author alexec (alex.e.c@gmail.com)
 */
public abstract class AbstractVBoxesMojo extends AbstractVBoxMojo {

	/**
     * Boxes to create, in order.
	 * @parameter expression="${vbox.names}", default="*"
	 */
	protected String names = "*";
	final File work = new File("target");

	public void execute() throws MojoExecutionException {

        final File src = new File("src/main/vbox");
        final List<String> names = new ArrayList<String>();
        if (this.names.equals("*")) {
            for (File f : src.listFiles(new FileFilter() {
                public boolean accept(File file) {
                    return file.isDirectory();
                }
            })) {
                names.add(f.getName());
            }
        } else {
		    names.addAll(Arrays.asList(this.names.split(",")));
        }

		for (final String n : names) {
			try {
                execute(new VBox(new File(src, n).toURI()));
			} catch (Exception e) {
				throw new MojoExecutionException("failed to create " + n, e);
			}
		}
	}

	protected abstract void execute(VBox box) throws Exception;
}
