package me.deadorfd.videos.utils.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @Author DeaDorfd
 * @Project videos
 * @Package me.deadorfd.videos.utils.sql
 * @Date 20.11.2023
 * @Time 01:02:55
 */
public class SQLite {

	public static Connection conn;

	public static void connect() {
		if (isConnected()) return;
		try {
			conn = DriverManager.getConnection(
					"jdbc:sqlite:" + System.getenv("APPDATA") + "/.videos/videos.db");
			createTable();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void disconnect() {
		if (conn != null) try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static boolean isConnected() {
		return conn != null;
	}

	private static void createTable() {
		try {
			conn.prepareStatement(
					"CREATE TABLE IF NOT EXISTS Favorites(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, VideoPath VARCHAR(100));")
					.executeUpdate();
			conn.prepareStatement(
					"CREATE TABLE IF NOT EXISTS History(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, VideoPath VARCHAR(100));")
					.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void update(String qry) {
		try {
			Statement st = conn.createStatement();
			st.executeUpdate(qry);
			st.close();
		} catch (SQLException e) {
			connect();
			System.err.println(e);
		}
	}

	public static ResultSet getResult(String qry) {
		if (!isConnected()) return null;
		try {
			return conn.createStatement().executeQuery(qry);
		} catch (SQLException e) {
			connect();
			e.printStackTrace();
		}
		return null;
	}

}
