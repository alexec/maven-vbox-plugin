package com.alexecollins.vbox.core.task;

import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class ListDefinitionsTest {
	@Test
	public void testCall() throws Exception {
		assertThat(new ListDefinitions().call().size(), equalTo(3));
	}
}
