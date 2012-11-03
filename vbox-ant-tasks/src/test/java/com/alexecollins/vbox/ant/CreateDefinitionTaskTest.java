package com.alexecollins.vbox.ant;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class CreateDefinitionTaskTest {
	@org.junit.Test
	public void testExecute() throws Exception {
		final CreateDefinitionTask sut = new CreateDefinitionTask();
		sut.setName("CentOS_6_3");
		sut.setDir("target/vbox/CentOS_6_3");
		sut.execute();
	}
}
