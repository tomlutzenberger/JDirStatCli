package com.tomlutzenberger.jdirstat_cli;

import com.tomlutzenberger.jdirstat_cli.helper.ByteFormatter;
import com.tomlutzenberger.jdirstat_cli.helper.CliLoader;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


public class PathAnalyzer implements Runnable {

	private static int fileCount = 0;
	private static int dirCount = 0;
	private static long totalSize = 0;
	private static Map<String,Integer> extensions = new HashMap<>();
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
				String fileExt = this.getFileExtension(childFile);

				this.addExtension(fileExt);

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

		System.out.print("\n\nOverview:\n");
		System.out.printf("  %s\n", ByteFormatter.getAuto(totalSize, true));
		System.out.printf("  %d Files\n", fileCount);
		System.out.printf("  %d Directories\n", dirCount);

		System.out.print("\nExtensions:");

		for (String ext : extensions.keySet()) {
			int count = extensions.get(ext);
			double percentage = ((double)count / (double)fileCount) * 100;
			System.out.printf("  %s: %d (%.2f%%)\n", ext, count, percentage);
		}
	}


	private String getFileExtension(File file) {

		String fileName = file.getName().toLowerCase();

		if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
			return fileName.substring(fileName.lastIndexOf(".") + 1);
		}

		return "";
	}


	private void addExtension(String ext) {

		int extCount = extensions.getOrDefault(ext, 1);
		extensions.put(ext, extCount + 1);
	}

}
