package com.alexecollins.maven.plugins.vbox;

import com.google.common.base.Joiner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author alex.collins
 */
public class ExecUtils {

	public static String exec(String cmd) throws IOException, InterruptedException {
		return execAux(cmd, Runtime.getRuntime().exec(cmd));
	}

	public static String exec(String... strings) throws IOException, InterruptedException {
		return execAux(Joiner.on(" ").join(new ProcessBuilder(strings).command()), new ProcessBuilder(strings).start());
	}

	public static String execAux(final String command, final Process p) throws IOException, InterruptedException {
		// stdout
		final String out = log(p.getInputStream());
		// stderr
		final String err = log(p.getErrorStream());

		if (p.waitFor() != 0) {
			throw new RuntimeException("failed to execute " + command + ", exitValue=" + p.exitValue() + ": " + (err != null ? err : out));
		}

		return out;
	}

	public static String log(final InputStream inputStream) throws IOException {
		final BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
		final StringBuffer out = new StringBuffer();
		try {
			String l;
			while ((l = r.readLine()) != null) {
				out.append(l).append('\n');
			}
		} finally {
			r.close();
		}
		return out.toString();
	}
}
