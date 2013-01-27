package com.alexecollins.vbox.maven;

import com.alexecollins.vbox.core.task.CreatePatch;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import java.io.File;

/**
 * Create a patch from two files or directories.
 *
 * @goal create-patch
 * @author alexec (alex.e.c@gmail.com)
 */
public class CreatePatchMojo extends AbstractMojo {
	/**
	 * The source file/directory.
	 *
	 * @parameter property="vbox.a"
	 * @required
	 */
	protected File a;

	/**
	 * The target file/directory.
	 *
	 * @parameter property="vbox.b"
	 * @required
	 */
	protected File b;

	/**
	 * The output file.
	 *
	 * @parameter property="vbox.patch"
	 * @required
	 */
	protected File patch;

	public void execute() throws MojoExecutionException, MojoFailureException {
		try {
			new CreatePatch(a,b,patch).call();
		} catch (Exception e) {
			throw new MojoExecutionException("failed to create patch", e);
		}
	}
}
