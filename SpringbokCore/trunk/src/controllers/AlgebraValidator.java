package controllers;

import java.text.MessageFormat;
import java.util.Map;

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
		
		if(StringUtils.isNotEmpty(algebra)) {
			algebra = StringUtils.normalize(algebra);
			char ch = algebra.charAt(0);
			if(ch == AlgebraConstants.SELECT)
				message = resolveSelectStatement(algebra);
			else if(ch == AlgebraConstants.PROJECT)
				message = resolveProjectStatement(algebra);
		}
		
		return message;
	}

	/**
	 * @param algebra
	 * @return
	 */
	private static String resolveProjectStatement(String algebra) {
		// should have at lease 'project col (tableName)'
		if(algebra.length()<2)
			return MessageBundle.getMessage(MessageBundle.PROJECT_MISSING_PROJECT_COLUMN);
		
		int indexOfOpenBrace = algebra.indexOf('('); 
		if(indexOfOpenBrace < 1)
			return MessageBundle.getMessage(MessageBundle.PROJECT_MISSING_TABLE_BRACES);
		
		int indexOfCloseBrace = algebra.indexOf(')'); 
		if(indexOfCloseBrace < 0)
			return MessageBundle.getMessage(MessageBundle.PROJECT_MISSING_TABLE_BRACES);
		
		if(indexOfCloseBrace < indexOfOpenBrace)
			return MessageBundle.getMessage(MessageBundle.PROJECT_MISSING_TABLE_BRACES);
		
		String tableName = algebra.substring(indexOfOpenBrace+1, indexOfCloseBrace);
		tableName = StringUtils.normalize(tableName);
		
		if(StringUtils.isEmpty(tableName))
			return MessageBundle.getMessage(MessageBundle.SELECT_MISSING_TABLE_NAME);
		
		/*
		 * select projectClause (tableName);
		 */
		String projectClause = algebra.substring(1, indexOfOpenBrace);
		projectClause = StringUtils.normalize(projectClause);
								
		if(StringUtils.isNotEmpty(projectClause))			
			return resolveProjectClause(projectClause);		
		else		
			return MessageBundle.getMessage(MessageBundle.PROJECT_MISSING_PROJECT_COLUMN); 
	}

	private static String resolveProjectClause(String projectClause) {
		// projectClause can not be ended with a ','		
		if(projectClause.lastIndexOf(',') == projectClause.length() -1)
			return MessageBundle.getMessage(MessageBundle.PROJECT_COLUMN_INVALID_END);
		return null;
	}

	private static String resolveSelectStatement(String algebra) {
		// should have at lease 'select (tableName)'
		if(algebra.length()<2)
			return MessageBundle.getMessage(MessageBundle.SELECT_MISSING_TABLE_NAME);
		
		int indexOfOpenBrace = algebra.indexOf('('); 
		if(indexOfOpenBrace < 1)
			return MessageBundle.getMessage(MessageBundle.SELECT_MISSING_TABLE_BRACES);
		
		int indexOfCloseBrace = algebra.indexOf(')'); 
		if(indexOfCloseBrace < 0)
			return MessageBundle.getMessage(MessageBundle.SELECT_MISSING_TABLE_BRACES);
		
		if(indexOfCloseBrace < indexOfOpenBrace)
			return MessageBundle.getMessage(MessageBundle.SELECT_MISSING_TABLE_BRACES);
		
		String tableName = algebra.substring(indexOfOpenBrace+1, indexOfCloseBrace);
		tableName = StringUtils.normalize(tableName);
		
		if(StringUtils.isEmpty(tableName))
			return MessageBundle.getMessage(MessageBundle.SELECT_MISSING_TABLE_NAME);
		
		/*
		 * select whereClause (tableName);
		 */
		String whereClause = algebra.substring(1, indexOfOpenBrace);
		whereClause = StringUtils.normalize(whereClause);
		
		if(StringUtils.isNotEmpty(whereClause))
			return resolveWhereClause(whereClause);
		
		return null;
	}

	private static String resolveWhereClause(String whereClause) {
		//Vector<String> conditions = new Vector<String>();
		String operator = "and";
		int index = whereClause.indexOf(operator);
		if(index < 0) {
			operator = "or";
			index = whereClause.indexOf(operator);
		}
		
		// no and | or
		if(index < 0) {
			String condition = StringUtils.normalize(whereClause);
			return resolveCondition(condition);
		}
		else {
			String condition = whereClause.substring(0, index);
			condition = StringUtils.normalize(condition);
			String message = resolveCondition(condition);
			// if first condition is ok then test remaining
			if(message == null)
				return resolveWhereClause(whereClause.substring(index + operator.length()));
			else
				return message;
		}
	//return null;
	}

	private static String resolveCondition(String condition) {
		if(!condition.contains("="))
			if(!condition.contains("!="))
				if(!condition.contains("<"))
					if(!condition.contains("<="))
						if(!condition.contains(">"))
							if(!condition.contains(">="))
								return MessageFormat.format(MessageBundle.getMessage(MessageBundle.SELECT_ERROR_WHERE_CONDITION), condition);
				
		return null;
	}
}
