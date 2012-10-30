package com.alexecollins.maven.plugins.vbox;

import com.alexecollins.maven.plugins.vbox.provisions.Provisions;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ArrayUtils;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.ResourceHandler;
import org.mortbay.resource.Resource;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * @author alexec (alex.e.c@gmail.com)
 * @goal provision
 * @phase pre-integration-test
 */
public class ProvisionMojo extends AbstractVBoxesMojo {
	private final int port = 10350;
	private final Server server = new Server(port);

	@Override
	protected void execute(VBox box) throws Exception {

		final Snapshot snapshot = Snapshot.POST_PROVISIONING;
		if (box.exists(outputDirectory) && box.getSnapshots().contains(snapshot.toString())) {
			box.restoreSnapshot(Snapshot.POST_PROVISIONING);
			return;
		}

		getLog().info("provisioning '" + box.getName() + "'");
		box.start();

		for (String f : box.getManifest().getFile()) {
			final File d = new File(box.getTarget(outputDirectory), f);
			if (!d.exists())
				FileUtils.copyURLToFile(new URL(box.getSrc() + "/" + f), d);
		}

		startServer(box);
		try {
			for (Object o : box.getProvisions().getPortForwardOrAwaitPortOrKeyboardPutScanCodes()) {
				if (o instanceof Provisions.PortForward)
					portForward(box.getName(), (Provisions.PortForward) o);
				else if (o instanceof Provisions.KeyboardPutScanCodes)
					keyboardPutScanCodes(box.getName(), ((Provisions.KeyboardPutScanCodes) o));
				else if (o instanceof Provisions.Sleep) {
					getLog().info("sleeping for " + ((Provisions.Sleep) o).getMs() + "ms");
					Thread.sleep(((Provisions.Sleep) o).getMs());
				} else if (o instanceof Provisions.Exec) {
					ExecUtils.exec(formatConfig(box.getName(), ((Provisions.Exec) o).getValue()));
				} else if (o instanceof Provisions.AwaitPort) {
					awaitPort((Provisions.AwaitPort) o);
				} else
					throw new AssertionError("unexpected provision");
			}

			box.pressPowerButton();
			box.awaitPowerOff(10000);
			box.takeSnapshot(snapshot);
		} finally {
			stopServer();
		}
	}

	private void awaitPort(final Provisions.AwaitPort ap) throws IOException, TimeoutException, InterruptedException {
		final long start = System.currentTimeMillis();

		while (true) {
			getLog().info("awaiting port localhost:" + ap.getHostport());
			try {
				new Socket("localhost", ap.getHostport()).close();
				return;
			} catch (ConnectException e) {
				// nop
			}

			if (System.currentTimeMillis() > start + ap.getTimeout()) {
				throw new TimeoutException("timed out waiting for port localhost:" + ap.getHostport());
			}

			Thread.sleep(30000);
		}
	}

	void stopServer() throws Exception {
		getLog().info("stopping server");
		server.stop();
	}

	void startServer(VBox box) throws Exception {
		getLog().info("starting server on port " + port);

		final ResourceHandler rh = new ResourceHandler();
		final File resource = new File(outputDirectory + "/vbox/boxes/" + box.getName());
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
			line = formatConfig(name, line);

			getLog().info("typing line " + line);


			keyboardPutScanCodes(name, ArrayUtils.addAll(ScanCodes.forString(line), ScanCodes.forKey("Enter")));
		}
	}

	private String formatConfig(final String name, String line) throws IOException, InterruptedException {
		line = line.replaceAll("%IP%", InetAddress.getLocalHost().getHostAddress());
		line = line.replaceAll("%PORT%", String.valueOf(port));
		line = line.replaceAll("%VBOX_ADDITIONS%", VBox.findGuestAdditions().getCanonicalPath());
		line = line.replaceAll("%NAME%", name);
		return line;
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
			ExecUtils.exec(command.toArray(new String[command.size()]));
			scancodes = ArrayUtils.subarray(scancodes, i, scancodes.length);
		}

	}

	private void portForward(String name, Provisions.PortForward pf) throws IOException, InterruptedException {
		final int hostPort = pf.getHostport();
		final int guestPort = pf.getGuestport();
		getLog().info("adding port forward hostport=" + hostPort + " guestport=" + guestPort);
		ExecUtils.exec("vboxmanage", "setextradata", name, "VBoxInternal/Devices/e1000/0/LUN#0/Config/" + guestPort + "/HostPort", String.valueOf(hostPort));
		ExecUtils.exec("vboxmanage", "setextradata", name, "VBoxInternal/Devices/e1000/0/LUN#0/Config/" + guestPort + "/GuestPort", String.valueOf(guestPort));
		ExecUtils.exec("vboxmanage", "setextradata", name, "VBoxInternal/Devices/e1000/0/LUN#0/Config/" + guestPort + "/Protocol", "TCP");
	}
}