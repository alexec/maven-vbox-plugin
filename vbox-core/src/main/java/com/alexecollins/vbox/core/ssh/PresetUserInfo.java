package com.alexecollins.vbox.core.ssh;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class PresetUserInfo extends AbstractUserInfo {
	private static final Logger LOGGER = LoggerFactory.getLogger(PresetUserInfo.class);
	public PresetUserInfo(String password, String passphrase) {
		this.password = password;
		this.passphrase = passphrase;
	}

	public boolean promptPassword(String message) {
		return true;
	}

	public boolean promptPassphrase(String message) {
		return true;
	}

	public boolean promptYesNo(String message) {
		return true;
	}

	public void showMessage(String message) {
		LOGGER.info(message);
	}
}