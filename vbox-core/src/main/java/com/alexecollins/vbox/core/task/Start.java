package com.alexecollins.vbox.core.task;

import com.alexecollins.util.ExecUtils;
import com.alexecollins.vbox.core.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class Start implements Callable<Void> {
    public enum Type {
            GUI, HEADLESS
    }
	private static final Logger LOGGER = LoggerFactory.getLogger(Start.class);
	private final VBox box;
    private final Type type;

    public Start(VBox box, Type type) {
		this.box = box;
        this.type = type;
    }

	public Void call() throws Exception {
		LOGGER.info("starting " + box.getName());
		ExecUtils.exec("vboxmanage", "startvm", box.getName(), "--type", String.valueOf(type).toLowerCase());
		LOGGER.info("started " + box.getName());
		return null;
	}
}
