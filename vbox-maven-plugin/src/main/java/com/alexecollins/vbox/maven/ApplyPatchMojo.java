package com.alexecollins.vbox.maven;

import com.alexecollins.vbox.core.task.ApplyPatch;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import java.io.File;

/**
 * Apply a named patch.
 *
 * @author alexec (alex.e.c@gmail.com)
 * @goal apply-patch
 */
public class ApplyPatchMojo extends AbstractMojo {
	/**
	 * The source file/directory.
	 *
	 * @parameter property="vbox.a"
	 * @required
	 */
	protected File a;

	/**
	 * The target file/directory. Defaults to the source directory.
	 *
	 * @parameter property="vbox.b", default-value="${vbox.a}"
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
			new ApplyPatch(a, b, patch).call();
		} catch (Exception e) {
			throw new MojoExecutionException("failed to apply patch", e);
		}
	}
}
