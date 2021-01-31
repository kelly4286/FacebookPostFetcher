package com.kelly.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import com.kelly.model.ReturnPostData;

import facebook4j.internal.logging.Logger;

public class DbConnector {
	final static Logger logger = Logger.getLogger(DbConnector.class);
	private Connection con = null;
	private String drivers;			// jdbc driver名稱
	private String connectionURL;	// connection URL
	private String username;		// DB帳號
	private String password;		// DB密碼

	/**
	 * 初始化DB connection 設定
	 */
	public Connection getConnection() {
		try {
			Class.forName(drivers);
			con = DriverManager.getConnection(connectionURL, username, password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (con != null) {
			logger.info("DB Connection Success!");
		} else {
			logger.info("DB Connection Fail!");
		}

		return con;
	}
	/**
	 * 關閉DB連線
	 */
	public void closeConnection() {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 從properties中讀取DB相關設定
	 * 
	 * @param prop properties檔案
	 */
	public DbConnector(Properties prop) {
		drivers = prop.getProperty("jdbc.drivers");
		connectionURL = prop.getProperty("jdbc.url");
		username = prop.getProperty("jdbc.username");
		password = prop.getProperty("jdbc.password");
	}
	/**
	 * 確認某篇貼文是否已存在在DB table中
	 * 
	 * @param id 貼文ID
	 * @return 是否存在
	 */
	public boolean checkPostExist(String id) {
		PreparedStatement statement = null;
		ResultSet rs = null;
		boolean result = false;

		String query = "SELECT id FROM post WHERE id = ?";
		try {
			statement = con.prepareStatement(query);
			statement.setString(1, id);
			rs = statement.executeQuery();
			while (rs.next()) {
				rs.getString("id");
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException logOrIgnore) {
				}
			if (statement != null)
				try {
					statement.close();
				} catch (SQLException logOrIgnore) {
					logOrIgnore.printStackTrace();
				}
		}
		return result;
	}
	/**
	 * insert貼文資料進DB
	 * 
	 * @param data 貼文資料
	 */
	public boolean insertPost(ReturnPostData data) {
		PreparedStatement preparedStmt = null;

		String query = " insert into post (`id`,`message`,`created_time`,`updated_time`,`description`,`from`,`full_picture`,`icon`,`link`,`name`,`picture`,`place`"
				+ ",`privacy`,`source`,`story`,`type`)" + " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try {
			preparedStmt = con.prepareStatement(query);
			preparedStmt.setString(1, data.getFieldValue(Constants.FIELD_NAME_ID));
			preparedStmt.setString(2, data.getFieldValue(Constants.FIELD_NAME_MESSAGE));
			preparedStmt.setDate(3, data.getFieldValueOfDate(Constants.FIELD_NAME_CREATE_TIME));
			preparedStmt.setDate(4, data.getFieldValueOfDate(Constants.FIELD_NAME_UPDATED_TIME));
			preparedStmt.setString(5, data.getFieldValue(Constants.FIELD_NAME_DESCRIPTION));
			preparedStmt.setString(6, data.getFieldValue(Constants.FIELD_NAME_FROM));
			preparedStmt.setString(7, data.getFieldValue(Constants.FIELD_NAME_FULL_PICTURE));
			preparedStmt.setString(8, data.getFieldValue(Constants.FIELD_NAME_ICON));
			preparedStmt.setString(9, data.getFieldValue(Constants.FIELD_NAME_LINK));
			preparedStmt.setString(10, data.getFieldValue(Constants.FIELD_NAME_NAME));
			preparedStmt.setString(11, data.getFieldValue(Constants.FIELD_NAME_PICTURE));
			preparedStmt.setString(12, data.getFieldValue(Constants.FIELD_NAME_PLACE));
			preparedStmt.setString(13, data.getFieldValue(Constants.FIELD_NAME_PRIVACY));
			preparedStmt.setString(14, data.getFieldValue(Constants.FIELD_NAME_SOURCE));
			preparedStmt.setString(15, data.getFieldValue(Constants.FIELD_NAME_STORY));
			preparedStmt.setString(16, data.getFieldValue(Constants.FIELD_NAME_TYPE));

			preparedStmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (preparedStmt != null)
				try {
					preparedStmt.close();
				} catch (SQLException logOrIgnore) {
					logOrIgnore.printStackTrace();
				}
		}
		return true;
	}
	/**
	 * 更新貼文資料
	 * 
	 * @param data 貼文資料
	 */
	public boolean updatePost(ReturnPostData data) {
		PreparedStatement preparedStmt = null;

		String query = " UPDATE `post` SET `message` = ?, `created_time` = ?, `updated_time` = ?, `description` = ?, `from` = ?, `full_picture` = ?,"
				+ "`icon` = ?, `link` = ?, `name` = ?, `picture` = ?, `place` = ?, `privacy` = ?, `source` = ?, `story` = ?, `type` = ? WHERE `id` = ?";

		try {
			preparedStmt = con.prepareStatement(query);
			preparedStmt.setString(1, data.getFieldValue(Constants.FIELD_NAME_MESSAGE));
			preparedStmt.setDate(2, data.getFieldValueOfDate(Constants.FIELD_NAME_CREATE_TIME));
			preparedStmt.setDate(3, data.getFieldValueOfDate(Constants.FIELD_NAME_UPDATED_TIME));
			preparedStmt.setString(4, data.getFieldValue(Constants.FIELD_NAME_DESCRIPTION));
			preparedStmt.setString(5, data.getFieldValue(Constants.FIELD_NAME_FROM));
			preparedStmt.setString(6, data.getFieldValue(Constants.FIELD_NAME_FULL_PICTURE));
			preparedStmt.setString(7, data.getFieldValue(Constants.FIELD_NAME_ICON));
			preparedStmt.setString(8, data.getFieldValue(Constants.FIELD_NAME_LINK));
			preparedStmt.setString(9, data.getFieldValue(Constants.FIELD_NAME_NAME));
			preparedStmt.setString(10, data.getFieldValue(Constants.FIELD_NAME_PICTURE));
			preparedStmt.setString(11, data.getFieldValue(Constants.FIELD_NAME_PLACE));
			preparedStmt.setString(12, data.getFieldValue(Constants.FIELD_NAME_PRIVACY));
			preparedStmt.setString(13, data.getFieldValue(Constants.FIELD_NAME_SOURCE));
			preparedStmt.setString(14, data.getFieldValue(Constants.FIELD_NAME_STORY));
			preparedStmt.setString(15, data.getFieldValue(Constants.FIELD_NAME_TYPE));
			preparedStmt.setString(16, data.getFieldValue(Constants.FIELD_NAME_ID));

			preparedStmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (preparedStmt != null)
				try {
					preparedStmt.close();
				} catch (SQLException logOrIgnore) {
					logOrIgnore.printStackTrace();
				}
		}
		return true;
	}
}
