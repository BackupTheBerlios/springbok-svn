package controllers;

import utils.AlgebraConstants;
import utils.SqlConstants;
import utils.StringUtils;

public class SqlToAlgebraTranslator {

	public String translate(String sql) {		
		String algebra = null;
		
		if(StringUtils.isNotEmpty(sql)) {
			sql = StringUtils.normalize(sql);
			if (sql.contains("union")) {
				algebra = translateUnionStatement(sql);
			} else if (sql.contains("intersect")) {
				algebra = translateIntersectStatement(sql);
			} else if (sql.contains("except")) {
				algebra = translateExceptStatement(sql);
			} else if(sql.startsWith("select")){
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

	private String translateExceptStatement(String sql) {
		String[] parts = sql.split("except");
		if(parts.length == 2) {
			String varLeft = parts[0];
			varLeft = StringUtils.normalize(varLeft);
			String left = translate(varLeft);
			left = StringUtils.normalize(left);
			
			String varRight = parts[1];
			varRight = StringUtils.normalize(varRight);
			String right = translate(varRight);
			right = StringUtils.normalize(right);
			
			return left + " "+ AlgebraConstants.DIFFERENCE +" " + right;
		}
		return null;
	}

	private String translateIntersectStatement(String sql) {
		String[] parts = sql.split("intersect");
		if(parts.length == 2) {
			String varLeft = parts[0];
			varLeft = StringUtils.normalize(varLeft);
			String left = translate(varLeft);
			left = StringUtils.normalize(left);
			
			String varRight = parts[1];
			varRight = StringUtils.normalize(varRight);
			String right = translate(varRight);
			right = StringUtils.normalize(right);
			
			return left + " "+ AlgebraConstants.INTERSECTION +" " + right;
		}
		return null;
	}

	private String translateUnionStatement(String sql) {
		String[] parts = sql.split("union");
		if(parts.length == 2) {
			String varLeft = parts[0];
			varLeft = StringUtils.normalize(varLeft);
			String left = translate(varLeft);
			left = StringUtils.normalize(left);
			
			String varRight = parts[1];
			varRight = StringUtils.normalize(varRight);
			String right = translate(varRight);
			right = StringUtils.normalize(right);
			
			return left + " "+ AlgebraConstants.UNION +" " + right;
		}
		return null;
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
