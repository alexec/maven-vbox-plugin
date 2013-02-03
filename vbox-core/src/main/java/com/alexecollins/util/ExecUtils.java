package com.alexecollins.util;

import com.google.common.base.Joiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.concurrent.*;

/**
 * Util for executing programs.
 *
 * @author alex.e.c@gmail.com
 */
public class ExecUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExecUtils.class);

	/**
	 * @see {@link #exec(byte[], File, String...)}
	 */
	public static String exec(String... strings) throws IOException, InterruptedException, ExecutionException {
		return exec(new byte[0], new File("."), strings);
	}

	/**
	 * Execute the command, but write the contents of stdin into it's input stream.
	 *
	 * NOT suitable for large files.
	 *
	 * E.g.
	 * <code>
	 *     patch -f -p1 < a.patch
	 * </code>
	 * Would be
	 * <code>
	 *     exec("a.patch", "patch", "-f", "-p1");
	 * </code>
	 *
	 * @since 2.0.0
	 */
	public static String exec(final byte[] stdin, File wd, String... strings) throws IOException, InterruptedException, ExecutionException {
		LOGGER.debug("executing: " + Joiner.on(" ").join(strings) + " in " + wd);

		final Process p = new ProcessBuilder().directory(wd).command(strings).start();

		final ExecutorService svc = Executors.newFixedThreadPool(2);
		final Future<String> outFuture = svc.submit(new Callable<String>() {
			public String call() throws Exception {
				return log(p.getInputStream());
			}
		});
		final Future<String> errFuture = svc.submit(new Callable<String>() {
			public String call() throws Exception {
				return log(p.getErrorStream());
			}
		});

		p.getOutputStream().write(stdin);
		p.getOutputStream().close();
		p.waitFor();

		final String out = outFuture.get();
		final String err = errFuture.get();

		svc.shutdown();

		if (p.exitValue() != 0) {
			throw new ExecutionException("failed to execute " + Joiner.on(" ").join(strings) + ", exitValue=" + p.exitValue() + ": " + (err != null ? err : out), null);
		}

		LOGGER.debug("complete ok");

		return out;
	}

	private static String log(final InputStream inputStream) throws IOException {
		final BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
		final StringBuilder out = new StringBuilder();
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
