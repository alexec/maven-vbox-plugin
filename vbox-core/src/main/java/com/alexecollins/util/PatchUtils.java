package com.alexecollins.util;

import name.fraser.neil.plaintext.diff_match_patch;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A facade/adapter.
 *
 * @author alexec (alex.e.c@gmail.com)
 */
public class PatchUtils {

	private static final diff_match_patch p = new diff_match_patch();

	private PatchUtils() {}

	public static void create(File a, File b, File patch) throws IOException {
		checkArgument(a != null && a.exists(), a + " exists");
		checkArgument(b != null && b.exists(), b + " exists");
		checkNotNull(patch, "patch is not null");
		FileUtils.writeStringToFile(patch, create(a, b), "UTF-8");
	}

	private static String create(File a, File b) throws IOException {

		if (a.isFile()) {
			return p.patch_toText(p.patch_make(FileUtils.readFileToString(a, "UTF-8"), FileUtils.readFileToString(b, "UTF-8")));
		}   else      {
			throw new UnsupportedOperationException();
		}
	}

	public static void apply(File a, File b, File patch) throws IOException {
		checkArgument(a != null && a.exists(), b + " exists");
		checkNotNull(b, "b exists");
		checkArgument(patch != null && patch.isFile(), patch + " exists and is file");
		FileUtils.writeStringToFile(b, apply(a, patch), "UTF-8");
	}

	private static String apply(File a, File patch) throws IOException {
		if (!a.isFile()) {
			throw new UnsupportedOperationException();
		}

		final Object[] objects = p.patch_apply(p.patch_fromText(FileUtils.readFileToString(patch, "UTF-8")),
				FileUtils.readFileToString(a, "UTF-8"));

		assert objects.length == 2;

		for (boolean b1 : (boolean[]) (objects[1])) {
			if (!b1) {
				throw new IllegalStateException("failed to apply text");
			}
		}

		return String.valueOf(objects[0]);
	}
}
