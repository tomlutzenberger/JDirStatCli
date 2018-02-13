package com.tomlutzenberger.jdirstat_cli.helper;


public class CliLoader {

	private static char[] states = {'|', '/', '-', '\\'};
	private static byte state = 0;


	public static void progress() {
		progress("");
	}


	public static void progress(String text)
	{
		if (state + 1 >= states.length) {
			state = 0;
		} else {
			state++;
		}

		System.out.printf("\r%s %s", states[state], text);
	}
}
