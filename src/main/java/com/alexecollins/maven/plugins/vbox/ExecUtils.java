package com.alexecollins.maven.plugins.vbox;

import com.google.common.base.Joiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;

/**
 * Util for executing programs.
 *
 * @author alex.e.c@gmail.com
 */
public class ExecUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExecUtils.class);

	public static String exec(String cmd) throws IOException, InterruptedException, ExecutionException {
		return execAux(cmd, Runtime.getRuntime().exec(cmd));
	}

	public static String exec(String... strings) throws IOException, InterruptedException, ExecutionException {
		return execAux(Joiner.on(" ").join(new ProcessBuilder(strings).command()), new ProcessBuilder(strings).start());
	}

	private static String execAux(final String command, final Process p) throws IOException, InterruptedException, ExecutionException {
		// stdout
		final String out = log(p.getInputStream());
		// stderr
		final String err = log(p.getErrorStream());

		if (p.waitFor() != 0) {
			throw new ExecutionException("failed to execute " + command + ", exitValue=" + p.exitValue() + ": " + (err != null ? err : out), null);
		}

		return out;
	}

	public static String log(final InputStream inputStream) throws IOException {
		final BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
		final StringBuffer out = new StringBuffer();
		try {
			String l;
			while ((l = r.readLine()) != null) {
				LOGGER.debug(l);
				out.append(l).append('\n');
			}
		} finally {
			r.close();
		}
		return out.toString();
	}
}
