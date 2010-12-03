package controllers;

import utils.AlgebraConstants;
import utils.StringUtils;

public class AlgebraToSqlTranslator {

	public String translate(String algebra) {		
		String message = null;
		
		if(StringUtils.isNotEmpty(algebra)) {
			algebra = StringUtils.normalize(algebra);
			char ch = algebra.charAt(0);
			if(ch == AlgebraConstants.SELECT)
				message = translateSelectStatement(algebra);
		}		
		return message;		
	}

	private String translateSelectStatement(String algebra) {
				
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
