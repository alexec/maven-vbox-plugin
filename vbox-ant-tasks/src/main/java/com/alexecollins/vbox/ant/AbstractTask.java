package com.alexecollins.vbox.ant;

import org.apache.tools.ant.Task;

import java.io.File;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public abstract class AbstractTask extends Task {
	File dir;
	File work;

	public void setDir(String dir) {
		this.dir = new File(dir);
	}

	public void setWork(String work) {
		this.work = new File(work);
	}
}
