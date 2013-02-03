package com.alexecollins.vbox.core.task;

import com.alexecollins.vbox.manifest.Manifest;

import javax.xml.bind.JAXB;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * List all the definitions available.
 *
 * @author alexec (alex.e.c@gmail.com)
 * @since 2.0.0
 */
public class ListDefinitions implements Callable<List<String>> {
	public List<String> call() {

		return JAXB.unmarshal(getClass().getResource("/Manifest.xml"), Manifest.class).getFile();
	}
}
