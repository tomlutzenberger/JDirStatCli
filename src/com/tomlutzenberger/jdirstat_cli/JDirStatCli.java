package com.tomlutzenberger.jdirstat_cli;

import java.io.File;


public class JDirStatCli {

	public static void main(String[] args) {

		if (args.length != 1) {
			System.err.println("Only 1 argument is accepted.");
			return;
		}

		String path = args[0];
		File f = new File(path);

		if (f.exists() && f.isAbsolute()) {
			PathAnalyzer pa = new PathAnalyzer(f);
			pa.analyze();
		} else {
			System.err.printf("Path `%s` is not valid, not absolute or does not exist.\n", path);
		}
	}



}
