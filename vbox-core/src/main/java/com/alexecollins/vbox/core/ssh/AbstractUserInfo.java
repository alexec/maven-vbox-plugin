package com.alexecollins.vbox.core.ssh;

import com.jcraft.jsch.UserInfo;

/**
 * @author alexec (alex.e.c@gmail.com)
 * @since 3.0.0
 */
public abstract class AbstractUserInfo implements UserInfo {
	protected String passphrase;
	protected String password;

	public String getPassphrase() {
		return passphrase;
	}

	public String getPassword() {
		return password;
	}
}
