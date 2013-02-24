package com.alexecollins.vbox.core.task;

import com.alexecollins.vbox.core.VBox;
import com.alexecollins.vbox.core.Work;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.concurrent.Callable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Delete a definition.
 *
 * @author alexec (alex.e.c@gmail.com)
 * @since 3.0.0
 */
public class DeleteDefinition implements Callable<Void> {
	private static final Logger LOGGER = LoggerFactory.getLogger(DeleteDefinition.class);
	private final Work work;
	private final VBox box;

	public DeleteDefinition(Work work, VBox box) {
	 	checkNotNull(work, "work");
		checkNotNull(box, "box") ;
		this.work = work;
		this.box = box;
	}

    public Void call() throws Exception {

	    // make sure the machine is gone, but attempt to be idempotent
	    final File file = new File(box.getSrc().toURL().getFile());
	    if (file.exists()) {
		    new Clean(work,box).call();
		    FileUtils.forceDelete(file);
		    LOGGER.info("deleted definition '" + box.getName() + "'");
	    }

		return null;
	}
}
