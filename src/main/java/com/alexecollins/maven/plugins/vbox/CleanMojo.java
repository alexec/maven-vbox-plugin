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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @goal clean
 * @phase clean
 */
public class CleanMojo extends AbstractVBoxesMojo {

	protected void execute(URI src) throws URISyntaxException, IOException, InterruptedException {

		if (src == null) {
			throw new IllegalArgumentException();
		}

		final String name = getName(src);

		getLog().info("cleaning '" + name + "'");

		if (exec("vboxmanage", "list", "vms").contains("\"" + name + "\"")) {
			if (getProperties(name).getProperty("VMState").equals("running")) {
				exec("vboxmanage", "controlvm", name, "poweroff");
				awaitPowerOff(name, 10000);
			}

			exec("vboxmanage", "unregistervm", name, "--delete");
		}

		final Matcher m = Pattern.compile("Location:[ \t]*(.*)\n").matcher(exec("vboxmanage", "list", "hdds"));
		while (m.find()) {
			if (m.group().contains(name)) {
				exec("vboxmanage", "closemedium", "disk", m.group(1).trim());
			}
		}

		FileUtils.deleteDirectory(getTarget(name));
	}
}
