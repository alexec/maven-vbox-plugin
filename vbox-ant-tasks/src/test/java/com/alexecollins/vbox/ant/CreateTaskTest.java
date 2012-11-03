package com.alexecollins.vbox.ant;

import org.junit.Test;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class CreateTaskTest extends AbstractTaskTest {
	@Test
	public void testExecute() throws Exception {
		final CreateTask sut = new CreateTask();
		sut.setDir(dir);
		sut.execute();
	}
}
