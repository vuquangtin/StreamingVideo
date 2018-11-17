package com.org.videomonitor.utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.github.ffpojo.exception.FFPojoException;

/**
 *
 * @author EMAIL:vuquangtin@gmail.com , tel:01677443333
 * @version 1.0.0
 */
public class FfpojoUtils {
	static Logger logger = Logger.getLogger(FfpojoUtils.class.getName());
	public static final String URL_FILE_NAME = "url.txt";

	public static String readFileAsString(String filePath) throws IOException {
		try {
			if (new File(filePath).exists())
				return org.apache.commons.io.FileUtils.readFileToString(new File(filePath), StandardCharsets.UTF_8);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static int writeObject(String content) throws IOException, FFPojoException {
		return writeObject(URL_FILE_NAME, content);
	}

	private static int writeObject(String filePath, String content) throws IOException, FFPojoException {

		File file = new File(filePath);
		boolean isExist = true;
		if (!file.exists()) {
			file.createNewFile();
			isExist = false;
		}
		try (BufferedWriter textFileWriter = Files.newBufferedWriter(Paths.get(filePath),
				java.nio.file.StandardOpenOption.APPEND)) {
			if (isExist) {
				textFileWriter.newLine();
				if (logger.isDebugEnabled()) {
					logger.debug("exists  " + isExist);
				}
			}
			textFileWriter.write(content);

		}
		return 1;
	}

	public static List<String> readObject(int currentPage, int itemsPerPage) throws IOException, FFPojoException {
		return readObject(URL_FILE_NAME, currentPage, itemsPerPage);
	}

	private static List<String> readObject(String filePath, int currentPage, int itemsPerPage)
			throws IOException, FFPojoException {
		List<String> url = new ArrayList<String>();
		if (!new File(filePath).exists()) {
			return url;
		}

		int startNum = (currentPage - 1) * itemsPerPage;
		if (startNum == 0)
			startNum = 1;
		int endNum = startNum + itemsPerPage;
		long startTime = System.nanoTime();
		// StringBuilder sb = new StringBuilder();
		Charset encoding = StandardCharsets.UTF_8;
		if (logger.isDebugEnabled())
			logger.debug("read file:" + filePath+"startNum:"+startNum+"\tendNum:"+endNum);
		FileInputStream inputStream = new FileInputStream(filePath);
		try (LineNumberReader rdr = new LineNumberReader(new InputStreamReader(inputStream, encoding))) {
			for (String line = null; (line = rdr.readLine()) != null;) {
				if (logger.isDebugEnabled())
					logger.debug("line["+rdr.getLineNumber()+"]:" + line);
				if (rdr.getLineNumber() >= startNum && rdr.getLineNumber() <= endNum) {
					url.add(line);
				} else if (rdr.getLineNumber() >= endNum) {
					long endTime = System.nanoTime();
					long elapsedTimeInMillis = TimeUnit.MILLISECONDS.convert((endTime - startTime),
							TimeUnit.NANOSECONDS);
					if (logger.isDebugEnabled()) {
						logger.debug("Total elapsed time: " + elapsedTimeInMillis + " ms");
					}
					if (logger.isDebugEnabled()) {
						logger.debug("listProfiles.size: " + url.size());
					}
					return url;
				}
			}
			// return new String[] { sb1.toString(), sb2.toString() };
		}
		return url;
	}

	public static boolean isObjectSaved(String content, String filePath) {
		boolean isSaved = checkObject(content, filePath);
		if (isSaved == true)
			return isSaved;
		if (isSaved)
			return checkObject(content, filePath);
		return false;
	}

	private static boolean checkObject(String content, String filePath) {
		try {
			if (!new File(filePath).exists()) {
				return false;
			}
			FileInputStream inputStream = new FileInputStream(filePath);
			Charset encoding = StandardCharsets.UTF_8;
			try (LineNumberReader rdr = new LineNumberReader(new InputStreamReader(inputStream, encoding))) {
				for (String line = null; (line = rdr.readLine()) != null;) {
					if (line.equals(content)) {
						if (logger.isDebugEnabled())
							logger.debug("line:" + line);
						return true;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void main(String[] args) {
		logger = Log4jUtils.initLog4j();
		try {
			List<String> list = FfpojoUtils.readObject(0, 10);
			for (String item : list) {
				System.out.println("item: " + item);
				if (logger.isDebugEnabled()) {
					logger.debug("item:" + item);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FFPojoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
