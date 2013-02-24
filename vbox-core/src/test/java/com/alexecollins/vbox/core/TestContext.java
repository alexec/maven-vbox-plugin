package com.alexecollins.vbox.core;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class TestContext extends Context {
	public TestContext() {
		super("test");

		Repo.getInstance().pathOf(this).deleteOnExit();
	}
}
