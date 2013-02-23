package com.alexecollins.vbox.core.patch;

import com.alexecollins.util.SystemUtils2;
import com.alexecollins.vbox.core.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

/**
 * Makes sure the architecture matches the host.
 *
 * @author alexec (alex.e.c@gmail.com)
 * @since 2.0.0
 */
public class ArchPatch implements  Patch {
	private static final Logger LOGGER = LoggerFactory.getLogger(ArchPatch.class);

	public void apply(VBox box) throws Exception {
		if (!box.is64Bit() && SystemUtils2.is64Bit()){
			LOGGER.info("patching architecture with 64bit");
			new PredefinedPatch(box.getProfile().getTemplate() + "--64bit", Collections.<String, String>emptyMap()).apply(box);
		} else {
			LOGGER.info("not patching architecture");
		}
	}

	public String getName() {
		return "ArchPatch";
	}
}
