package com.alexecollins.vbox.ant;

import com.alexecollins.vbox.core.Work;
import org.apache.tools.ant.Task;

import java.io.File;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public abstract class AbstractTask extends Task {
	protected File dir;
	protected File work;
	protected File cacheDir;

	public void setDir(File dir) {
		this.dir = dir  ;
	}

	public void setWork(File work) {
		this.work = work;
	}

	public void setCacheDir(File cacheDir) {
		this.cacheDir = cacheDir;
	}

	protected Work work() {
		return new Work(work,cacheDir);
	}
}
