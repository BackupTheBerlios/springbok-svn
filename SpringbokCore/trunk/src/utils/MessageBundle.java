package utils;

import java.util.HashMap;

public class MessageBundle {

	public static final String SELECT_MISSING_TABLE_NAME = "SELECT_MISSING_TABLE_NAME";
	public static final String SELECT_MISSING_TABLE_BRACES = "SELECT_MISSING_TABLE_BRACES";
	public static final String SELECT_ERROR_WHERE_CONDITION = "SELECT_ERROR_WHERE_CONDITION";
	
	public static final String PROJECT_MISSING_TABLE_NAME = "PROJECT_MISSING_TABLE_NAME";
	public static final String PROJECT_MISSING_PROJECT_COLUMN = "PROJECT_MISSING_PROJECT_COLUMN";
	public static final String PROJECT_MISSING_TABLE_BRACES = "PROJECT_MISSING_TABLE_BRACES";
	public static final String PROJECT_COLUMN_INVALID_END = "PROJECT_COLUMN_INVALID_END";
	
	public static final String UNION_COLUMN_UNBALANCED = "UNION_COLUMN_UNBALANCED";
	public static final String INTERSECT_COLUMN_UNBALANCED = "INTERSECT_COLUMN_UNBALANCED";
	public static final String DIFFERENCE_COLUMN_UNBALANCED = "DIFFERENCE_COLUMN_UNBALANCED";

	private static HashMap<String, String> content = new HashMap<String, String>();

	static{
		content.put(SELECT_MISSING_TABLE_NAME, "You have an error in your Query syntax; Expecting table name. E.g "+ AlgebraConstants.SELECT +" whereClause (tableName)");
		content.put(SELECT_MISSING_TABLE_BRACES, "You have an error in your Query syntax; Missing braces around table name. E.g "+ AlgebraConstants.SELECT +" whereClause (tableName)");
		content.put(SELECT_ERROR_WHERE_CONDITION, "You have an error in your Query syntax; Conditional expression is not valid in where clause {0}. E.g operand=operand");		
		content.put(PROJECT_MISSING_TABLE_NAME, "You have an error in your Query syntax; Expecting table name. E.g "+ AlgebraConstants.PROJECT +" column (tableName)");		
		content.put(PROJECT_MISSING_PROJECT_COLUMN, "You have an error in your Query syntax; Expecting at least one projected column. E.g "+ AlgebraConstants.PROJECT +" column (tableName)");
		content.put(PROJECT_MISSING_TABLE_BRACES, "You have an error in your Query syntax; Missing braces around table name. E.g "+ AlgebraConstants.PROJECT +" column (tableName)");		
		content.put(PROJECT_COLUMN_INVALID_END, "You have an error in your Query syntax; Invalid projectionClause end. Remove additional ',' in projectionClause. E.g "+ AlgebraConstants.PROJECT +" column1,column1 (tableName)");
		content.put(UNION_COLUMN_UNBALANCED, "You have an error in your Query syntax; Union operation columns have to be union compatible. E.g column1 "+ AlgebraConstants.UNION +" column2");
		content.put(INTERSECT_COLUMN_UNBALANCED, "You have an error in your Query syntax; Intersection operation columns have to be union compatible. E.g column1 "+ AlgebraConstants.INTERSECTION +" column2");
		content.put(DIFFERENCE_COLUMN_UNBALANCED, "You have an error in your Query syntax; Difference operation columns have to be union compatible. E.g column1 "+ AlgebraConstants.DIFFERENCE +" column2");
	}
	
	public static String getMessage(String key) {
		return content.get(key);
	}
}
