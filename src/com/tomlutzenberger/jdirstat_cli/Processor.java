package com.tomlutzenberger.jdirstat_cli;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Processor implements Runnable
{

	public void run()
	{
		ExecutorService executor = Executors.newFixedThreadPool(4);
		String previousFilePath = "";

		do {
			File nextFile = new File(PathCollector.getNext());
			String nextFilePath = nextFile.getAbsolutePath();

			if (!previousFilePath.equals(nextFilePath)) {
				executor.submit(new PathCollector(nextFile));
			}

			previousFilePath = nextFilePath;
		} while (PathCollector.hasNext());

		PathCollector.complete = true;

		executor.shutdown();
	}

}
