package com.tomlutzenberger.jdirstat_cli;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.System.currentTimeMillis;


public class JDirStatCli {

	public static void main(String[] args) {

		if (args.length != 1) {
			System.err.println("Only 1 argument is accepted.");
			return;
		}

		long startTime = System.currentTimeMillis();

		String path = args[0];
		File f = new File(path);

		if (f.exists() && f.isAbsolute()) {

			String[] childList = f.list();
			if (childList == null) {
				System.err.printf("Path `%s` has no children. Quit.", f.getAbsolutePath());
				return;
			}

			PathCollector.addUncollectedPaths(f, childList);

			Thread proc = new Thread(new Processor());
			proc.start();

			Thread pa = new Thread(new PathAnalyzer());
			pa.start();

			try {
				proc.join();
				pa.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			PathAnalyzer.getStats();

			double duration = (double)(System.currentTimeMillis() - startTime) / 1000;

			System.out.printf("\n\nDuration: %.2f sec\n", duration);

		} else {
			System.err.printf("Path `%s` is not valid, not absolute or does not exist.\n", path);
		}
	}

}
