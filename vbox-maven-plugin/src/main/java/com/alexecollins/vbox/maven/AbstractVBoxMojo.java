package com.alexecollins.vbox.maven;


import com.alexecollins.vbox.core.Context;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.project.MavenProject;
import org.slf4j.impl.MavenLogAdapter;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public abstract class AbstractVBoxMojo extends AbstractMojo {

	/**
	 * @parameter default-value="${project}"
	 * @readonly
	 */
	protected MavenProject project;

	public AbstractVBoxMojo() {
		// ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD
		MavenLogAdapter.LOG = getLog();
	}

	protected Context getContext() {
		return new Context(project.getGroupId() +":"+project.getArtifactId());
	}
}
