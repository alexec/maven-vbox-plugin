package com.alexecollins.maven.plugins.vbox;

import com.alexecollins.maven.plugins.vbox.schema.VirtualBox;

import java.net.URI;

/**
 * @author alexec (alex.e.c@gmail.com)
 * @goal provision
 * @phase pre-integration-test
 */
public class ProvisionMojo extends AbstractVBoxMojo {

	@Override
	protected void execute(URI src) throws Exception {

		final String name = getName(src);

		getLog().info("provisioning '" + name + "'");
		exec("vboxmanage", "startvm", name);

		final VirtualBox cfg = getCfg(src);

		for (VirtualBox.Provisions.Provision.PortForward o : cfg.getProvisions().getProvision().getPortForward()) {
			final int hostPort = o.getHostport();
			final int guestPort = o.getGuestport();
			getLog().info("adding port forward " + hostPort + " -> " + guestPort);
			exec("vboxmanage", "setextradata", name, "VBoxInternal/Devices/e1000/0/LUN#0/Config/" + guestPort + "/HostPort", String.valueOf(hostPort));
			exec("vboxmanage", "setextradata", name, "VBoxInternal/Devices/e1000/0/LUN#0/Config/" + guestPort + "/GuestPort", String.valueOf(guestPort));
			exec("vboxmanage", "setextradata", name, "VBoxInternal/Devices/e1000/0/LUN#0/Config/" + guestPort + "/Protocol", "TCP");
		}


		getLog().info("hammering 'Return'");
		cfg.getProvisions().getProvision().getHammerReturn();

		for (int i = 0; i < 10; i++) {
			getLog().debug("Putting 'Return' into keyboard buffer");
			exec("vboxmanage", "controlvm", name, "keyboardputscancode", "1c");
			Thread.sleep(5000);
		}
	}
}
