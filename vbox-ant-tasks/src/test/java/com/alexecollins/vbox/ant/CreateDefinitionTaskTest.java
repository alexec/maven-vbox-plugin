package com.alexecollins.vbox.ant;

import java.io.File;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class CreateDefinitionTaskTest {
	@org.junit.Test
	public void testExecute() throws Exception {
		final CreateDefinitionTask sut = new CreateDefinitionTask();
		sut.setName("CentOS_6_5");
		sut.setDir(new File("target/vbox/CentOS_6_5"));
		sut.execute();
	}
}
