package org.slf4j.impl;

import org.slf4j.ILoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author alex.e.c@gmail.com
 */
public class LoggerFactory implements ILoggerFactory {
	private final Map<String, MavenLogAdapter> loggerMap = new HashMap<String, MavenLogAdapter>();
	
	public org.slf4j.Logger getLogger(String name) {
		synchronized (loggerMap) {
			if (!loggerMap.containsKey(name)) {
				loggerMap.put(name, new MavenLogAdapter(name));
			}

			return loggerMap.get(name);
		}
	}
}