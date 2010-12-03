package utils;

import java.util.HashMap;

public class MessageBundle {

	public static final String MISSING_TABLE_NAME = "MISSING_TABLE_NAME";
	public static final String MISSING_TABLE_BRACES = "MISSING_TABLE_BRACES";
	public static final String ERROR_WHERE_CONDITION = "ERROR_WHERE_CONDITION";

	private static HashMap<String, String> content = new HashMap<String, String>();

	static{
		content.put(MISSING_TABLE_NAME, "You have an error in your Query syntax; Expecting table name. E.g "+ AlgebraConstants.SELECT +" whereClause (tableName)");
		content.put(MISSING_TABLE_BRACES, "You have an error in your Query syntax; Missing braces around table name. E.g "+ AlgebraConstants.SELECT +" whereClause (tableName)");
		content.put(ERROR_WHERE_CONDITION, "You have an error in your Query syntax; Conditional expression is not valid in where clause {0}. E.g operand=operand");
	}
	
	public static String getMessage(String key) {
		return content.get(key);
	}
}
