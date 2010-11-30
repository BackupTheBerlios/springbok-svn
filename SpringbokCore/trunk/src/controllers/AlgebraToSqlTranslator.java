package controllers;

public class AlgebraToSqlTranslator {

	public String translate(String algebra) {
		StringBuffer sql = new StringBuffer();
		
		if(algebra != null && algebra.trim().length() != 0) {
			algebra = algebra.replaceAll("\u03C3", "select * from");
			sql.append(algebra);
		}
		
		return sql.toString();
	}
	
}
