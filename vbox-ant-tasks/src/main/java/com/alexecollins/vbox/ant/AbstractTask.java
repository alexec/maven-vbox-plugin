package com.alexecollins.vbox.ant;

import org.apache.tools.ant.Task;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public abstract class AbstractTask extends Task {
	String dir;

	public void setDir(String dir) {
		this.dir = dir;
	}
}
