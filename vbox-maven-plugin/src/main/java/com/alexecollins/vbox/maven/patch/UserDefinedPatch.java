package com.alexecollins.vbox.maven.patch;

import com.alexecollins.vbox.core.VBox;
import com.alexecollins.vbox.core.patch.Patch;
import com.alexecollins.vbox.core.patch.UnifiedPatch;
import org.apache.commons.io.FileUtils;

import java.io.File;

/**
 * A user-defined patch from a file.
 *
 * @author alexec (alex.e.c@gmail.com)
 * @since 2.0.0
 */
public class UserDefinedPatch implements Patch {
	private File file;
	private int level = 1;

	public void apply(VBox box) throws Exception {
		new UnifiedPatch(FileUtils.readFileToByteArray(file), level).apply(box);
	}

	public String getName() {
		return "UserDefinedPatch {" + file + "}";
	}
}
