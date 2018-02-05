package com.tomlutzenberger.jdirstat_cli;


public class CliLoader {

	private static char[] states = {'|', '/', '-', '\\'};
	private static byte state = 0;

	public static void progress() {
		if (state + 1 >= states.length) {
			state = 0;
		} else {
			state++;
		}

		System.out.printf("\r%s Loading...", states[state]);
	}
}
