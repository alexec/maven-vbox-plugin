package com.alexecollins.vbox.ant;

import com.alexecollins.vbox.core.Context;
import com.alexecollins.vbox.core.Work;
import org.apache.tools.ant.Task;

import java.io.File;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public abstract class AbstractTask extends Task {
	protected File dir;
	protected String context = "global";

	public void setDir(File dir) {
		this.dir = dir  ;
	}

	public void setContext(String context) {
		this.context = context;
	}

	protected Work work() {
		return new Work(new Context(context));
	}

	protected Context context() {
		return new Context(context);
	}
}
