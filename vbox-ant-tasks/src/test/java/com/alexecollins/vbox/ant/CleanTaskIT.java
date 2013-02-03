package com.alexecollins.vbox.ant;

import org.junit.Test;

import java.io.File;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class CleanTaskIT extends AbstractTaskTest {


	@Test
	public void testExecute() throws Exception {
		final CreateDefinitionTask defn = new CreateDefinitionTask();
		defn.setName("CentOS_6_3");
		defn.setDir(dir);
		defn.setWork(new File("target"));
		defn.execute();
		final CleanTask sut = new CleanTask();
		sut.setDir(dir);
		sut.setWork(new File("target"));
		sut.execute();
	}
}
