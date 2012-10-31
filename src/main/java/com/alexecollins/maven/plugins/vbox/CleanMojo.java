package com.alexecollins.maven.plugins.vbox;

import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * @goal clean
 * @phase clean
 */
public class CleanMojo extends AbstractVBoxesMojo {

	protected void execute(VBox box) throws URISyntaxException, IOException, InterruptedException, TimeoutException, ExecutionException {

		getLog().info("cleaning '" + box.getName() + "'");

		if (ExecUtils.exec("vboxmanage", "list", "vms").contains("\"" + box.getName() + "\"")) {
			if (box.getProperties().getProperty("VMState").equals("running")) {
				box.powerOff();
				box.awaitState(10000l, "poweroff");
			}

			box.unregister();
		}

/*		final Matcher m = Pattern.compile("Location:[ \t]*(.*)\n").matcher(ExecUtils.exec("vboxmanage", "list", "hdds"));
		while (m.find()) {
			if (m.group().contains(name)) {
				ExecUtils.exec("vboxmanage", "closemedium", "disk", m.group(1).trim());
			}
		}*/

		FileUtils.deleteDirectory(box.getTarget());
	}
}
