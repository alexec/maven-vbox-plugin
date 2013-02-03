package com.alexecollins.util;

import org.junit.Test;

import java.io.File;

import static junit.framework.Assert.assertEquals;

/**
 * @author alex.collins
 */
public class ExecUtilsIT {
	@Test
	public void testExec() throws Exception {
		ExecUtils.exec("vboxmanage", "list", "ostypes");
	}

	@Test
	public void testExec1() throws Exception {
		assertEquals("hello\n", ExecUtils.exec("hello".getBytes(), new File("."), "cat"));
	}
}
