package com.org.videomonitor.utilities;

import java.io.File;
import java.text.DecimalFormat;

import org.apache.log4j.Logger;



public class FileUtils {
	static Logger logger = Logger.getLogger(FileUtils.class.getName());
	public static final String NAME_FILE = "pg";
	public static final String ENTER_ROW = "\t\t\t";
	// kich thuoc theo MB
	public static final int FILE_SIZE_MAX_MB = 25;
	public static final int FILE_SIZE_MIN_MB = 5;
	public static String FOLDER_SAVE = null;
	public static String FILE_NAME = null;
	public static String FILE_NAME_NEW = null;

	public static long folderSize(String directory) {
		return folderSize(new File(directory));
	}

	public static long folderSize(File directory) {
		long length = 0;
		if (directory.listFiles() != null) {
			for (File file : directory.listFiles()) {
				if (file.isFile())
					length += file.length();
				else
					length += folderSize(file);
			}
		}
		return length;
	}

	public static String readableFileSize(long size) {
		if (size <= 0)
			return "0";
		final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
		int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
		return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	}

	public static boolean checkFileIsMaxSize(String fileName) {
		return checkFileIsMaxSize(new File(fileName), FILE_SIZE_MAX_MB);
	}

	public static boolean checkFileIsMaxSize(File file, int Max) {

		// Get length of file in bytes
		long fileSizeInBytes = file.length();
		// Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
		long fileSizeInKB = fileSizeInBytes / 1024;
		// Convert the KB to MegaBytes (1 MB = 1024 KBytes)
		long fileSizeInMB = fileSizeInKB / 1024;

		if (fileSizeInMB > Max) {
			return true;
		}
		return false;
	}

	
}
