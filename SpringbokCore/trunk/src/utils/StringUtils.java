package utils;

/**
 * Utility class for Strings
 * 
 * @author Hirantha Bandara
 * 
 */
public class StringUtils {

	/**
	 * Returns true if the given string is empty
	 * 
	 * @param string
	 * @return
	 */
	public static boolean isEmpty(String string) {
		return (string == null || string.trim().length() == 0);
	}

	/**
	 * Returns true if the given string is not empty
	 * 
	 * @param string
	 * @return
	 */
	public static boolean isNotEmpty(String string) {
		return (string != null && string.trim().length() != 0);
	}

	/**
	 * Normalises the given string by removing duplicate white spaces
	 * 
	 * @return
	 */
	public static String normalize(String string) {
		if (isNotEmpty(string)) {
			string = string.trim();
			return string.replaceAll("\\s{2,}", " ");
		}
		return "";
	}
}
