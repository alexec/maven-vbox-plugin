package com.alexecollins.vbox.core.task;

import com.alexecollins.vbox.core.VBox;
import com.alexecollins.vbox.core.Work;
import org.junit.runner.RunWith;
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
@RunWith(Parameterized.class)
public abstract class AbstractTest {

	@Parameterized.Parameters(name = "{0}")
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][]{
				{"UbuntuServer_12_10" },
				{"TinyCore_4_x"},
				{"CentOS_6_3"}
				// ,{"WindowsServer2008" }
		});
	}

	private final String name;
	private final File src;
	private final File target;
	private final Work work = new Work(new File("target"), new File(System.getProperty("user.home"), ".vbox"));

	public AbstractTest(final String name) throws Exception {
		this.name = name;
		System.out.println("name=" + name);
		target = new File(work.getBaseDir(), "vbox/boxes/" + name);
		src = new File(System.getProperty("java.io.tmpdir"), getName());
		final CreateDefinition definition = new CreateDefinition(getName(), src);
		definition.call();
	}

	public String getName() {
		return name;
	}

	public VBox getBox() throws IOException, JAXBException, SAXException, URISyntaxException {
		return new VBox(src.toURI());
	}

	public File getTarget() {
		return target;
	}

	public Work getWork() {
		return work;
	}

	public File getSrc() {
		return src;
	}
}
