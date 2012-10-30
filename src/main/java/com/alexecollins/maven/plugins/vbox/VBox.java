package com.alexecollins.maven.plugins.vbox;

import com.alexecollins.maven.plugins.vbox.manifest.Manifest;
import com.alexecollins.maven.plugins.vbox.mediaregistry.MediaRegistry;
import com.alexecollins.maven.plugins.vbox.provisions.Provisions;
import de.innotek.virtualbox_settings.VirtualBox;
import org.apache.commons.io.FileUtils;

import javax.xml.bind.JAXB;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author alex.collins
 */
public class VBox {
	private final URI src;
	private final String name;

	public VBox(final URI src) {
		this.src = src;

		final String p = src.getPath();
		final String q = p.endsWith("/") ? p.substring(0, p.length() - 1) : p;
		name = q.substring(q.lastIndexOf('/') + 1);
	}

	/**
	 * @return The name of the VM at the supplied URI.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param outputDirectory Target.
	 * @return The target (aka work) directory for the box.
	 */
	protected File getTarget(final File outputDirectory) {
		return new File(outputDirectory, "vbox/boxes/" + name);
	}

	/**
	 * @param outputDirectory
	 * @return If the box exists.
	 */
	protected boolean exists(final File outputDirectory) {
		return getTarget(outputDirectory).exists();
	}


	/**
	 * @return A collection of snapshot names.
	 */
	protected Set<Snapshot> getSnapshots() throws IOException, InterruptedException {
		final Set<Snapshot> s = new HashSet<Snapshot>();
		final Properties p = getProperties();
		for (Object o : p.keySet()) {
			if (o.toString().startsWith("SnapshotName")) {
				s.add(Snapshot.valueOf(p.getProperty(o.toString())));
			}
		}
		return s;
	}

	/**
	 * @return The box's properties.
	 */
	protected Properties getProperties() throws IOException, InterruptedException {
		return getPropertiesFromString(ExecUtils.exec("vboxmanage", "showvminfo", name, "--machinereadable"));
	}

	static Properties getPropertiesFromString(final String exec) {
		final Properties p = new Properties();
		final Matcher m = Pattern.compile("([^=\n]*)=\"([^\"]*)\"").matcher(exec);
		while (m.find()) {
			p.setProperty(m.group(1), m.group(2));
		}
		return p;
	}

	protected void awaitPowerOff(long millis) throws InterruptedException, IOException {
		long s = System.currentTimeMillis();
		do {
			System.out.println("waiting for power off");
			Thread.sleep(3000);
			if (System.currentTimeMillis() > s + millis) {
				throw new IllegalStateException("failed to power off in " + millis + "ms");
			}
		} while (!getProperties().get("VMState").equals("poweroff"));
	}

	protected VirtualBox getVirtualBox() throws IOException, URISyntaxException {
		return JAXB.unmarshal(new URI(src.toString() + "/VirtualBox.xml").toURL().openStream(), VirtualBox.class);
	}

	protected MediaRegistry getMediaRegistry() throws IOException, URISyntaxException {
		return JAXB.unmarshal(new URI(src.toString() + "/MediaRegistry.xml").toURL().openStream(), MediaRegistry.class);
	}

	protected Manifest getManifest() throws IOException, URISyntaxException {
		return JAXB.unmarshal(new URI(src.toString() + "/Manifest.xml").toURL().openStream(), Manifest.class);
	}

	protected Provisions getProvisions() throws IOException, URISyntaxException {
		return JAXB.unmarshal(new URI(src.toString() + "/Provisions.xml").toURL().openStream(), Provisions.class);
	}


	/**
	 * @return A collection of all DVDs.
	 */
	protected static Set<File> getDvds() throws IOException, InterruptedException {
		final Matcher m = Pattern.compile("Location: *(.*VBoxGuestAdditions\\.iso)").matcher(ExecUtils.exec("vboxmanage", "list", "dvds"));
		final HashSet<File> files = new HashSet<File>();
		while (m.find()) {
			files.add(new File(m.group(1)));
		}
		return files;
	}


	/**
	 * @return The location of the guest additions, or null if not found.
	 */
	protected static File findGuestAdditions() throws IOException, InterruptedException {

		for (String c : new String[]{
				"C:\\Program Files\\Oracle\\VirtualBox\\VBoxGuestAdditions.iso"
		}) {
			System.out.println(c);
			final File f = new File(c);
			if (f.exists()) {
				return f;
			}
		}

		final Matcher m = Pattern.compile("Location: *(.*VBoxGuestAdditions\\.iso)").matcher(ExecUtils.exec("vboxmanage", "list", "dvds"));
		if (m.find()) return new File(m.group(1));

		return null;
	}


	public static void installAdditions() throws IOException, InterruptedException {

		if (VBox.findGuestAdditions() != null) return;

		final File file = new File("target/vbox/downloads/Oracle_VM_VirtualBox_Extension_Pack-4.1.22-80657.vbox-extpack");
		if (!file.exists()) {
			FileUtils.copyURLToFile(new URL("http://download.virtualbox.org/virtualbox/4.1.22/Oracle_VM_VirtualBox_Extension_Pack-4.1.22-80657.vbox-extpack"), file);
		}

		ExecUtils.exec("vboxmanage", "extpack", "install", file.getCanonicalPath());
	}

	public URI getSrc() {
		return src;
	}

	public void powerOff() throws IOException, InterruptedException {
		ExecUtils.exec("vboxmanage", "controlvm", name, "poweroff");
	}

	public void unregister() throws IOException, InterruptedException {
		ExecUtils.exec("vboxmanage", "unregistervm", name, "--delete");
	}

	public void restoreSnapshot(final Snapshot snapshot) throws IOException, InterruptedException {
		ExecUtils.exec("vboxmanage", "snapshot", name, "restore", snapshot.toString());
	}

	public void start() throws IOException, InterruptedException {
		ExecUtils.exec("vboxmanage", "startvm", name);
	}

	public void takeSnapshot(final Snapshot snapshot) throws IOException, InterruptedException {
		ExecUtils.exec("vboxmanage", "snapshot", name, "take", snapshot.toString());
	}

	public void pressPowerButton() throws IOException, InterruptedException {
		ExecUtils.exec("vboxmanage", "controlvm", name, "acpipowerbutton");
	}
}
