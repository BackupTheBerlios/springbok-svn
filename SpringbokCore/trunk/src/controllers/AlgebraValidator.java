package controllers;

import java.text.MessageFormat;

import utils.AlgebraConstants;
import utils.MessageBundle;
import utils.StringUtils;

/**
 * Algebra statement validator
 * 
 * @author Hirantha Bandara
 * 
 */
public class AlgebraValidator {

	public static String validate(String algebra) {
		String message = null;

		if (StringUtils.isNotEmpty(algebra)) {
			algebra = StringUtils.normalize(algebra);
			char ch = algebra.charAt(0);
			if (ch == AlgebraConstants.SELECT)
				message = resolveSelectStatement(algebra);
			else if (ch == AlgebraConstants.PROJECT)
				message = resolveProjectStatement(algebra);
			else if (algebra.contains(AlgebraConstants.UNION.toString()))
				message = resolveUnionStatement(algebra);
			else if (algebra.contains(AlgebraConstants.INTERSECTION.toString()))
				message = resolveIntersectionStatement(algebra);
			else if (algebra.contains(AlgebraConstants.DIFFERENCE.toString()))
				message = resolveDifferenceStatement(algebra);
			else if(algebra.contains(AlgebraConstants.PRODUCT.toString()))
				message = resolveProductStatement(algebra);
		}

		return message;
	}

	private static String resolveProductStatement(String algebra) {
		
		int index = algebra.indexOf(AlgebraConstants.ASSIGN); 
		if(index > 0) {
			// remove assign part
			algebra = algebra.substring(index + 1);
		}
			
		
		
		String[] parts = algebra.split(AlgebraConstants.PRODUCT.toString());
		if (parts.length != 2) {
			return MessageBundle
					.getMessage(MessageBundle.PRODUCT_TABLES_UNBALANCED);
		} else if(parts.length == 2){
			String varLeft = parts[0];
			varLeft = StringUtils.normalize(varLeft);

			if(!varLeft.matches("\\p{Graph}*"))
				return MessageBundle
				.getMessage(MessageBundle.PRODUCT_TABLES_LEFT_TABEL_ERROR);
			
			String varRight = parts[1];
			varRight = StringUtils.normalize(varRight);
			if(!varRight.matches("\\p{Graph}*"))
				return MessageBundle
				.getMessage(MessageBundle.PRODUCT_TABLES_RIGHT_TABEL_ERROR);

		}
		return null;
	}

	private static String resolveUnionStatement(String algebra) {
		String[] parts = algebra.split(AlgebraConstants.UNION.toString());
		if (parts.length != 2) {
			return MessageBundle
					.getMessage(MessageBundle.UNION_COLUMN_UNBALANCED);
		}
		return null;
	}

	private static String resolveIntersectionStatement(String algebra) {
		String[] parts = algebra
				.split(AlgebraConstants.INTERSECTION.toString());
		if (parts.length != 2) {
			return MessageBundle
					.getMessage(MessageBundle.INTERSECT_COLUMN_UNBALANCED);
		}
		return null;
	}

	private static String resolveDifferenceStatement(String algebra) {
		String[] parts = algebra.split(AlgebraConstants.DIFFERENCE.toString());
		if (parts.length != 2) {
			return MessageBundle
					.getMessage(MessageBundle.DIFFERENCE_COLUMN_UNBALANCED);
		}
		return null;
	}

	/**
	 * @param algebra
	 * @return
	 */
	private static String resolveProjectStatement(String algebra) {
		// should have at lease 'project col (tableName)'
		if (algebra.length() < 2)
			return MessageBundle
					.getMessage(MessageBundle.PROJECT_MISSING_PROJECT_COLUMN);

		int indexOfOpenBrace = algebra.indexOf('(');
		if (indexOfOpenBrace < 1)
			return MessageBundle
					.getMessage(MessageBundle.PROJECT_MISSING_TABLE_BRACES);

		int indexOfCloseBrace = algebra.indexOf(')');
		if (indexOfCloseBrace < 0)
			return MessageBundle
					.getMessage(MessageBundle.PROJECT_MISSING_TABLE_BRACES);

		if (indexOfCloseBrace < indexOfOpenBrace)
			return MessageBundle
					.getMessage(MessageBundle.PROJECT_MISSING_TABLE_BRACES);

		String tableName = algebra.substring(indexOfOpenBrace + 1,
				indexOfCloseBrace);
		tableName = StringUtils.normalize(tableName);

		if (StringUtils.isEmpty(tableName))
			return MessageBundle
					.getMessage(MessageBundle.SELECT_MISSING_TABLE_NAME);

		/*
		 * select projectClause (tableName);
		 */
		String projectClause = algebra.substring(1, indexOfOpenBrace);
		projectClause = StringUtils.normalize(projectClause);

		if (StringUtils.isNotEmpty(projectClause))
			return resolveProjectClause(projectClause);
		else
			return MessageBundle
					.getMessage(MessageBundle.PROJECT_MISSING_PROJECT_COLUMN);
	}

	private static String resolveProjectClause(String projectClause) {
		// projectClause can not be ended with a ','
		if (projectClause.lastIndexOf(',') == projectClause.length() - 1)
			return MessageBundle
					.getMessage(MessageBundle.PROJECT_COLUMN_INVALID_END);
		return null;
	}

	private static String resolveSelectStatement(String algebra) {
		// should have at lease 'select (tableName)'
		if (algebra.length() < 2)
			return MessageBundle
					.getMessage(MessageBundle.SELECT_MISSING_TABLE_NAME);

		int indexOfOpenBrace = algebra.indexOf('(');
		if (indexOfOpenBrace < 1)
			return MessageBundle
					.getMessage(MessageBundle.SELECT_MISSING_TABLE_BRACES);

		int indexOfCloseBrace = algebra.indexOf(')');
		if (indexOfCloseBrace < 0)
			return MessageBundle
					.getMessage(MessageBundle.SELECT_MISSING_TABLE_BRACES);

		if (indexOfCloseBrace < indexOfOpenBrace)
			return MessageBundle
					.getMessage(MessageBundle.SELECT_MISSING_TABLE_BRACES);

		String tableName = algebra.substring(indexOfOpenBrace + 1,
				indexOfCloseBrace);
		tableName = StringUtils.normalize(tableName);

		if (StringUtils.isEmpty(tableName))
			return MessageBundle
					.getMessage(MessageBundle.SELECT_MISSING_TABLE_NAME);

		/*
		 * select whereClause (tableName);
		 */
		String whereClause = algebra.substring(1, indexOfOpenBrace);
		whereClause = StringUtils.normalize(whereClause);

		if (StringUtils.isNotEmpty(whereClause))
			return resolveWhereClause(whereClause);

		return null;
	}

	private static String resolveWhereClause(String whereClause) {
		// Vector<String> conditions = new Vector<String>();
		String operator = "and";
		int index = whereClause.indexOf(operator);
		if (index < 0) {
			operator = "or";
			index = whereClause.indexOf(operator);
		}

		// no and | or
		if (index < 0) {
			String condition = StringUtils.normalize(whereClause);
			return resolveCondition(condition);
		} else {
			String condition = whereClause.substring(0, index);
			condition = StringUtils.normalize(condition);
			String message = resolveCondition(condition);
			// if first condition is ok then test remaining
			if (message == null)
				return resolveWhereClause(whereClause.substring(index
						+ operator.length()));
			else
				return message;
		}
		// return null;
	}

	private static String resolveCondition(String condition) {
		if (!condition.contains("="))
			if (!condition.contains("!="))
				if (!condition.contains("<"))
					if (!condition.contains("<="))
						if (!condition.contains(">"))
							if (!condition.contains(">="))
								return MessageFormat
										.format(
												MessageBundle
														.getMessage(MessageBundle.SELECT_ERROR_WHERE_CONDITION),
												condition);

		return null;
	}
}
