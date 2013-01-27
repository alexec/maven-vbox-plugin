package com.alexecollins.vbox.ant;

import com.alexecollins.vbox.core.Work;
import org.apache.tools.ant.Task;

import java.io.File;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
abstract class AbstractTask extends Task {
	File dir;
	File work;
	File cacheDir;

	public void setDir(String dir) {
		this.dir = new File(dir);
	}

	public void setWork(String work) {
		this.work = new File(work);
	}

	public File getCacheDir() {
		return cacheDir;
	}

	protected Work getWork() {
		return new Work(work,cacheDir);

	}
}
