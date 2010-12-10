package controllers;

import java.util.Map;

import utils.AlgebraConstants;
import utils.StringUtils;

public class AlgebraToSqlTranslator {

	public String translate(Map<String, String> variableMap, String algebra) {		
		String statement = null;
		
		if(StringUtils.isNotEmpty(algebra)) {
			algebra = StringUtils.normalize(algebra);
			char ch = algebra.charAt(0);
			if(ch == AlgebraConstants.SELECT)
				statement = translateSelectStatement(variableMap, algebra);
			else if(ch == AlgebraConstants.PROJECT)
				statement = translateProjectStatement(variableMap, algebra);
			else if(algebra.contains(AlgebraConstants.UNION.toString()))
				statement = translateUnionStatement(variableMap, algebra);
			else if(algebra.contains(AlgebraConstants.INTERSECTION.toString()))
				statement = translateIntersectionStatement(variableMap, algebra);
			else if(algebra.contains(AlgebraConstants.DIFFERENCE.toString()))
				statement = translateDifferenceStatement(variableMap, algebra);
		}		
		return statement;		
	}

	private String translateDifferenceStatement(
			Map<String, String> variableMap, String algebra) {
		String[] parts = algebra.split(AlgebraConstants.DIFFERENCE.toString());
		if(parts.length == 2) {
			String varLeft = parts[0];
			varLeft = StringUtils.normalize(varLeft);
			String left = variableMap.get(varLeft);
			left = StringUtils.normalize(left);
			
			String varRight = parts[1];
			varRight = StringUtils.normalize(varRight);
			String right = variableMap.get(varRight);
			right = StringUtils.normalize(right);
			
			return left + " except " + right;
		}
		
		return null;
	}

	private String translateIntersectionStatement(
			Map<String, String> variableMap, String algebra) {
		String[] parts = algebra.split(AlgebraConstants.INTERSECTION.toString());
		if(parts.length == 2) {
			String varLeft = parts[0];
			varLeft = StringUtils.normalize(varLeft);
			String left = variableMap.get(varLeft);
			left = StringUtils.normalize(left);
			
			String varRight = parts[1];
			varRight = StringUtils.normalize(varRight);
			String right = variableMap.get(varRight);
			right = StringUtils.normalize(right);
			
			return left + " intersect " + right;
		}
		
		return null;
	}

	private String translateUnionStatement(Map<String, String> variableMap,
			String algebra) {
		String[] parts = algebra.split(AlgebraConstants.UNION.toString());
		if(parts.length == 2) {
			String varLeft = parts[0];
			varLeft = StringUtils.normalize(varLeft);
			String left = variableMap.get(varLeft);
			left = StringUtils.normalize(left);
			
			String varRight = parts[1];
			varRight = StringUtils.normalize(varRight);
			String right = variableMap.get(varRight);
			right = StringUtils.normalize(right);
			
			return left + " union " + right;
		}
		
		return null;
	}

	private String translateProjectStatement(Map<String, String> variableMap, String algebra) {
		int indexOfOpenBrace = algebra.indexOf('(');		
		int indexOfCloseBrace = algebra.indexOf(')');
		
		String tableName = algebra.substring(indexOfOpenBrace+1, indexOfCloseBrace);
		tableName = StringUtils.normalize(tableName);
				
		/*
		 * translate projectClause (tableName);
		 */
		String projectClause = algebra.substring(1, indexOfOpenBrace);
		projectClause = StringUtils.normalize(projectClause);
		

		// assume tableName => select (tableName)
		String value = variableMap.get(tableName);
		if(StringUtils.isNotEmpty(value)) {
			//String statement = translateSelectStatement(variableMap, tableName);
			if(StringUtils.isNotEmpty(projectClause))
				return value.replaceFirst("\\*", projectClause); // "select " + projectClause + " (" + value + ")";
			else
				return value;
		} else {
			// assume as a table
			String statement = "select * from " + tableName;
			if(StringUtils.isNotEmpty(projectClause))
				return statement.replaceFirst("\\*", projectClause);
			return statement;
			//TODO: if a assigned variable
		}
		
		//return null;
	}

	private String translateSelectStatement(Map<String, String> variableMap, String algebra) {
				
		int indexOfOpenBrace = algebra.indexOf('(');		
		int indexOfCloseBrace = algebra.indexOf(')');
		
		String tableName = algebra.substring(indexOfOpenBrace+1, indexOfCloseBrace);
		tableName = StringUtils.normalize(tableName);
				
		/*
		 * translate whereClause (tableName);
		 */
		String whereClause = algebra.substring(1, indexOfOpenBrace);
		whereClause = StringUtils.normalize(whereClause);
		
		if(StringUtils.isNotEmpty(whereClause)) {
			String message = translateWhereClause(whereClause);
			if(message != null)				
				return "Select * from "+tableName+" " + message;			
		}
		
		return "Select * from "+tableName;
	}

	private String translateWhereClause(String whereClause) {
		if(StringUtils.isNotEmpty(whereClause))
			return "where "+whereClause;
		return null;
	}
	
}
