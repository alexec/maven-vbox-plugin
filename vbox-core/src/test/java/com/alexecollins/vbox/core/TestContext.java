package com.alexecollins.vbox.core;

import org.apache.commons.io.FileUtils;

import java.io.IOException;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class TestContext extends Context {
	public TestContext() {
		super("test");
	}

	@Override
	protected void finalize() throws Throwable {
		destroy();
		super.finalize();
	}

	public void destroy() throws IOException {
		FileUtils.deleteDirectory(Repo.getInstance().pathOf(this));
	}
}
