package com.alexecollins.vbox.core.task;

import com.alexecollins.vbox.core.VBox;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public abstract class AbstractTask implements Callable<Void> {
	final File work;

	protected AbstractTask(File work) {
		this.work = work;
	}


	/**
	 * @return The target (aka work) directory for the box.
	 */
	File getTarget(VBox box) {
		return new File(work, "vbox/boxes/" + box.getName());
	}

	/**
	 *  Complete the variables.
	 */
	public String subst(final VBox box, String line) throws IOException, InterruptedException, ExecutionException {
		line = line.replaceAll("%VBOX_ADDITIONS%", VBox.findGuestAdditions().getPath().replaceAll("\\\\", "/"));
		line = line.replaceAll("%NAME%", box.getName());
		return line;
	}
}
