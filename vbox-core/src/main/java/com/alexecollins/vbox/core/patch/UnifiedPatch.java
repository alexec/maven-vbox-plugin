package com.alexecollins.vbox.core.patch;

import com.alexecollins.util.ExecUtils;
import com.alexecollins.vbox.core.VBox;
import com.alexecollins.vbox.core.patch.Patch;

import java.io.File;

/**
 * A patch based on the unified diff format.
 *
 * This is a very common patching algo and should suite almost all purposes.
 *
 * Typically you'd create a patch using:
 *
 * <code>
 *     diff -ruN app0 app1 > my-patch.patch
 * </code>
 *

 * @author alexec (alex.e.c@gmail.com)
 */
public class UnifiedPatch implements Patch {
	private File patchFile;

	public UnifiedPatch(File patchFile) {
		if (patchFile == null) {throw new IllegalArgumentException();}
		this.patchFile = patchFile;
	}

	public void apply(VBox box) throws Exception {
		if (box == null) {throw new IllegalArgumentException();}
		if (patchFile == null) {throw new IllegalStateException();}

		ExecUtils.exec("patch ");
	}
}
