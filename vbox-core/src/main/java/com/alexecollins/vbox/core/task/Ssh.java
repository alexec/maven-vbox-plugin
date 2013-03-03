package com.alexecollins.vbox.core.task;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.util.concurrent.Callable;

/**
 * @author alexec (alex.e.c@gmail.com)
 * @since 3.0.0
 */
public class Ssh implements Callable<Void> {
	public Void call() throws Exception {
		final JSch jsch=new JSch();
		final Session session = jsch.getSession("root", "localhost", 10022);
		session.setUserInfo(new ConsoleUserInfo());
		session.connect(30000);
		final Channel channel=session.openChannel("shell");
		channel.setInputStream(System.in);
		channel.setOutputStream(System.out);
		channel.connect(3000);
		return null;
	}

	public static void main(String[] args) throws Exception {
		new Ssh().call();
	}
}
