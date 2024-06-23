package com.app.utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

public class DBUtils {
	private static Connection connection;
	private static final String DB_URL;
	private static final String USER_NAME;
	private static final String PASSWORD;

	static {
		DB_URL = "jdbc:mysql://localhost:3306/acts_2024";
		USER_NAME = "root";
		PASSWORD = "Tsaurav@03";
	}

	public static Connection openConnection() throws SQLException {
		connection = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);

		return connection;
	}

	// add a static method to close connection

	public static void closeConnection() throws SQLException {
		if (connection != null)
			connection.close();
		System.out.println("db cn closed");

	}

}