package com.tomlutzenberger.jdirstat_cli;

import java.math.BigDecimal;
import java.math.RoundingMode;


public class ByteFormatter {

	//private static final long ONE_TERA = 1024 * 1024 * 1024 * 1024;
	private static final long ONE_GIGA = 1024 * 1024 * 1024;
	private static final long ONE_MEGA = 1024 * 1024;
	private static final long ONE_KILO = 1024;


	public static String getAuto(long bytes) {

		if (bytes > ONE_GIGA) {
			return String.format("%d GB", getGbytes(bytes));

		} else if (bytes > ONE_MEGA) {
			return String.format("%d MB", getMbytes(bytes));

		} else if (bytes > ONE_KILO) {
			return String.format("%d kB", getKbytes(bytes));

		} else {
			return String.format("%d B", bytes);
		}
	}


	public static String getAuto(long bytes, boolean decimal) {

		if (!decimal) {
			return getAuto(bytes);
		}

		if (bytes > ONE_GIGA) {
			return String.format("%.2f GB", getDecimalGbytes(bytes));

		} else if (bytes > ONE_MEGA) {
			return String.format("%.2f MB", getDecimalMbytes(bytes));

		} else if (bytes > ONE_KILO) {
			return String.format("%.2f kB", getDecimalKbytes(bytes));

		} else {
			return String.format("%d B", bytes);
		}
	}



	public static long getKbytes(long bytes) {
		return bytes / ONE_KILO;
	}



	public static double getDecimalKbytes(long bytes) {
		return (double)bytes / ONE_KILO;
	}



	public static long getMbytes(long bytes) {
		return bytes / ONE_MEGA;
	}



	public static double getDecimalMbytes(long bytes) {
		return (double)bytes / ONE_MEGA;
	}



	public static long getGbytes(long bytes) {
		return bytes / ONE_GIGA;
	}



	public static double getDecimalGbytes(long bytes) {
		return (double)bytes / ONE_GIGA;
	}
}
