package com.alexecollins.vbox.ant;

import com.alexecollins.vbox.core.task.ListDefinitions;
import org.apache.tools.ant.BuildException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class ListDefinitionsTask extends AbstractTask {

	private static final Logger LOGGER = LoggerFactory.getLogger(ListDefinitionsTask.class);

	@Override
	public void execute() throws BuildException {

		try {
			LOGGER.info(new ListDefinitions().call().toString());
		} catch (Exception e) {
			throw new BuildException(e);
		}
	}
}
