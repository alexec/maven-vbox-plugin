package com.alexecollins.util;

import com.google.common.io.Files;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class PatchUtilsTest {
	@Test
	public void testFileDiff() throws Exception {
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

	@Test
	public void testDirDiff() throws Exception {
		final File tmp = Files.createTempDir();

		final File a = new File(tmp, "a");
		{
			assert a.mkdir();
			Files.touch(new File(a, "touch-only-in-a.0"));
			FileUtils.writeStringToFile(new File(a, "changed"), "line 1\nline 2");
			final File subDir = new File(a, "sub-dir");
			assert subDir.mkdir();
			FileUtils.touch(new File(subDir, "touch-in-a.1"));
			assert new File(a, "dir-only-in-a.2").mkdir();
		}

		final File b = new File(tmp, "b");
		{
			assert b.mkdir();
			Files.touch(new File(b, "touch-only-in-b.0"));
			FileUtils.writeStringToFile(new File(b, "changed"), "line 1\nline 2\nline 3");
			final File subDir = new File(b, "sub-dir");
			assert subDir.mkdir();
			assert new File(subDir, "dir-only-in-b.1").mkdir();
			assert new File(b, "dir-only-in-b.2").mkdir();
		}

		final File patchOutput = new File(tmp, "test.patch");
		PatchUtils.create(a, b, patchOutput);

		System.out.println(FileUtils.readFileToString(patchOutput, "UTF-8"));

		assertTrue(patchOutput.exists());
		PatchUtils.apply(a, a, patchOutput);

		System.out.println("diff - ");
		try {
			ExecUtils.exec("diff", "-ruN", a.getPath(), b.getPath());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			// nop
		}

		assertArrayEquals(FileUtils2.getSignature(a), FileUtils2.getSignature(b));
	}
}
