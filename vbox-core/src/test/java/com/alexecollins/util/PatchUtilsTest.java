package com.alexecollins.util;

import com.google.common.io.Files;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;

import static junit.framework.Assert.*;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class PatchUtilsTest {
	@Test
	public void testDiff() throws Exception {
		final File tmp = Files.createTempDir();
		final File a = new File(tmp, "a");
		FileUtils.write(a, "a\nfile\nline");
		final File b = new File(tmp, "b");
		FileUtils.write(b, "b\nfile\nalex\nnothing");
		final File patchOutput = new File(tmp, "test.patch");
		PatchUtils.create(a, b, patchOutput);
		assertTrue(patchOutput.exists());
		PatchUtils.apply(a, a, patchOutput);
		assertEquals(FileUtils.readFileToString(a), FileUtils.readFileToString(b));
	}
}
