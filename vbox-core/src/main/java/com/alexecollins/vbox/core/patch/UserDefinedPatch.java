package com.alexecollins.vbox.core.patch;

import com.alexecollins.vbox.core.VBox;
import org.apache.commons.io.FileUtils;

import java.io.File;

/**
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

	public void setFile(File file) {
		this.file = file;
	}
}