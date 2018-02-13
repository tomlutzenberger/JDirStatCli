package com.tomlutzenberger.jdirstat_cli;

import com.tomlutzenberger.jdirstat_cli.helper.ByteFormatter;
import com.tomlutzenberger.jdirstat_cli.helper.Counter;

import java.io.File;
import java.util.NoSuchElementException;


public class PathAnalyzer implements Runnable {

	private static Counter pathTypeCounter = new Counter();
	private static Counter extensionCounter = new Counter();
	private static long totalSize = 0;

	public static final Object lock = new Object();


	public void run() {

		while (!PathCollector.complete) {

			if (PathCollector.hasNextChild()) {
				try {
					File childFile = new File(PathCollector.getNextChild());
					analyzePath(childFile);

				} catch (NoSuchElementException e) {
				}
			}
		}
	}


	private void analyzePath(File path) {

		String pathType;

		if (path.isFile()) {
			pathType = "files";
			String fileExt = this.getFileExtension(path);

			synchronized (lock) {
				totalSize += path.length();

				try {
					extensionCounter.up(fileExt);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		} else if (path.isDirectory()) {
			pathType = "directories";

		} else {
			pathType = "unknown";
		}

		PathCollector.removeChild(path.getAbsolutePath());

		try {
			synchronized (lock) {
				pathTypeCounter.up(pathType);
			}
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

			System.out.print("\nExtensions:\n");

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
