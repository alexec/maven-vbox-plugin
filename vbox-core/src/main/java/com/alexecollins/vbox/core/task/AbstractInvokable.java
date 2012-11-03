package com.alexecollins.vbox.core.task;

import com.alexecollins.util.Invokable;
import com.alexecollins.vbox.core.VBox;

import java.io.File;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public abstract class AbstractInvokable implements Invokable{
	final File work;

	protected AbstractInvokable(File work) {
		this.work = work;
	}


	/**
	 * @return The target (aka work) directory for the box.
	 */
	File getTarget(VBox box) {
		return new File(work, "vbox/boxes/" + box.getName());
	}

	/**
	 * @return If the box exists.
	 */
	public boolean exists(VBox box) {
		return getTarget(box).exists();
	}
}
