package com.alexecollins.maven.plugins.vbox;

import com.alexecollins.maven.plugins.vbox.schema.Provisions;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ArrayUtils;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.ResourceHandler;
import org.mortbay.resource.Resource;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author alexec (alex.e.c@gmail.com)
 * @goal provision
 * @phase pre-integration-test
 */
public class ProvisionMojo extends AbstractVBoxesMojo {
	private final int port = 10350;
	private final Server server = new Server(port);

	@Override
	protected void execute(URI src) throws Exception {

		final String name = getName(src);
		final Snapshot snapshot = Snapshot.POST_PROVISIONING;
		if (exists(name) && getSnapshots(name).contains(snapshot.toString())) {
			exec("vboxmanage", "snapshot", name, "restore", snapshot.toString());
			return;
		}

		getLog().info("provisioning '" + name + "'");
		exec("vboxmanage", "startvm", name);

		for (String f : getManifest(src).getFile()) {
			final File d = new File(getTarget(getName(src)), f);
			if (!d.exists())
				FileUtils.copyURLToFile(new URL(src.toString() + "/" + f), d);
		}

		startServer(src);

		for (Object o : getProvisions(src).getPortForwardOrKeyboardPutScanCodesOrSleep()) {
			if (o instanceof Provisions.PortForward)
				portForward(name, (Provisions.PortForward) o);
			else if (o instanceof Provisions.KeyboardPutScanCodes)
				keyboardPutScanCodes(name, ((Provisions.KeyboardPutScanCodes) o));
			else if (o instanceof Provisions.Sleep) {
				getLog().info("sleeping for " + ((Provisions.Sleep) o).getMs() + "ms");
				Thread.sleep(((Provisions.Sleep) o).getMs());
			} else
				throw new AssertionError();
		}


		exec("vboxmanage", "controlvm", name, "acpipowerbutton");

		awaitPowerOff(name);

		exec("vboxmanage", "snapshot", name, "take", snapshot.toString());

		stopServer();
	}

	void stopServer() throws Exception {
		getLog().info("stopping server");
		server.stop();
	}

	void startServer(URI src) throws Exception {
		getLog().info("starting server on port " + port);

		final ResourceHandler rh = new ResourceHandler();
		final File resource = new File(outputDirectory + "/vbox/boxes/" + getName(src));
		getLog().debug("resource " + resource);
		assert resource.exists();
		rh.setBaseResource(Resource.newResource(resource.toURI().toURL()));
		server.setHandler(rh);
		server.start();

		final URL u = new URL("http://" + InetAddress.getLocalHost().getHostAddress() + ":" + port);
		getLog().info("testing server by getting " + u);
		final HttpURLConnection c = (HttpURLConnection) u.openConnection();
		c.connect();
		if (403 != c.getResponseCode()) throw new IllegalStateException(c.getResponseMessage());
		c.disconnect();

	}

	private void keyboardPutScanCodes(String name, Provisions.KeyboardPutScanCodes ksc) throws IOException, InterruptedException {

		final String keys = ksc.getKeys();
		if (keys != null) {
			getLog().info("typing keys " + keys);
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

			getLog().info("typing line " + line);


			keyboardPutScanCodes(name, ArrayUtils.addAll(ScanCodes.forString(line), ScanCodes.forKey("Enter")));
		}

	}

	private void keyboardPutScanCodes(String name, int[] scancodes) throws IOException, InterruptedException {
		getLog().debug("typing " + Arrays.toString(scancodes));


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

	private void portForward(String name, Provisions.PortForward pf) throws IOException, InterruptedException {
		final int hostPort = pf.getHostport();
		final int guestPort = pf.getGuestport();
		getLog().info("adding port forward hostport=" + hostPort + " guestport=" + guestPort);
		exec("vboxmanage", "setextradata", name, "VBoxInternal/Devices/e1000/0/LUN#0/Config/" + guestPort + "/HostPort", String.valueOf(hostPort));
		exec("vboxmanage", "setextradata", name, "VBoxInternal/Devices/e1000/0/LUN#0/Config/" + guestPort + "/GuestPort", String.valueOf(guestPort));
		exec("vboxmanage", "setextradata", name, "VBoxInternal/Devices/e1000/0/LUN#0/Config/" + guestPort + "/Protocol", "TCP");
	}
}