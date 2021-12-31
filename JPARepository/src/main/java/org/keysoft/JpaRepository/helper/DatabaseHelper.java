package org.keysoft.JpaRepository.helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.keysoft.JpaRepository.consts.SqlStatementEnum;
import org.keysoft.JpaRepository.databaseconfig.DatabaseConfig;


public class DatabaseHelper {
	// max row of request to db
	public static final String CONNECTION_POOL = "100";
	DatabaseConfig dbconfig = new DatabaseConfig();
	public Connection getConnection() {
		Connection conn = null;
		try {
			Class.forName(dbconfig.getCLASSNAME());
			conn = DriverManager.getConnection(dbconfig.getDB_URL(),dbconfig.getUSERNAME(),dbconfig.getPASSWORD());
//			conn = DriverManager.getConnection("jdbc:mysql://localhost:5200/demo_db?autoReconnect=true&useSSL=false&allowPublicKeyRetrieval=true","root","Root@123");
			if (conn != null) {
				System.out.printf("Connection successfully ! , db_url : %s \n , username : %s \n, password : %s \n",dbconfig.getDB_URL() , dbconfig.getUSERNAME(),dbconfig.getPASSWORD());
			}
		} catch (Exception e) {
			System.err.printf("Connection failured ! , db_url : %s , username : %s \n , password : %s \n",dbconfig.getDB_URL() , dbconfig.getUSERNAME(),dbconfig.getPASSWORD());
			e.printStackTrace();
		}
		return conn;
	}
	public Boolean createDb(String dbName) {
		
		try {
			Connection connection  = getConnection() ;
			StringBuilder statement = new StringBuilder() ;
			statement.append(SqlStatementEnum.CREATE_SCHEMA.value).append(SqlStatementEnum.SPACE.value).append(SqlStatementEnum.GRAVE_ACCENT.value)
					.append(dbName.trim()).append(SqlStatementEnum.GRAVE_ACCENT.value).append(SqlStatementEnum.SPACE.value)
					.append(SqlStatementEnum.END_CREATE_SCHEMA.value);
			System.err.println("Executed statement : "+ statement);
			connection.createStatement().execute(statement.toString());
			return true ;
			
		} catch (SQLException e) {
			System.err.println("Create db failed");
			e.printStackTrace();
		}
		return false;
		 ///CREATE SCHEMA `demo_db2` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin ;
	}
	public Boolean dropDb(String dbName) {

		try {
			Connection connection  = getConnection() ;
			StringBuilder statement = new StringBuilder() ;
			statement.append(SqlStatementEnum.DROP_SCHEMA.value).append(SqlStatementEnum.SPACE.value).append(SqlStatementEnum.GRAVE_ACCENT.value)
					.append(dbName).append(SqlStatementEnum.GRAVE_ACCENT.value).append(SqlStatementEnum.END_STATEMENT.value);
			System.err.println("Executed statement : "+ statement);
			connection.createStatement().execute(statement.toString());
			return true ;
			
		} catch (SQLException e) {
			System.err.println("Create db failed");
			e.printStackTrace();
		}
		return false;
		// DROP DATABASE `demo_db`;
		 
	}
	public Boolean dropTable(String tableName) {
		try {
			Connection connection  = getConnection() ;
			StringBuilder statement = new StringBuilder() ;
			statement.append(SqlStatementEnum.DROP_TABLE.value).append(SqlStatementEnum.GRAVE_ACCENT.value).append(tableName).append(SqlStatementEnum.GRAVE_ACCENT.value)
					.append(SqlStatementEnum.END_STATEMENT.value);			
			System.err.println("Executed statement : "+ statement);
			connection.createStatement().execute(statement.toString());
			return true ;
			
		} catch (SQLException e) {
			System.err.println("Create db failed");
			e.printStackTrace();
		}
		// DROP TABLE `demo_db`.`new_table`;
		return false;
	}
	
	
	
}
