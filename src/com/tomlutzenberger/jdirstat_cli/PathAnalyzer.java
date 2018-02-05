package com.tomlutzenberger.jdirstat_cli;

import java.io.File;


public class PathAnalyzer implements Runnable {

	private static int fileCount = 0;
	private static int dirCount = 0;
	private static long totalSize = 0;
	private File path;


	public PathAnalyzer(File f) {
		this.path = f;
	}


	public void run() {

		String[] children = this.path.list();

		if (children == null || children.length == 0) {
			//Given path has no children
			return;
		}

		for (String child : children) {
			File childFile = new File(this.path, child);

			CliLoader.progress();

			if (childFile.isFile()) {
				fileCount++;
				totalSize += childFile.length();
			} else if (childFile.isDirectory()) {
				dirCount++;

				Thread pa = new Thread(new PathAnalyzer(childFile));
				pa.start();
				try {
					pa.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void getStats() {
		System.out.print("\n\n");
		System.out.println("Overview:");
		System.out.printf("  %d Files, %s\n", fileCount, ByteFormatter.getAuto(totalSize, true));
		System.out.printf("  %d Directories\n", dirCount);
	}

}
