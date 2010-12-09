package controllers;

import utils.AlgebraConstants;
import utils.SqlConstants;
import utils.StringUtils;

public class SqlToAlgebraTranslator {

	public String translate(String sql) {		
		String algebra = null;
		
		if(StringUtils.isNotEmpty(sql)) {
			sql = StringUtils.normalize(sql);
			if(sql.startsWith("select")){
				algebra = translateSelectStatement(sql);
			}
				
//			char ch = sql.charAt(0);
//			if(ch == AlgebraConstants.SELECT)
//				algebra = translateSelectStatement(sql);
//			else if(ch == AlgebraConstants.PROJECT)
//				algebra = translateProjectStatement(sql);
		}		
		
		algebra = StringUtils.normalize(algebra);		
		return algebra;		
	}

	private String translateProjectClause(String algebra) {
		
		if(algebra.equals(SqlConstants.ALL_COLUMNS))
			return null;
						
		return algebra;
	}

	private String translateSelectStatement(String algebra) {

		int indexFrom = algebra.indexOf(SqlConstants.FROM);
		int indexWhere = algebra.indexOf(SqlConstants.WHERE);
				
		String tableName = null;
		if(indexWhere < 0 )
			tableName = algebra.substring(indexFrom+SqlConstants.FROM.length()+1);
		else 
			tableName = algebra.substring(indexFrom+SqlConstants.FROM.length()+1, indexWhere);
		tableName = StringUtils.normalize(tableName);
				
		/*
		 * translate projectClause tableName;
		 */
		String projectClause = algebra.substring(SqlConstants.SELECT.length() + 1, indexFrom);
		projectClause = StringUtils.normalize(projectClause);
		
		String sql = null;
		
		if (indexWhere > 0) {
			String whereClause = algebra.substring(indexWhere+ 
					SqlConstants.WHERE.length() + 1);
			whereClause = StringUtils.normalize(whereClause);
			if(StringUtils.isNotEmpty(whereClause)) {
				String message = translateWhereClause(whereClause);
				if(message != null)				
					sql = AlgebraConstants.SELECT + " " + message + " ("+tableName+")";
			}
		}
		
		if (StringUtils.isNotEmpty(projectClause)) {
			String message = translateProjectClause(projectClause);
			if (message != null) {
				if (StringUtils.isNotEmpty(sql))
					return AlgebraConstants.PROJECT + " " + message + " ("
							+ sql + ")";
				else
					return AlgebraConstants.PROJECT + " " + message + " ("
							+ tableName + ")";
			} else {
				if (StringUtils.isNotEmpty(sql))
					return sql;
			}
		}
		
		return AlgebraConstants.SELECT+" ("+tableName+")";
	}

	private String translateWhereClause(String whereClause) {
		if(StringUtils.isNotEmpty(whereClause))
			return whereClause;
		return null;
	}
	
}
