package com.alexecollins.vbox.core.task;

import com.alexecollins.vbox.core.VBox;
import org.junit.runners.Parameterized;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;

/**
 * @author alex.e.c@gmail.com
 */
public abstract class AbstractTest {

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][]{
				{"UbuntuServer_12_10" },{"CentOS_6_3"},{"WindowsServer2008" }
		});
	}

	protected final String name;
	protected final File src;
	protected final VBox box;
	final File target;
	final File work = new File("target");

	public AbstractTest(final String name) throws IOException, JAXBException, SAXException, URISyntaxException {
		this.name = name;
		System.out.println("name=" + name);
		src = new File("src/main/vbox/" + name);
		target = new File(work, "vbox/boxes/" + name);
		box = new VBox(src.toURI());
	}
}
