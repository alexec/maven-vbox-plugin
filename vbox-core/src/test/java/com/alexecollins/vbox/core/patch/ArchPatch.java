package com.alexecollins.vbox.core.patch;

import com.alexecollins.util.SystemUtils2;
import com.alexecollins.vbox.core.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;

/**
 * Makes sure the architecture matches the host.
 *
 * @author alexec (alex.e.c@gmail.com)
 * @ince 2.0.0
 */
public class ArchPatch extends PredefinedPatch {
	private static final Logger LOGGER = LoggerFactory.getLogger(ArchPatch.class);
	public ArchPatch(String templateName) throws IOException {
		super(templateName + "--64bit", Collections.<String, String>emptyMap());
	}

	@Override
	public void apply(VBox box) throws Exception {
		if (!box.is64Bit() && SystemUtils2.is64Bit()){
			LOGGER.info("patching architecture with 64bit");
			super.apply(box);
		} else {
			LOGGER.info("not patching architecture");
		}
	}
}
