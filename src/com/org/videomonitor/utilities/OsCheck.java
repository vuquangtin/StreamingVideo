package com.org.videomonitor.utilities;

import java.util.Locale;

import org.apache.log4j.Logger;

import com.org.videomonitor.enums.OSArchitectureType;
import com.org.videomonitor.enums.OSType;

/**
 * Xác định hệ điều hành đang chạy. Từ hệ điều hành đang chạy ta có thể xác định
 * các cấu hình cần thiết cho từng OS.
 * 
 * @author EMAIL:vuquangtin@gmail.com , tel:01677443333
 * @version 1.0.0
 *
 */
public class OsCheck {
	static Logger logger = Logger.getLogger(OsCheck.class.getName());
	// cached result of OS detection
	protected static OSType detectedOS;
	protected static OSArchitectureType detectedOSArch;
	// C:\\
	public static final String WINS = "\\";
	public static final String MAC = "/";

	/**
	 * Detect the operating system from the os.name System property and cache the
	 * result
	 * 
	 * @return - the operating system detected
	 * @author EMAIL:vuquangtin@gmail.com , tel:01677443333
	 * @version 1.0.0
	 */
	public static OSType getOperatingSystemType() {
		if (detectedOS == null) {
			String OS = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
			if (logger.isDebugEnabled())
				logger.debug("os.name:" + OS);
			if ((OS.indexOf("mac") >= 0) || (OS.indexOf("darwin") >= 0)) {
				detectedOS = OSType.MacOS;
			} else if (OS.indexOf("win") >= 0) {
				detectedOS = OSType.Windows;
			} else if (OS.indexOf("nux") >= 0) {
				detectedOS = OSType.Linux;
			} else if (OS.indexOf("solaris") > 0 || (OS.indexOf("sunos") >= 0)) {
				detectedOS = OSType.Solaris;
			} else {
				detectedOS = OSType.Other;
			}
		}
		return detectedOS;
	}

	// public static Util.EnumOS getOSType() {
	// String s = System.getProperty("os.name").toLowerCase(Locale.ROOT);
	// return s.contains("win") ? Util.EnumOS.WINDOWS
	// : (s.contains("mac") ? Util.EnumOS.OSX
	// : (s.contains("solaris") ? Util.EnumOS.SOLARIS
	// : (s.contains("sunos") ? Util.EnumOS.SOLARIS
	// : (s.contains("linux") ? Util.EnumOS.LINUX
	// : (s.contains("unix") ? Util.EnumOS.LINUX : Util.EnumOS.UNKNOWN)))));
	// }
	// @SideOnly(Side.CLIENT)
	// public static enum EnumOS
	// {
	// LINUX,
	// SOLARIS,
	// WINDOWS,
	// OSX,
	// UNKNOWN;
	// }

	public static OSArchitectureType getOSArchitectureType() {
		if (detectedOSArch == null) {
			String osArch = System.getProperty("os.arch", "generic").toLowerCase(Locale.ENGLISH);
			if ((osArch.indexOf("64") >= 0)) {
				detectedOSArch = OSArchitectureType.X64;
			} else {
				detectedOSArch = OSArchitectureType.X32;
			}
		}
		return detectedOSArch;
	}
}
