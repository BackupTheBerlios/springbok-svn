package dao;

import java.sql.Connection;
import java.sql.ResultSet;

import exception.DatabaseException;

public abstract class DatabaseManager {

	/** get a connection */
	public abstract Connection getConnection() throws DatabaseException;

	public abstract void releaseConnection(Connection conn)
			throws DatabaseException;

	public abstract int executeUpdate(Connection conn, String statement)
			throws DatabaseException;

	public abstract ResultSet executeQueryNoParams(Connection conn,
			String statement) throws DatabaseException;
}
