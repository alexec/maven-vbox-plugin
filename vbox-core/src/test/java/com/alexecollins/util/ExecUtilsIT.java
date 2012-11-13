package com.alexecollins.util;

import org.junit.Test;

/**
 * @author alex.collins
 */
public class ExecUtilsIT {
	@Test
	public void testExec() throws Exception {
		ExecUtils.exec("vboxmanage", "list", "ostypes");
	}
}
