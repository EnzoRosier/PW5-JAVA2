package fr.isen.java2.db.daos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//import javax.sql.DataSource;

//import org.sqlite.SQLiteDataSource;


//ROSIER ENZO

public class DataSourceFactory {

	//private static SQLiteDataSource dataSource;
	private static String connectionUrl;

	private DataSourceFactory() {
		// This is a static class that should not be instantiated.
		// Here's a way to remember it when this class will have 2K lines and you come
		// back to it in 2 years
		throw new IllegalStateException("This is a static class that should not be instantiated");
	}

	/**
	 * @return a connection to the SQLite Database
	 * 
	 */
	// public static DataSource getDataSource() {
	// 	if (dataSource == null) {
	// 		dataSource = new SQLiteDataSource();
	// 		dataSource.setUrl("jdbc:sqlite:sqlite.db");
	// 	}
	// 	return dataSource;
	// }

	/**
	 * @return a connection to the SQLite Database
	 * 
	 */
	public static Connection getConnection() throws SQLException {
		if (connectionUrl == null) {
			throw new SQLException();
		}
		return DriverManager.getConnection(connectionUrl);
	}

	/**
	 * Set the Url of the database
	 * 
	 */
	public static void setConnectionUrl(String connectionUrl) {
		DataSourceFactory.connectionUrl = connectionUrl;
	}
}