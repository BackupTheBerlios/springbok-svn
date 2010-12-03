package model;

/**
 * Data model to store query result
 * 
 * @author Hirantha Bandara
 * 
 */
public class DataModel {

	private String name;
	private String[] columnNames;
	private String[][] data;
	private String query;

	/**
	 * Returns table name
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets table name
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns column names
	 * 
	 * @return
	 */
	public String[] getColumnNames() {
		return columnNames;
	}

	/**
	 * Sets column names
	 * 
	 * @param columnNames
	 */
	public void setColumnNames(String[] columnNames) {
		this.columnNames = columnNames;
	}

	/**
	 * Returns query results
	 * 
	 * @return
	 */
	public String[][] getData() {
		return data;
	}

	/**
	 * Sets query results
	 * 
	 * @param data
	 */
	public void setData(String[][] data) {
		this.data = data;
	}

	/**
	 * Returns the query
	 * 
	 * @return
	 */
	public String getQuery() {
		return query;
	}

	/**
	 * Sets query
	 * 
	 * @param query
	 */
	public void setQuery(String query) {
		this.query = query;
	}

}
