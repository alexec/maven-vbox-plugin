package com.alexecollins.util;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class Bytes2Test {
	@Test
	public void testSearchAndReplace() throws Exception {

		byte[] s = "${search}".getBytes();
		byte[] r = "replace".getBytes();

		byte[] src = "${search} and ${search} is ${search}".getBytes();

		final byte[] actuals = Bytes2.searchAndReplace(src, s, r);
		//System.out.println("<"+new String(actuals)+">");
		assertArrayEquals("replace and replace is replace".getBytes(), actuals);

	}

	@Test
	public void testSearchAndReplace2() throws Exception {

		byte[] s = "${search}".getBytes();
		byte[] r = "repl".getBytes();    // shorted

		byte[] src = "${search} and ${search} is ${search}".getBytes();

		final byte[] actuals = Bytes2.searchAndReplace(src, s, r);
		//System.out.println("<"+new String(actuals)+">");
		assertArrayEquals("repl and repl is repl".getBytes(), actuals);

	}}
