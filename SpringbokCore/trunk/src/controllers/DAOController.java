package controllers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.Alias;
import model.DataModel;
import model.Result;
import dao.DatabaseManager;
import dao.InternalDatabaseManager;
import exception.CoreException;
import exception.DatabaseException;

public class DAOController {

	private static DatabaseManager databaseManager = null;
	private static DAOController instance = null;
	private static final Pattern selectPattern = Pattern
			.compile("[Ss][Ee][Ll][Ee][Cc][Tt]\\p{Graph}*");

	public static DAOController getInstance(Alias alias) throws CoreException {
		if (instance == null) {
			try {
				// based on the alias use appropriate data base manager
				databaseManager = new InternalDatabaseManager(alias);
				instance = new DAOController();
			} catch (DatabaseException e) {
				// throw Exception (specific to ui) to block the remaining
				// process
				e.printStackTrace();
				throw new CoreException(e);
			}

		}
		return instance;

	}

	public Result runStatement(String statement) throws CoreException {
		// try to find executeUpdate or executeQuery using a regx pattern
		Result result = new Result();
		Matcher matcher = selectPattern.matcher(statement);
		if (matcher.find()) {
			ResultSet resultSet = executeQueryNoParams(statement);
			DataModel dataModel = convertResultSetToDataModel(resultSet);
			result.setDataModel(dataModel);
		} else {
			int status = executeUpdate(statement);
			result.setStatus(status);
		}

		return result;
	}

	// private static void setDatabaseManager(DatabaseManager databaseManager) {
	// this.databaseManager =
	// }

	private DataModel convertResultSetToDataModel(ResultSet resultSet) {
		DataModel dataModel = new DataModel();
		if (resultSet != null) {

			try {
				// resultSet.next();
				ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
				dataModel.setName(resultSetMetaData.getTableName(1));
				int columnCount = resultSetMetaData.getColumnCount();
				
				Vector<String> columnNames = new Vector<String>();
				for (int i = 1; i < columnCount + 1; i++) {
					String columnName = resultSetMetaData.getColumnName(i);
					columnNames.add(columnName);
				}
				
				List<Vector<String>> rows = new ArrayList<Vector<String>>();				
				int rowCount = 0;
				while(resultSet.next()){
					rowCount ++;
					Vector<String> columnValues = new Vector<String>();
					for(int i=1;i<columnCount+1; i++) {
						columnValues.add(String.valueOf(resultSet.getObject(i)));
					}
					rows.add(columnValues);
				}
				String[][] data = new String[rows.size()][]; 
				for(int i=0;i<rows.size();i++){
					data[i] = rows.get(i).toArray(new String[]{});
				}
				
				dataModel.setColumnNames(columnNames.toArray(new String[]{}));
				dataModel.setData(data);
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return dataModel;
	}

	private int executeUpdate(String statement) throws CoreException {
		Connection conn = null;
		try {
			conn = getConnection();
			return databaseManager.executeUpdate(conn, statement);
			// return a data structure
		} catch (DatabaseException e) {
			// throw Exception specific to ui
			e.printStackTrace();
			throw new CoreException(e);
		} finally {
			releaseConnection(conn);
		}
	}

	private ResultSet executeQueryNoParams(String statement)
			throws CoreException {
		Connection conn = null;
		try {
			conn = getConnection();
			return databaseManager.executeQueryNoParams(conn, statement);
			// return a data structure
		} catch (DatabaseException e) {
			// throw Exception specific to ui
			e.printStackTrace();
			throw new CoreException(e);
		} finally {
			// releaseConnection(conn);
		}
	}

	private Connection getConnection() throws CoreException {
		try {
			return databaseManager.getConnection();
			// return a data structure
		} catch (DatabaseException e) {
			// throw Exception specific to ui
			e.printStackTrace();
			throw new CoreException(e);
		}
	}

	private void releaseConnection(Connection conn) throws CoreException {
		try {
			databaseManager.releaseConnection(conn);
			// return a data structure
		} catch (DatabaseException e) {
			// throw Exception specific to ui
			e.printStackTrace();
			throw new CoreException(e);
		}
	}
	// private DatabaseManager getDatabaseManager(Alias alias)
	// throws DatabaseException {
	// // check the driver type and return appropriate
	//
	// return InternalDatabaseManager.getInstance(alias);
	//
	// }

}
