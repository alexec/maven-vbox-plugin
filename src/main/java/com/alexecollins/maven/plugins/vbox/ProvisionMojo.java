package com.alexecollins.maven.plugins.vbox;

import com.alexecollins.maven.plugins.vbox.schema.VirtualBox;
import org.apache.commons.lang.ArrayUtils;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.ResourceHandler;
import org.mortbay.resource.Resource;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author alexec (alex.e.c@gmail.com)
 * @goal provision
 * @phase pre-integration-test
 */
public class ProvisionMojo extends AbstractVBoxMojo {
	private final int port = 10350;
	private final Server server = new Server(port);

	@Override
	protected void execute(URI src) throws Exception {

		final String name = getName(src);

		getLog().info("provisioning '" + name + "'");
		exec("vboxmanage", "startvm", name);

		startServer(src);

		final VirtualBox cfg = getCfg(src);

		for (Object o : cfg.getProvisions().getProvision().getPortForwardOrHammerReturnOrKeyboardPutScanCodes()) {
			if (o instanceof VirtualBox.Provisions.Provision.PortForward)
				portForward(name, (VirtualBox.Provisions.Provision.PortForward) o);
			else if (o instanceof VirtualBox.Provisions.Provision.HammerReturn)
				hammerReturn(name, (VirtualBox.Provisions.Provision.HammerReturn) o);
			else if (o instanceof VirtualBox.Provisions.Provision.KeyboardPutScanCodes)
				keyboardPutScanCodes(name, ((VirtualBox.Provisions.Provision.KeyboardPutScanCodes) o));
			else if (o instanceof VirtualBox.Provisions.Provision.Sleep) {
				getLog().info("sleeping for " + ((VirtualBox.Provisions.Provision.Sleep) o).getMs() + "ms");
				Thread.sleep(((VirtualBox.Provisions.Provision.Sleep) o).getMs());
			} else
				throw new AssertionError();
		}
		stopServer();

	}

	void stopServer() throws Exception {
		server.stop();
	}

	void startServer(URI src) throws Exception {
		final ResourceHandler rh = new ResourceHandler();
		rh.setBaseResource(Resource.newResource(src.toURL()));
		server.setHandler(rh);
		server.start();
	}

	private void keyboardPutScanCodes(String name, VirtualBox.Provisions.Provision.KeyboardPutScanCodes ksc) throws IOException, InterruptedException {

		final String keys = ksc.getKeys();
		if (keys != null) {
			final List<Integer> sc = new ArrayList<Integer>();
			for (String key : keys.split(",")) {
				for (int c : ScanCodes.forKey(key)) {
					sc.add(c);
				}
			}
			keyboardPutScanCodes(name, ArrayUtils.toPrimitive(sc.toArray(new Integer[sc.size()])));
		}
		String line = ksc.getLine();
		if (line != null) {
			line = line.replaceAll("%IP%", InetAddress.getLocalHost().getHostAddress());
			line = line.replaceAll("%PORT%", String.valueOf(port));

			getLog().info("putting " + line);


			keyboardPutScanCodes(name, ArrayUtils.addAll(ScanCodes.forString(line), ScanCodes.forKey("Enter")));
		}

	}

	private void hammerReturn(String name, VirtualBox.Provisions.Provision.HammerReturn hr) throws IOException, InterruptedException {
		getLog().info("hammering 'Return'");

		for (int i = 0; i < hr.getTimes(); i++) {
			keyboardPutScanCodes(name, new int[]{0x1c});
			Thread.sleep(5000);
		}
	}

	private void keyboardPutScanCodes(String name, int[] scancodes) throws IOException, InterruptedException {
		getLog().info("putting keyboard scancodes " + Arrays.toString(scancodes) + " into keyboard");


		while (scancodes.length > 0) {
			final List<String> command = new ArrayList<String>();
			command.addAll(Arrays.asList("vboxmanage", "controlvm", name, "keyboardputscancode"));

			int i = 0;
			for (int scancode : scancodes) {
				command.add((scancode > 0xf ? "" : "0") + Integer.toHexString(scancode));
				i++;
				if (i > 16) {
					break;
				}
			}
			exec(command.toArray(new String[command.size()]));
			scancodes = ArrayUtils.subarray(scancodes, i, scancodes.length);
		}

	}

	private void portForward(String name, VirtualBox.Provisions.Provision.PortForward pf) throws IOException, InterruptedException {
		final int hostPort = pf.getHostport();
		final int guestPort = pf.getGuestport();
		getLog().info("adding port forward hostport=" + hostPort + " guestport=" + guestPort);
		exec("vboxmanage", "setextradata", name, "VBoxInternal/Devices/e1000/0/LUN#0/Config/" + guestPort + "/HostPort", String.valueOf(hostPort));
		exec("vboxmanage", "setextradata", name, "VBoxInternal/Devices/e1000/0/LUN#0/Config/" + guestPort + "/GuestPort", String.valueOf(guestPort));
		exec("vboxmanage", "setextradata", name, "VBoxInternal/Devices/e1000/0/LUN#0/Config/" + guestPort + "/Protocol", "TCP");
	}
}