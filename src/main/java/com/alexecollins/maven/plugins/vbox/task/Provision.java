package com.alexecollins.maven.plugins.vbox.task;

import au.com.bytecode.opencsv.CSVReader;
import com.alexecollins.maven.plugins.vbox.Invokable;
import com.alexecollins.maven.plugins.vbox.ScanCodes;
import com.alexecollins.maven.plugins.vbox.Snapshot;
import com.alexecollins.maven.plugins.vbox.VBox;
import com.alexecollins.maven.plugins.vbox.provisioning.Provisioning;
import com.alexecollins.maven.plugins.vbox.util.DurationUtils;
import com.alexecollins.maven.plugins.vbox.util.ExecUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ArrayUtils;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.ResourceHandler;
import org.mortbay.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.*;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class Provision implements Invokable{
	private static final Logger LOGGER = LoggerFactory.getLogger(Provision.class);
	private final Server server;
	private final VBox box;
	private final Set<String> targets;


	public Provision(VBox box, Set<String> targets) throws IOException {
		this.box = box;
		this.targets = targets;

		server = new Server(Provision.findFreePort());
	}

	public void invoke() throws Exception {

		final Snapshot snapshot = Snapshot.POST_PROVISIONING;
		if (box.exists() && box.getSnapshots().contains(snapshot.toString())) {
			box.restoreSnapshot(Snapshot.POST_PROVISIONING);
			return;
		}

		LOGGER.info("provisioning '" + box.getName() + "'");
		for (String f : box.getManifest().getFile()) {
			final File d = new File(box.getTarget(), f);
			if (!d.exists())
				FileUtils.copyURLToFile(new URL(box.getSrc() + "/" + f), d);
		}

		startServer();
		try {
			final List<Provisioning.Target> targets = box.getProvisioning().getTarget();
			for (Provisioning.Target target : targets) {
				if (this.targets.contains(target.getName()) || this.targets.equals(Collections.singletonList("*"))) {
					LOGGER.info("executing target " + target.getName());
					if (target.equals(targets.get(0)) && !box.getProperties().getProperty("VMState").equals("running")) {
						LOGGER.info("starting box");
						box.start();
					}
					executeTarget(box, target);
					if (target.equals(targets.get(targets.size() - 1))) {
						if (box.getProperties().getProperty("VMState").equals("running")) {
							LOGGER.info("stopping box");
							box.pressPowerButton();
							box.awaitState((long) 10000, "poweroff");
						}
						box.takeSnapshot(snapshot);
					}
				} else {
					LOGGER.info("skipping target " + target.getName());
				}
			}
		} finally {
			stopServer();
		}
	}

	private void executeTarget(final VBox box, final Provisioning.Target stage) throws IOException, InterruptedException, TimeoutException, ExecutionException {
		for (Object o : stage.getPortForwardOrAwaitPortOrAwaitState()) {
			if (o instanceof Provisioning.Target.PortForward)
				portForward(box.getName(), (Provisioning.Target.PortForward) o);
			else if (o instanceof Provisioning.Target.KeyboardPutScanCodes)
				keyboardPutScanCodes(box.getName(), ((Provisioning.Target.KeyboardPutScanCodes) o));
			else if (o instanceof Provisioning.Target.Sleep) {
				final Provisioning.Target.Sleep s = (Provisioning.Target.Sleep) o;
				final long seconds = s.getMinutes() * 60 + s.getSeconds();

				LOGGER.info("sleeping for " + seconds + " second(s)");
				Thread.sleep(seconds * 1000);
			} else if (o instanceof Provisioning.Target.Exec) {
				try {
					ExecUtils.exec(new CSVReader(new StringReader(formatConfig(box.getName(), ((Provisioning.Target.Exec) o).getValue())), ' ').readNext());
				} catch (ExecutionException e) {
					if (((Provisioning.Target.Exec) o).isFailonerror())
						throw e;
					else
						LOGGER.info("ignoring error " + e.getMessage());
				}
			} else if (o instanceof Provisioning.Target.AwaitPort) {
				awaitPort((Provisioning.Target.AwaitPort) o);
			} else if (o instanceof Provisioning.Target.AwaitState) {
				box.awaitState(1000 * DurationUtils.secondsForString(((Provisioning.Target.AwaitState) o).getTimeout()), ((Provisioning.Target.AwaitState) o).getState());
			} else
				throw new AssertionError("unexpected provision");
		}
	}

	private void awaitPort(final Provisioning.Target.AwaitPort ap) throws IOException, TimeoutException, InterruptedException {
		final long start = System.currentTimeMillis();

		while (true) {
			LOGGER.info("awaiting port localhost:" + ap.getHostport());
			try {
				new Socket("localhost", ap.getHostport()).close();
				return;
			} catch (ConnectException e) {
				// nop
			}

			if (System.currentTimeMillis() > start + DurationUtils.secondsForString(ap.getTimeout())) {
				throw new TimeoutException("timed out waiting for port localhost:" + ap.getHostport());
			}

			Thread.sleep(30000);
		}
	}

	void stopServer() throws Exception {
		if (server.isRunning()) {
			LOGGER.info("stopping local web server");
			server.stop();
		}
	}

	void startServer() throws Exception {
		LOGGER.info("starting local web server on port " + getServerPort());

		final ResourceHandler rh = new ResourceHandler();
		final File resource = new File("target/vbox/boxes/" + box.getName());
		LOGGER.debug("resource " + resource);
		assert resource.exists();
		rh.setBaseResource(Resource.newResource(resource.toURI().toURL()));
		server.setHandler(rh);
		server.start();

		final URL u = new URL("http://" + InetAddress.getLocalHost().getHostAddress() + ":" + getServerPort());
		LOGGER.info("testing server by getting " + u);
		final HttpURLConnection c = (HttpURLConnection) u.openConnection();
		c.connect();
		if (403 != c.getResponseCode()) throw new IllegalStateException(c.getResponseMessage());
		c.disconnect();

	}

	private static int findFreePort() throws IOException {
		final ServerSocket server = new ServerSocket(0);
		final int port = server.getLocalPort();
		server.close();
		return port;
	}

	public void keyboardPutScanCodes(String name, Provisioning.Target.KeyboardPutScanCodes ksc) throws IOException, InterruptedException, ExecutionException {

		{
			final String keys = ksc.getKeys();
			if (keys != null) {
				LOGGER.info("typing keys " + keys);
				final List<Integer> sc = new ArrayList<Integer>();
				for (String key : keys.split(",")) {
					for (int c : ScanCodes.forKey(key)) {
						sc.add(c);
					}
				}
				keyboardPutScanCodes(name, ArrayUtils.toPrimitive(sc.toArray(new Integer[sc.size()])));
			}
		}
		{
			String line;
			line = ksc.getLine();
			if (line != null) {
				line = formatConfig(name, line);

				LOGGER.info("typing line '" + line + "'");

				keyboardPutScanCodes(name, ArrayUtils.addAll(ScanCodes.forString(line), ScanCodes.forKey("Enter")));
			}
		}

		{
			String text = ksc.getValue();
			if (text != null && text.length() > 0) {
				text = formatConfig(name, text);

				LOGGER.info("typing text '" + text + "'");

				keyboardPutScanCodes(name, ArrayUtils.addAll(ScanCodes.forString(text), ScanCodes.forKey("Enter")));
			}
		}
	}

	public String formatConfig(final String name, String line) throws IOException, InterruptedException, ExecutionException {
		line = line.replaceAll("%IP%", InetAddress.getLocalHost().getHostAddress());
		line = line.replaceAll("%PORT%", String.valueOf(getServerPort()));
		line = line.replaceAll("%VBOX_ADDITIONS%", VBox.findGuestAdditions().getPath().replaceAll("\\\\", "/"));
		line = line.replaceAll("%NAME%", name);
		return line;
	}

	private void keyboardPutScanCodes(String name, int[] scancodes) throws IOException, InterruptedException, ExecutionException {
		LOGGER.debug("typing " + Arrays.toString(scancodes));

		while (scancodes.length > 0) {
			final List<String> command = new ArrayList<String>();
			command.addAll(Arrays.asList("vboxmanage", "controlvm", name, "keyboardputscancode"));

			int i = 0;
			for (int scancode : scancodes) {
				command.add((scancode > 0xf ? "" : "0") + Integer.toHexString(scancode));
				i++;
				// split on enter
				if (i >= 16 || scancode == 156) {
					break;
				}
			}
			ExecUtils.exec(command.toArray(new String[command.size()]));
			Thread.sleep(scancodes[i - 1] == 156 ? 2000 : 100); //  a short sleep to let the OS digest
			scancodes = ArrayUtils.subarray(scancodes, i, scancodes.length);
		}
	}

	private void portForward(String name, Provisioning.Target.PortForward pf) throws IOException, InterruptedException, ExecutionException {
		final int hostPort = pf.getHostport();
		final int guestPort = pf.getGuestport();
		LOGGER.info("adding port forward hostport=" + hostPort + " guestport=" + guestPort);
		ExecUtils.exec("vboxmanage", "setextradata", name, "VBoxInternal/Devices/e1000/0/LUN#0/Config/" + guestPort + "/HostPort", String.valueOf(hostPort));
		ExecUtils.exec("vboxmanage", "setextradata", name, "VBoxInternal/Devices/e1000/0/LUN#0/Config/" + guestPort + "/GuestPort", String.valueOf(guestPort));
		ExecUtils.exec("vboxmanage", "setextradata", name, "VBoxInternal/Devices/e1000/0/LUN#0/Config/" + guestPort + "/Protocol", "TCP");
	}

	public int getServerPort() {
		return server.getConnectors()[0].getPort();
	}
}