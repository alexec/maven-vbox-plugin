package com.alexecollins.vbox.maven;

import com.alexecollins.vbox.core.VBox;
import com.alexecollins.vbox.core.task.Provision;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Create and provision each of the boxes defined in src/main/vbox.
 *
 * @author alexec (alex.e.c@gmail.com)
 * @goal provision
 * @phase pre-integration-test
 */
public class ProvisionMojo extends AbstractVBoxesMojo {

	/**
	 * Which targets in the Provision.xml to execute, or all if "*".
	 *
	 * @parameter property="vbox.provision.targets", default="*"
	 */
	protected String targets = "*";

	@Override
	protected void execute(VBox box) throws Exception {

		new Provision(getWork(), box, new HashSet<String>(Arrays.asList(this.targets.split(",")))).call();
	}


}