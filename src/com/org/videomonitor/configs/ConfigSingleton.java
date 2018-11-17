package com.org.videomonitor.configs;

import org.apache.log4j.Logger;

import com.org.videomonitor.enums.OSArchitectureType;
import com.org.videomonitor.enums.OSType;
import com.org.videomonitor.utilities.OsCheck;

public class ConfigSingleton {
	static Logger logger = Logger.getLogger(ConfigSingleton.class.getName());
	private static volatile ConfigSingleton instance = null;
	private static OSType OSTYPE = null;
	private static OSArchitectureType OSARCHTYPE = null;

	private ConfigSingleton() {
		try {
			if (OSTYPE == null)
				OSTYPE = OsCheck.getOperatingSystemType();
			if (OSARCHTYPE == null)
				OSARCHTYPE = OsCheck.getOSArchitectureType();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static ConfigSingleton getInstance() {
		if (instance == null) {
			synchronized (ConfigSingleton.class) {
				if (instance == null) {
					instance = new ConfigSingleton();
				}
			}
		}
		return instance;
	}

	public static final String CHROME_DRIVER_LINUX = "linux_chromedriver";
	public static final String CHROME_DRIVER_MAC = "mac_chromedriver";
	public static final String CHROME_DRIVER_WIN = "chromedriver.exe";
	private static final String CHROME_LOGS_MAC = "chromedriver.log";
	private static final String CHROME_LOGS_LINUX = "chromedriver.log";
	private static final String DEFAULT_DOWNLOAD_DIRECTORY_LINUX = "downloads";
	private static final String DEFAULT_DOWNLOAD_DIRECTORY_MAC = "downloads";

	public String getChromeSeleniumDriverPath() {
		if (OSTYPE == OSType.Linux)
			return CHROME_DRIVER_LINUX;
		else if (OSTYPE == OSType.MacOS)
			return CHROME_DRIVER_MAC;
		else
			return CHROME_DRIVER_WIN;
	}

	public void setChromeDriverProperty() {
		if (OSTYPE == OSType.MacOS)
			System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_MAC);
		else if (OSTYPE == OSType.Linux)
			System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_LINUX);
		else
			System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_WIN);
	}

	public void setChromeLogs() {
		if (OSTYPE == OSType.MacOS)
			System.setProperty("webdriver.chrome.logfile", CHROME_LOGS_MAC);
		else if (OSTYPE == OSType.Linux)
			System.setProperty("webdriver.chrome.logfile", CHROME_LOGS_LINUX);
		else
			System.setProperty("webdriver.chrome.logfile", CHROME_LOGS_LINUX);

	}

	public String getChromeDownloadDirectory() {
		if (OSTYPE == OSType.Linux)
			return DEFAULT_DOWNLOAD_DIRECTORY_LINUX;
		else
			return DEFAULT_DOWNLOAD_DIRECTORY_MAC;
	}
}
