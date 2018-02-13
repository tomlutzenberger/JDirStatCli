package com.tomlutzenberger.jdirstat_cli;

import com.tomlutzenberger.jdirstat_cli.helper.CliLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class PathCollector implements Runnable
{
	private File path;

	public static List<String> uncollectedPaths = new ArrayList<>();
	public static List<String> childPaths = new ArrayList<>();

	public static boolean complete = false;
	public static final Object lock = new Object();


	public PathCollector(File f)
	{
		this.path = f;
	}


	public void run()
	{
		if (this.path.isDirectory()) {
			String[] children = this.path.list();

			if (children != null && children.length > 0) {
				synchronized (lock) {
					addUncollectedPaths(this.path, children);
				}
			}
		}

		synchronized (lock) {
			updateParentPath();
		}

		int childCount = childPaths.size();
		int totalCount = uncollectedPaths.size() + childCount;
		double percentage = ((double)childCount / (double)totalCount) * 100;

		CliLoader.progress(String.format("%d/%d - %.2f%%", childCount, totalCount, percentage));
	}


	public static void addUncollectedPaths(File parent, String[] paths)
	{
		for (String childPath : paths) {
			String fullChildPath = new File(parent.getAbsolutePath(), childPath).getAbsolutePath();

			if (new File(fullChildPath).isAbsolute()) {
				uncollectedPaths.add(fullChildPath);
			}
		}
	}


	private void updateParentPath()
	{
		childPaths.add(this.path.getAbsolutePath());
		int parentIndex = uncollectedPaths.indexOf(this.path.getAbsolutePath());
		if (parentIndex != -1) uncollectedPaths.remove(parentIndex);
	}

	public static String getNext()
	{
		synchronized (lock) {
			return uncollectedPaths.listIterator().next();
		}
	}

	public static boolean hasNext()
	{
		synchronized (lock) {
			return uncollectedPaths.listIterator().hasNext();
		}
	}

	public static String getNextChild()
	{
		synchronized (lock) {
			return childPaths.listIterator().next();
		}
	}

	public static boolean hasNextChild()
	{
		synchronized (lock) {
			return childPaths.listIterator().hasNext();
		}
	}

	public static void removeChild(String child)
	{
		synchronized (lock) {
			childPaths.remove(child);
		}
	}



}
