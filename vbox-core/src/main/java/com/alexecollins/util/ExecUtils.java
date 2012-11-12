package com.alexecollins.util;

import au.com.bytecode.opencsv.CSVWriter;
import com.google.common.base.Joiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

/**
 * Util for executing programs.
 *
 * @author alex.e.c@gmail.com
 */
public class ExecUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExecUtils.class);

	public static String exec(String... strings) throws IOException, InterruptedException, ExecutionException {
		LOGGER.debug("executing: " + Joiner.on(" ").join(strings));

		final Process p = new ProcessBuilder(strings).start();

		// stderr
		final String err = log(p.getErrorStream());
		// stdout
		final String out = log(p.getInputStream());

		if (p.waitFor() != 0) {
			final StringWriter w = new StringWriter();
			new CSVWriter(w, ' ').writeAll(Arrays.<String[]>asList(strings));
			throw new ExecutionException("failed to execute " + w + ", exitValue=" + p.exitValue() + ": " + (err != null ? err : out), null);
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
