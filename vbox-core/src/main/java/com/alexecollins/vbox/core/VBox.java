package com.alexecollins.vbox.core;

import com.alexecollins.util.DurationUtils;
import com.alexecollins.util.ExecUtils;
import com.alexecollins.util.FileUtils2;
import com.alexecollins.vbox.manifest.Manifest;
import com.alexecollins.vbox.mediaregistry.MediaRegistry;
import com.alexecollins.vbox.profile.Profile;
import com.alexecollins.vbox.provisioning.Provisioning;
import com.google.common.annotations.VisibleForTesting;
import de.innotek.virtualbox_settings.VirtualBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author alex.e.c@gmail.com
 */
public class VBox {
	private static final Logger LOGGER = LoggerFactory.getLogger(VBox.class);
	private final VirtualBox virtualBox;
	private final MediaRegistry mediaRegistry;
	private final Manifest manifest;
	private final Provisioning provisioning;
	private final URI src;
	private final String name;
	private final Profile profile;

	public VBox(final URI src) throws URISyntaxException, IOException, JAXBException, SAXException {

		if (src == null) {
			throw new IllegalArgumentException("src is null");
		}

		this.src = src;

		final String p = src.toString();

		if (p == null) {
			throw new IllegalStateException(src + " has null path");
		}

		final String q = p.endsWith("/") ? p.substring(0, p.length() - 1) : p;
		name = q.substring(q.lastIndexOf('/') + 1);

		virtualBox =  unmarshal(new URI(src.toString() + "/VirtualBox.xml").toURL().openStream(), VirtualBox.class);
		mediaRegistry = unmarshal(new URI(src.toString() + "/MediaRegistry.xml").toURL().openStream(), MediaRegistry.class);
		manifest = unmarshal(new URI(src.toString() + "/Manifest.xml").toURL().openStream(), Manifest.class);
		provisioning = unmarshal(new URI(src.toString() + "/Provisioning.xml").toURL().openStream(), Provisioning.class);
		profile = unmarshal(new URI(src.toString() + "/Profile.xml").toURL().openStream(), Profile.class);
	}

	/**
	 * @return The name of the VM at the supplied URI.
	 */
	public String getName() {
		return name;
	}


	/**
	 * @return A collection of snapshot names.
	 */
	public Set<Snapshot> getSnapshots() throws IOException, InterruptedException, ExecutionException {
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
	public Properties getProperties() throws IOException, InterruptedException, ExecutionException {
		return getPropertiesFromString(ExecUtils.exec("vboxmanage", "showvminfo", name, "--machinereadable"));
	}

	@VisibleForTesting
	static Properties getPropertiesFromString(final String exec) {
		final Properties p = new Properties();
		final Matcher m = Pattern.compile("([^=\n]*)=\"([^\"]*)\"").matcher(exec);
		while (m.find()) {
			p.setProperty(m.group(1), m.group(2));
		}
		return p;
	}

	public void awaitState(final long millis, final String state) throws InterruptedException, IOException, TimeoutException, ExecutionException {
		long s = System.currentTimeMillis();
		do {
            long remaining = s + millis - System.currentTimeMillis();
            LOGGER.info("awaiting " + state + " for " + DurationUtils.prettyPrint(remaining) );
			if (remaining < 0) {
				throw new TimeoutException("failed to see " + state + " in " + millis + "ms");
			}
			Thread.sleep(Math.min(10000l, remaining));
		} while (!getProperties().get("VMState").equals(state));

		LOGGER.info("in state " + state);
	}

    public VirtualBox getVirtualBox() {
		return virtualBox;
	}

	public MediaRegistry getMediaRegistry() {
		return mediaRegistry;
	}

	public Manifest getManifest() {
		return manifest;
	}

	public Profile getProfile() {
		return profile;
	}

	@SuppressWarnings("unchecked")
	private <T> T unmarshal(final InputStream in, final Class<T> type) throws JAXBException, SAXException {
		final Unmarshaller u = JAXBContext.newInstance(type).createUnmarshaller();
		u.setSchema(SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(getClass().getResource("/" + type.getSimpleName() + ".xsd")));
		return (T) u.unmarshal(in);
	}

	public Provisioning getProvisioning()  {
		return provisioning;
	}


	/**
	 * @return A collection of all DVDs.
	 */
	protected static Set<File> getDvds() throws IOException, InterruptedException, ExecutionException {
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
	public static File findGuestAdditions() throws IOException, InterruptedException, ExecutionException {

		for (String c : new String[]{
				"C:\\Program Files\\Oracle\\VirtualBox\\VBoxGuestAdditions.iso",
				"/Applications/VirtualBox.app/Contents/MacOS/VBoxGuestAdditions.iso"
		}) {
			final File f = new File(c);
			if (f.exists()) {
				return f;
			}
		}

		final Matcher m = Pattern.compile("Location: *(.*VBoxGuestAdditions\\.iso)").matcher(ExecUtils.exec("vboxmanage", "list", "dvds"));
		if (m.find()) return new File(m.group(1));

		return null;
	}

    /**
     * @return A set of all the host networks.
     */
    public static Set<String> findHostOnlyInterfaces() throws InterruptedException, ExecutionException, IOException {
        return parseHostOnlyIfs(ExecUtils.exec("vboxmanage", "list", "hostonlyifs"));
    }

    @VisibleForTesting static Set<String> parseHostOnlyIfs(String s) {
        final Matcher m = Pattern.compile("^Name: *(.*)").matcher(s);
        final Set<String> names = new HashSet<String>();
        while (m.find()) {
            names.add(m.group(1));
        }
        return names;
    }


	public static void installAdditions(File work) throws IOException, InterruptedException, ExecutionException {

		if (VBox.findGuestAdditions() != null) return;

		final Version v = getVersion();

		final String a = v.major + "." + v.minor + "." + v.build;
		final String b = a + "-" + v.revision;

        final String f = "Oracle_VM_VirtualBox_Extension_Pack-" + b + ".vbox-extpack";
        final File file = new File(work, "vbox/downloads/" + f);
        // http://download.virtualbox.org/virtualbox/4.2.6/Oracle_VM_VirtualBox_Extension_Pack-4.2.6-82870.vbox-extpack
		FileUtils2.copyURLToFile(new URL("http://download.virtualbox.org/virtualbox/" + a + "/" + f), file);

		ExecUtils.exec("vboxmanage", "extpack", "install", file.getCanonicalPath());
	}

	public static class Version {
		final int major;
		final int minor;
		final int build;
		final int revision;

		Version(final String x) {
			final Matcher m = Pattern.compile("^([0-9]+)\\.([0-9]+)\\.([0-9]+)r([0-9]+)$").matcher(x);
			if (!m.find()) throw new IllegalArgumentException();
			major = Integer.parseInt(m.group(1));
			minor = Integer.parseInt(m.group(2));
			build = Integer.parseInt(m.group(3));
			revision = Integer.parseInt(m.group(4));
		}

        @Override
        public String toString() {
            return major + "." + minor + "." + build + "r" + revision;
        }
    }

    /**
     * @return  The version.
     */
	public static Version getVersion() throws IOException, ExecutionException, InterruptedException {
		return new Version(ExecUtils.exec("vboxmanage", "-v").trim());
	}

	public URI getSrc() {
		return src;
	}

	public void powerOff() throws IOException, InterruptedException, ExecutionException {
		if (!getProperties().getProperty("VMState").equals("poweroff")) {
			ExecUtils.exec("vboxmanage", "controlvm", name, "poweroff");
		}
	}

	public void unregister() throws IOException, InterruptedException, ExecutionException, TimeoutException {
		if (exists()) {
			if (getProperties().getProperty("VMState").equals("running")) {
				powerOff();
				awaitState(10000l, "poweroff");
			}
			ExecUtils.exec("vboxmanage", "unregistervm", name, "--delete");
		}
	}

	public void restoreSnapshot(final Snapshot snapshot) throws IOException, InterruptedException, ExecutionException {
		ExecUtils.exec("vboxmanage", "snapshot", name, "restore", snapshot.toString());
	}

	/**
	 * Start the box, but do not wait for it to complete start-up.
	 */
	public void start() throws IOException, InterruptedException, ExecutionException, TimeoutException, URISyntaxException {
		ExecUtils.exec("vboxmanage", "startvm", getName(), "--type", String.valueOf(getProfile().getType()));
	}

	public void takeSnapshot(final Snapshot snapshot) throws IOException, InterruptedException, ExecutionException {
		ExecUtils.exec("vboxmanage", "snapshot", name, "take", snapshot.toString());
	}

	public void pressPowerButton() throws IOException, InterruptedException, ExecutionException {
		ExecUtils.exec("vboxmanage", "controlvm", name, "acpipowerbutton");
	}

	public boolean exists() throws IOException, ExecutionException, InterruptedException {
		return parseVms(ExecUtils.exec("vboxmanage", "list", "vms")).contains(name);
	}

	@VisibleForTesting
	static Set<String> parseVms(String exec) {

		final Matcher m = Pattern.compile("\"(.*)\"").matcher(exec);
		final Set<String> names = new HashSet<String>();
		while (m.find()) {
			names.add(m.group(1));
		}
		return names;
	}

	public static class OSType {
		private final String name;

		public OSType(final String name) {
			this.name = name;
		}

		@Override
		public boolean equals(final Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			OSType osType = (OSType) o;

			if (name != null ? !name.equals(osType.name) : osType.name != null) return false;

			return true;
		}

		@Override
		public int hashCode() {
			return name != null ? name.hashCode() : 0;
		}
	}

	public static Set<OSType> getOSTypes() throws IOException, ExecutionException, InterruptedException {
		return parseOSTypes(ExecUtils.exec("vboxmanage", "list", "ostypes"));
	}

	@VisibleForTesting
	static Set<OSType> parseOSTypes(final String exec) {
		final Set<OSType> x = new HashSet<OSType>();
		final Matcher m = Pattern.compile("ID:  *(.*)$", Pattern.MULTILINE).matcher(exec);
		while (m.find()) {
			x.add(new OSType(m.group(1)));
		}

		return x;
	}

}
