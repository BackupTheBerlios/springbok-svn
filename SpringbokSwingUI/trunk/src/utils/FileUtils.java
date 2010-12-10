package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Utility class for files
 * 
 * @author Hirantha Bandara
 * 
 */
public class FileUtils {

	/**
	 * Reads the given file
	 * 
	 * @param file
	 * @return
	 */
	public static String readFile(File file) {
		StringBuffer buffer = new StringBuffer();
		FileReader fileReader = null;
		BufferedReader reader = null;
		try {
			fileReader = new FileReader(file);
			reader = new BufferedReader(fileReader);
			String line = null;
			while ((line = reader.readLine()) != null) {
				buffer.append(line + "\n");
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
				}
			}

			if (fileReader != null) {
				try {
					fileReader.close();
				} catch (IOException e) {
				}
			}
		}
		return buffer.toString();
	}

	/**
	 * Creates the file using given content
	 * 
	 * @param file
	 * @param content
	 * @return
	 */
	public static boolean createFile(File file, String content) {
		FileWriter fileWriter = null;
		BufferedWriter writer = null;
		try {
			fileWriter = new FileWriter(file);
			writer = new BufferedWriter(fileWriter);
			writer.write(content);
			return true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
				}
			}

			if (fileWriter != null) {
				try {
					fileWriter.close();
				} catch (IOException e) {
				}
			}
		}

		return false;
	}
}
