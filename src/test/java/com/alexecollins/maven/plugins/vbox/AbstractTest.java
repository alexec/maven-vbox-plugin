package com.alexecollins.maven.plugins.vbox;

import org.junit.runners.Parameterized;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;

/**
 * @author alex.e.c@gmail.com
 */
public abstract class AbstractTest {

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][]{
				{"CentOS_6_3"} //,
				//{"UbuntuServer_12_10" },
				//{"WindowsServer2008" }
		});
	}


	public final String name;
	final File src;
	final File target;

	public AbstractTest(final String name) {
		this.name = name;
		System.out.println("name=" + name);
		src = new File("src/main/vbox/" + name);
		target = new File("target/vbox/boxes/" + name);
	}
}
