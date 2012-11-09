package org.slf4j.impl;

import org.apache.maven.plugin.logging.Log;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MarkerIgnoringBase;
import org.slf4j.helpers.MessageFormatter;

/**
 * To use this, set LOG to be the result of {@link org.apache.maven.plugin.AbstractMojo#getLog()}.
 *
 * @author alex.e.c@gmail.com
 */
public class MavenLogAdapter extends MarkerIgnoringBase {
	// MS_CANNOT_BE_FINAL
	public static Log LOG;
	private static final int LOG_LEVEL_TRACE = 1;
	private static final int LOG_LEVEL_DEBUG = 2;
	private static final int LOG_LEVEL_INFO = 3;
	private static final int LOG_LEVEL_WARN = 4;
	private static final int LOG_LEVEL_ERROR = 5;

	MavenLogAdapter(String name) {
		this.name = name;
	}

	private void log(int level, String message, Throwable t) {

		switch (level) {
			case LOG_LEVEL_DEBUG:
			case LOG_LEVEL_TRACE:
				if (t != null)
					LOG.debug(message, t);
				else
					LOG.debug(message);
				break;
			case LOG_LEVEL_INFO:
				if (t != null)
					LOG.info(message, t);
				else
					LOG.info(message);
				break;
			case LOG_LEVEL_ERROR:
				if (t != null)
					LOG.error(message, t);
				else
					LOG.error(message);
				break;
			case LOG_LEVEL_WARN:
				if (t != null)
					LOG.warn(message, t);
				else
					LOG.warn(message);
				break;
			default:
				throw new UnsupportedOperationException("unsupported level " + level);
		}
	}


	private void formatAndLog(int level, String format, Object arg1,
	                          Object arg2) {

		FormattingTuple tp = MessageFormatter.format(format, arg1, arg2);
		log(level, tp.getMessage(), tp.getThrowable());
	}


	private void formatAndLog(int level, String format, Object[] argArray) {
		FormattingTuple tp = MessageFormatter.arrayFormat(format, argArray);
		log(level, tp.getMessage(), tp.getThrowable());
	}


	public boolean isTraceEnabled() {
		return true;
	}


	public void trace(String msg) {
		log(LOG_LEVEL_TRACE, msg, null);
	}


	public void trace(String format, Object param1) {
		formatAndLog(LOG_LEVEL_TRACE, format, param1, null);
	}


	public void trace(String format, Object param1, Object param2) {
		formatAndLog(LOG_LEVEL_TRACE, format, param1, param2);
	}


	public void trace(String format, Object[] argArray) {
		formatAndLog(LOG_LEVEL_TRACE, format, argArray);
	}


	public void trace(String msg, Throwable t) {
		log(LOG_LEVEL_TRACE, msg, t);
	}

	public boolean isDebugEnabled() {
		return true;
	}

	public void debug(String msg) {
		log(LOG_LEVEL_DEBUG, msg, null);
	}

	public void debug(String format, Object param1) {
		formatAndLog(LOG_LEVEL_DEBUG, format, param1, null);
	}

	public void debug(String format, Object param1, Object param2) {
		formatAndLog(LOG_LEVEL_DEBUG, format, param1, param2);
	}


	public void debug(String format, Object[] argArray) {
		formatAndLog(LOG_LEVEL_DEBUG, format, argArray);
	}

	public void debug(String msg, Throwable t) {
		log(LOG_LEVEL_DEBUG, msg, t);
	}

	public boolean isInfoEnabled() {
		return true;
	}

	public void info(String msg) {
		log(LOG_LEVEL_INFO, msg, null);
	}

	public void info(String format, Object arg) {
		formatAndLog(LOG_LEVEL_INFO, format, arg, null);
	}

	public void info(String format, Object arg1, Object arg2) {
		formatAndLog(LOG_LEVEL_INFO, format, arg1, arg2);
	}


	public void info(String format, Object[] argArray) {
		formatAndLog(LOG_LEVEL_INFO, format, argArray);
	}

	public void info(String msg, Throwable t) {
		log(LOG_LEVEL_INFO, msg, t);
	}


	public boolean isWarnEnabled() {
		return true;
	}


	public void warn(String msg) {
		log(LOG_LEVEL_WARN, msg, null);
	}


	public void warn(String format, Object arg) {
		formatAndLog(LOG_LEVEL_WARN, format, arg, null);
	}


	public void warn(String format, Object arg1, Object arg2) {
		formatAndLog(LOG_LEVEL_WARN, format, arg1, arg2);
	}

	public void warn(String format, Object[] argArray) {
		formatAndLog(LOG_LEVEL_WARN, format, argArray);
	}

	public void warn(String msg, Throwable t) {
		log(LOG_LEVEL_WARN, msg, t);
	}

	public boolean isErrorEnabled() {
		return true;
	}

	public void error(String msg) {
		log(LOG_LEVEL_ERROR, msg, null);
	}


	public void error(String format, Object arg) {
		formatAndLog(LOG_LEVEL_ERROR, format, arg, null);
	}

	public void error(String format, Object arg1, Object arg2) {
		formatAndLog(LOG_LEVEL_ERROR, format, arg1, arg2);
	}

	public void error(String format, Object[] argArray) {
		formatAndLog(LOG_LEVEL_ERROR, format, argArray);
	}

	public void error(String msg, Throwable t) {
		log(LOG_LEVEL_ERROR, msg, t);
	}
}
