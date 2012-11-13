package com.alexecollins.vbox.core.task;

import com.alexecollins.vbox.manifest.Manifest;

import javax.xml.bind.JAXB;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class ListDefinitions implements Callable<List<String>> {
	public List<String> call() throws Exception {

		return JAXB.unmarshal(getClass().getResource("/Manifest.xml"), Manifest.class).getFile();
	}
}
