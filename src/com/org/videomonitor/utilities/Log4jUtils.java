package com.org.videomonitor.utilities;

import java.io.File;
import java.util.Properties;
import java.util.logging.Level;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Log4jUtils {
	public static final String LOGS_FOLDER = "logs";
	public static final String LOGS_FILE = "FBDetector";
	public static final int sizeLog = 100;

	public static Logger initLog4j() {
		return initLog4j(LOGS_FILE, sizeLog);
	}

	public static Logger initLog4j(String logsFile, int sizeLogMB) {
		try {
			long sizeLogs = FileUtils.folderSize(LOGS_FOLDER);
			if (sizeLogs > 0) {
				String fileSize = FileUtils.readableFileSize(sizeLogs);
				System.out.println("fileSize:" + fileSize);
				if (fileSize.contains("GB")) {
					// MB
					System.out.println("cleanDirectory:" + LOGS_FOLDER);
					org.apache.commons.io.FileUtils.cleanDirectory(new File(LOGS_FOLDER));
					// FileUtils.delete(LOGS_FILE);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Properties properties = new Properties();
		properties.setProperty("log4j.rootLogger", "TRACE,stdout,MyFile");
		properties.setProperty("log4j.rootCategory", "TRACE");

		properties.setProperty("log4j.appender.stdout", "org.apache.log4j.ConsoleAppender");
		properties.setProperty("log4j.appender.stdout.layout", "org.apache.log4j.PatternLayout");
		// properties.setProperty("log4j.appender.stdout.layout.ConversionPattern","%d{yyyy/MM/dd
		// HH:mm:ss.SSS} [%5p] %t (%F) - %m%n");

		// properties.setProperty("log4j.appender.stdout","org.apache.log4j.ConsoleAppender");

		properties.setProperty("log4j.appender.MyFile", "org.apache.log4j.RollingFileAppender");
		properties.setProperty("log4j.appender.MyFile.File",
				LOGS_FOLDER + "/" + logsFile + "-" + TimeUtils.getNow() + ".log");

		properties.setProperty("log4j.appender.MyFile.MaxFileSize", sizeLogMB + "mb");

		properties.setProperty("log4j.appender.MyFile.MaxBackupIndex", "1");
		properties.setProperty("log4j.appender.MyFile.layout", "org.apache.log4j.PatternLayout");
		// properties.setProperty("log4j.appender.MyFile.layout.ConversionPattern","%d{yyyy/MM/dd
		// HH:mm:ss.SSS} [%5p] %t (%F) - %m%n");
		properties.setProperty("log4j.appender.MyFile.layout.ConversionPattern", "%d %5p [%l] - %m%n");

		properties.setProperty("log4j.appender.stdout.layout", "org.apache.log4j.PatternLayout");
		// properties.setProperty("log4j.appender.stdout.layout.ConversionPattern","%5p
		// | %d | %F | %L | %m%n");
		properties.setProperty("log4j.appender.stdout.layout.ConversionPattern", "%d %5p %l - %m%n");
		properties.setProperty("log4j.category.com.restfb", "WARN,file");
		properties.setProperty("log4j.additivity.com.restfb", "false");
		properties.setProperty("log4j.category.com.mongodb", "WARN,file");
		properties.setProperty("log4j.additivity.com.mongodb", "false");
		java.util.logging.Logger.getLogger("com.mongodb").setLevel(Level.OFF);
		// Logger mongoLogger = Logger.getLogger( "org.mongodb.driver" );
		// mongoLogger.setLevel(org.apache.log4j.Level.SEVERE);
		// java.util.logging.Logger.getLogger("org.mongodb.driver").setLevel(java.util.log‌​ging.Level.SEVERE);
		java.util.logging.Logger.getLogger("org.mongodb.driver").setLevel(Level.OFF);
		// %d %5p [%l] - %m%n
		PropertyConfigurator.configure(properties);
		Logger logger = Logger.getLogger("log4j");

//		logger.fatal("This is a FATAL message.");
//		logger.error("This is an ERROR message.");
//		logger.warn("This is a WARN message.");
		logger.info("This is an INFO message.");
//		logger.debug("This is a DEBUG message.");
//		logger.trace("This is a TRACE message.");

		return logger;
	}
}
