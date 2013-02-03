package com.alexecollins.vbox.core.patch;

import com.alexecollins.util.ExecUtils;
import com.alexecollins.vbox.core.VBox;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

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
 * @since 2.0.0
 */
public class UnifiedPatch implements Patch {
	private byte[] patch;
	private int level; // you'll almost always want to set this to 1

	public UnifiedPatch(byte[] patch, int level) {
		if (patch == null) {throw new IllegalArgumentException();}
		this.patch = patch;
		this.level = level;
	}

	public void apply(VBox definition) throws InterruptedException, ExecutionException, IOException {
		if (definition == null) {throw new IllegalArgumentException();}
		if (patch == null) {throw new IllegalStateException();}

		ExecUtils.exec(patch, new File(definition.getSrc().toURL().getFile()), "patch",  "-f",  "-p", String.valueOf(level));
	}

	public String getName() {
		return "UnifiedPatch";
	}


}
