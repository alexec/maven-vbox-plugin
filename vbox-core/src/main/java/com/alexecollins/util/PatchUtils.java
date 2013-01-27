package com.alexecollins.util;

import name.fraser.neil.plaintext.diff_match_patch;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * A facade/adapter.
 *
 * @author alexec (alex.e.c@gmail.com)
 */
public class PatchUtils {

	private static final diff_match_patch p = new diff_match_patch();

	private PatchUtils() {}

	public static void create(File a, File b, File patch) throws IOException {
		FileUtils.writeStringToFile(patch, p.patch_toText(p.patch_make(FileUtils.readFileToString(a, "UTF-8"), FileUtils.readFileToString(b, "UTF-8"))), "UTF-8");
	}

	public static void apply(File a, File b, File patch) throws IOException {
		final Object[] objects = p.patch_apply(p.patch_fromText(FileUtils.readFileToString(patch, "UTF-8")),
				FileUtils.readFileToString(a, "UTF-8"));

		assert objects.length == 2;

		for (boolean b1 : (boolean[]) (objects[1])) {
			if (!b1) {
				throw new IllegalStateException("failed to apply text");
			}
		}


		FileUtils.writeStringToFile(b, String.valueOf(objects[0]), "UTF-8");
	}
}
