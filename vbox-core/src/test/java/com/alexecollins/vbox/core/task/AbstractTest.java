package com.alexecollins.vbox.core.task;

import com.alexecollins.vbox.core.Context;
import com.alexecollins.vbox.core.TestContext;
import com.alexecollins.vbox.core.VBox;
import com.alexecollins.vbox.core.Work;
import com.google.common.io.Files;
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
				{"TinyCore_4_x"},
				{"UbuntuServer_12_10" },
				{"CentOS_6_3"}
				// ,{"WindowsServer2008" }
		});
	}

	private final String name;
	private final File src;
	private final File target;
	private final Context context = new TestContext();
	private final Work work = new Work(context);

	public AbstractTest(final String name) throws Exception {
		this.name = name;
		System.out.println("name=" + name);
		src = Files.createTempDir();
		final CreateDefinition definition = new CreateDefinition(context, getName(), src);
		definition.call();
		target = work.targetOf(getBox());
	}

	public String getName() {
		return name;
	}

	public VBox getBox() throws IOException, JAXBException, SAXException, URISyntaxException {
		return new VBox(context, src.toURI());
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
