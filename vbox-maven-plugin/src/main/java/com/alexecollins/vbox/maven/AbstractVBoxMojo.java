package com.alexecollins.vbox.maven;


import org.apache.maven.plugin.AbstractMojo;
import org.slf4j.impl.MavenLogAdapter;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
abstract class AbstractVBoxMojo extends AbstractMojo {

	AbstractVBoxMojo() {
		// ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD
		MavenLogAdapter.LOG = getLog();
	}
}
