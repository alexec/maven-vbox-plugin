package com.alexecollins.vbox.core.task;

import com.alexecollins.vbox.core.VBox;
import com.alexecollins.vbox.core.ssh.PresetUserInfo;
import com.alexecollins.vbox.profile.Profile;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.util.concurrent.Callable;

/**
 * Connect to a box.
 *
 * @author alexec (alex.e.c@gmail.com)
 * @since 3.0.0
 */
public class Ssh implements Callable<Void> {
	private final VBox box;

	public Ssh(VBox box) {
		this.box = box;
	}

	public Void call() throws Exception {
		final JSch jsch = new JSch();
		final Profile.SSH x = box.getProfile().getSSH();
		final Session session = jsch.getSession(x.getAuth().getUsername(), x.getHostname(), x.getPort());
		session.setUserInfo(new PresetUserInfo(x.getAuth().getPassword(), null));
		session.connect(30000);
		final Channel channel=session.openChannel("shell");
		channel.setInputStream(System.in);
		channel.setOutputStream(System.out);
		channel.connect(3000);
		try {
			while (channel.isConnected()) {
				Thread.sleep(250); // bit rubbish way of doing this
			}
		} finally {
			channel.disconnect();
		}
		return null;
	}
}
