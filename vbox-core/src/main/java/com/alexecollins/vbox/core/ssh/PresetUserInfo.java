package com.alexecollins.vbox.core.ssh;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class PresetUserInfo extends AbstractUserInfo {
	public PresetUserInfo(String password, String passphrase) {
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
		//To change body of implemented methods use File | Settings | File Templates.
	}
}