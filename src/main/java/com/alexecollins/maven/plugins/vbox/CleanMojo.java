package com.alexecollins.maven.plugins.vbox;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @goal clean
 * @phase clean
 */
public class CleanMojo extends AbstractVBoxMojo {

	protected void execute(URI src) throws URISyntaxException, IOException, InterruptedException {

		if (src == null) {
			throw new IllegalArgumentException();
		}

		final String name = getName(src);

		getLog().info("cleaning '" + name + "'");

		final File t = getTarget(name);

		getLog().debug("target " + t);

		if (t.exists()) {
			try {
				exec("vboxmanage", "controlvm", name, "poweroff");
				Thread.sleep(3000); // a moment or two to shutdown
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			} catch (Exception e) {
				getLog().info("failed to power off box (probably OK): " + e.getMessage());
			}
			exec("vboxmanage", "unregistervm", name, "--delete");

			FileUtils.deleteDirectory(t);
		}
	}
}
