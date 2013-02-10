package com.alexecollins.vbox.core.task;

import com.alexecollins.vbox.core.VBox;
import com.alexecollins.vbox.core.Work;
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

	@Parameterized.Parameters(name = "{0}")
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][]{
				{"TinyCore_4_x"},{"UbuntuServer_12_10" },{"CentOS_6_3"},{"WindowsServer2008" }
		});
	}

	final String name;
	protected final File src;
	final VBox box;
	final File target;
	final Work work = new Work(new File("target"));

	protected AbstractTest(final String name) throws IOException, JAXBException, SAXException, URISyntaxException {
		this.name = name;
		System.out.println("name=" + name);
		src = new File("src/main/vbox/" + name);
		target = new File(work.getBaseDir(), "vbox/boxes/" + name);
		box = new VBox(src.toURI());
	}
}
