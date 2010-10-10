package dao;

import java.io.File;
import java.sql.Connection; //import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern; //import java.sql.SQLException;

import model.Alias;

import org.apache.derby.jdbc.EmbeddedDataSource;

import exception.DatabaseException;

public class InternalDatabaseManager extends DatabaseManager {

	private static EmbeddedDataSource ds;

	public static String REQUESTS_TABLE = "APP.REQUESTS";
	public static String EVENTS_TABLE = "APP.EVENTS";

	// private static InternalDatabaseManager instance = null;

	private static Pattern connectionUrlPattern = null;

	public InternalDatabaseManager(Alias alias) throws DatabaseException {
		validateConnectionUrl(alias.getConnectionUrl());
		String databaseName = extractDatabaseName(alias.getConnectionUrl());
		initDataSource("internalDb" + File.separatorChar + databaseName, alias
				.getProperties().getProperty("user"), alias.getProperties()
				.getProperty("password"));
		// try {
		// createTables();
		// } catch (DatabaseException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}

	// public static InternalDatabaseManager getInstance(Alias alias) throws
	// DatabaseException{
	// validateConnectionUrl(alias.getConnectionUrl());
	// instance = new InternalDatabaseManager(alias);
	// return instance;
	// }

	private static String extractDatabaseName(String connectionUrl) {
		String[] torkens = connectionUrl.split("jdbc:derby:");
		torkens = torkens[1].split(";");
		return torkens[0];
	}

	private static void validateConnectionUrl(String connectionUrl)
			throws DatabaseException {
		if (connectionUrlPattern == null) {
			connectionUrlPattern = Pattern.compile("jdbc:derby:(\\p{Graph})*");
		}
		Matcher match = connectionUrlPattern.matcher(connectionUrl);
		if (!match.find())
			throw new DatabaseException("Invalid Connection URL");
	}

	// We want to keep the same connection for a given thread
	// as long as we're in the same transaction
	// private static ThreadLocal<Connection> tranConnection = new
	// ThreadLocal();

	private void initDataSource(String dbname, String user, String password) {
		ds = new EmbeddedDataSource();
		ds.setDatabaseName(dbname);
		ds.setUser(user);
		ds.setPassword(password);
		ds.setCreateDatabase("create");
	}

	// public static synchronized void beginTransaction() throws Exception {
	// if (tranConnection.get() != null) {
	// throw new Exception("This thread is already in a transaction");
	// }
	// Connection conn = getConnection();
	// conn.setAutoCommit(false);
	// tranConnection.set(conn);
	// }
	//
	// public static void commitTransaction() throws Exception {
	// if (tranConnection.get() == null) {
	// throw new Exception(
	// "Can't commit: this thread isn't currently in a "
	// + "transaction");
	// }
	// tranConnection.get().commit();
	// tranConnection.set(null);
	// }
	//
	// public static void rollbackTransaction() throws Exception {
	// if (tranConnection.get() == null) {
	// throw new Exception(
	// "Can't rollback: this thread isn't currently in a "
	// + "transaction");
	// }
	// tranConnection.get().rollback();
	// tranConnection.set(null);
	// }

	/** get a connection */
	public Connection getConnection() throws DatabaseException {
		try {
			return ds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			new DatabaseException(e.getNextException().getMessage(), e);
		}
		return null;
	}

	public void releaseConnection(Connection conn) throws DatabaseException {
		try {
			conn.close();
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	/**
	 * Helper wrapper around boilerplate JDBC code. Execute a statement that
	 * doesn't return results using a PreparedStatment, and returns the number
	 * of rows affected
	 */
	public int executeUpdate(Connection conn, String statement)
			throws DatabaseException {
		// Connection conn = getConnection();
		try {
			PreparedStatement ps = conn.prepareStatement(statement);			
			return ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DatabaseException(e);
		}
	}

	/**
	 * Helper wrapper around boilerplat JDBC code. Execute a statement that
	 * returns results using a PreparedStatement that takes no parameters
	 * (you're on your own if you're binding parameters).
	 * 
	 * @return the results from the query
	 */
	public ResultSet executeQueryNoParams(Connection conn, String statement)
			throws DatabaseException {
		try {
			PreparedStatement ps = conn.prepareStatement(statement);
			return ps.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DatabaseException(e);
		}
	}

	// private void createTables() throws DatabaseException {
	// System.out.println("Creating tables");
	//	        
	// executeUpdate(
	// "CREATE TABLE " + REQUESTS_TABLE + "(" +
	// "sequence_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, " +
	// "request_type INTEGER, " +
	// "event_id VARCHAR(300), " +
	// "date VARCHAR(20), " +
	// "title VARCHAR(300), " +
	// "edit_url VARCHAR(300))");
	//	                        
	// executeUpdate(
	// "CREATE TABLE " + EVENTS_TABLE + "(" +
	// "event_id VARCHAR(300) PRIMARY KEY, " +
	// "date VARCHAR(20), " +
	// "title VARCHAR(300), " +
	// "edit_url VARCHAR(300), " +
	// "version_id VARCHAR(300))");
	// }

	// /**
	// * Drop the tables. Used mostly for unit testing, to get back
	// * to a clean state
	// */
	// public void dropTables() throws Exception {
	// try {
	// executeUpdate("DROP TABLE " + REQUESTS_TABLE);
	// } catch ( DatabaseException sqle ) {
	// if (! tableDoesntExist(sqle.getMessage())) {
	// throw sqle;
	// }
	// }
	//	        
	// try {
	// executeUpdate("DROP TABLE " + EVENTS_TABLE);
	// } catch ( DatabaseException sqle ) {
	// if (! tableDoesntExist(sqle.getMessage())) {
	// throw sqle;
	// }
	// }
	// }

	private static boolean tableDoesntExist(String sqlState) {
		return sqlState.equals("42X05") || sqlState.equals("42Y55");
	}
}
