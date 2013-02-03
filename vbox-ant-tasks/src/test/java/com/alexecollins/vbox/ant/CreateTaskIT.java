package com.alexecollins.vbox.ant;

import org.junit.Test;

import java.io.File;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class CreateTaskIT extends AbstractTaskTest {
	@Test
	public void testExecute() throws Exception {
		final CreateTask sut = new CreateTask();
		sut.setDir(dir);
		sut.setWork(new File("target"));
		sut.execute();
	}
}
