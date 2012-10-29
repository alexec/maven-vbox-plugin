package com.alexecollins.maven.plugins.vbox;

import java.net.URI;

/**
 * @author alexec (alex.e.c@gmail.com)
 * @phase pre-integration-test
 * @goal recreate
 */
public class RecreateMojo extends CreateMojo {

	@Override
	protected void execute(URI src) throws Exception {
		remove(src);
		super.execute(src);
	}
}
