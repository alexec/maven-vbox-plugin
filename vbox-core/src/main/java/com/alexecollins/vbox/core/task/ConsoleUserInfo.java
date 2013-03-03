package com.alexecollins.vbox.core.task;

import com.jcraft.jsch.UserInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class ConsoleUserInfo implements UserInfo {
	private final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
	private String passphrase;
	private String password;

	public String getPassphrase() {
		return passphrase;
	}

	public String getPassword() {
		return password;
	}

	public boolean promptPassword(String s) {
		System.out.println(s);
		try {
			password = stdin.readLine();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return true;
	}

	public boolean promptPassphrase(String s) {
		System.out.println(s);
		try {
			passphrase = stdin.readLine();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return true;
	}

	public boolean promptYesNo(String s) {
		System.out.println(s);
		try {
			return stdin.readLine().equalsIgnoreCase("Yes");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void showMessage(String s) {
		System.out.println(s);
	}
}
