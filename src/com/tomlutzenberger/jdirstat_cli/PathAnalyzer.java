package com.tomlutzenberger.jdirstat_cli;

import java.io.File;


public class PathAnalyzer {

	private int fileCount = 0;
	private int dirCount = 0;
	private long totalSize = 0;
	private File path;


	public PathAnalyzer(File f) {
		this.path = f;
	}


	public void analyze() {

		analyzePath(this.path);

		System.out.print("\n\n");
		System.out.println("Overview:");
		System.out.printf("  %d Files, %s\n", this.fileCount, ByteFormatter.getAuto(this.totalSize, true));
		System.out.printf("  %d Directories\n", this.dirCount);
	}


	protected void analyzePath(File f) {

		String[] children = f.list();

		if (children == null || children.length == 0) {
			//System.out.println("Given path has no children.");
			return;
		}

		for (String child : children) {
			File childFile = new File(f, child);

			CliLoader.progress();

			if (childFile.isFile()) {
				this.fileCount++;
				this.totalSize += childFile.length();
			} else if (childFile.isDirectory()) {
				this.dirCount++;
				analyzePath(childFile);
			}
		}
	}

}
