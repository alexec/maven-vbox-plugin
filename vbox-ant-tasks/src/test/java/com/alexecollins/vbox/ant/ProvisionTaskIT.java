package com.alexecollins.vbox.ant;

import org.junit.Test;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class ProvisionTaskIT extends AbstractTaskTest {
	@Test
	public void testExecute() throws Exception {
		final CreateTask create = new CreateTask();
		create.setDir(dir);
		create.execute();
		final ProvisionTask sut = new ProvisionTask();
		sut.setDir(dir);
		sut.execute();
	}
}
