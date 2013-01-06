package com.alexecollins.vbox.maven;

import com.alexecollins.vbox.core.VBox;
import com.alexecollins.vbox.core.task.Provision;

import java.util.Arrays;
import java.util.HashSet;

/**
 * @author alexec (alex.e.c@gmail.com)
 * @goal provision
 * @phase pre-integration-test
 */
public class ProvisionMojo extends AbstractVBoxesMojo {

	/**
	 * Which targets to do, or all if "*".
	 *
	 * @parameter expression="${vbox.provision.targets}", default="*"
	 */
	protected String targets = "*";

	@Override
	protected void execute(VBox box) throws Exception {

		new Provision(work, box, new HashSet<String>(Arrays.asList(this.targets.split(",")))).call();
	}


}