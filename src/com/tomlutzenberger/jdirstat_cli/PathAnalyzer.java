package com.tomlutzenberger.jdirstat_cli;

import com.tomlutzenberger.jdirstat_cli.helper.ByteFormatter;
import com.tomlutzenberger.jdirstat_cli.helper.CliLoader;
import com.tomlutzenberger.jdirstat_cli.helper.Counter;

import java.io.File;


public class PathAnalyzer implements Runnable {

	private static Counter pathTypeCounter = new Counter();
	private static Counter extensionCounter = new Counter();
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

			analyzePath(childFile);
		}
	}


	public void analyzePath(File path) {

		String pathType;

		if (path.isFile()) {
			pathType = "files";
			String fileExt = this.getFileExtension(path);
			totalSize += path.length();

			try {
				extensionCounter.up(fileExt);
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if (path.isDirectory()) {
			pathType = "directories";

			Thread pa = new Thread(new PathAnalyzer(path));
			pa.start();
			try {
				pa.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			pathType = "unknown";
		}


		try {
			pathTypeCounter.up(pathType);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static void getStats() {

		System.out.print("\n\nOverview:\n");
		System.out.printf("  %s\n", ByteFormatter.getAuto(totalSize, true));

		try {
			long fileCount = pathTypeCounter.get("files");

			System.out.printf("  %d Files\n", pathTypeCounter.get("files"));
			System.out.printf("  %d Directories\n", pathTypeCounter.get("directories"));

			System.out.print("\nExtensions:");

			for (String ext : extensionCounter.getAll()) {
				long extCount = extensionCounter.get(ext);
				double percentage = ((double)extCount / (double)fileCount) * 100;
				System.out.printf("  %s: %d (%.2f%%)\n", ext, extCount, percentage);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private String getFileExtension(File file) {

		String fileName = file.getName().toLowerCase();

		if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
			return fileName.substring(fileName.lastIndexOf(".") + 1);
		}

		return "";
	}

}
