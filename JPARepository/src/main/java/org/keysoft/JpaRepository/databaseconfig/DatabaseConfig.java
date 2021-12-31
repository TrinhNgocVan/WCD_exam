package org.keysoft.JpaRepository.databaseconfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.keysoft.JpaRepository.helper.DatabaseHelper;

public class DatabaseConfig {
	private   String DB_URL;
    private   String USERNAME ;
    private  String   PASSWORD ;
    private String 	CLASSNAME ;
   
    public String getCLASSNAME() {
    	return CLASSNAME ;
    }

    public  String getDB_URL() {
        return DB_URL;
    }

    public  String getUSERNAME() {
        return USERNAME;
    }

    public  String getPASSWORD() {
        return PASSWORD;
    }

	public DatabaseConfig() {
		super();
    	ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    	InputStream input = classLoader.getResourceAsStream("application.properties");    	
    	try {
    	Properties properties = new Properties() ;
    	properties.load(input);
    	String dbName = properties.getProperty("database.name").trim();
    	String dbHost = properties.getProperty("database.host").trim();
    	String dbPort = properties.getProperty("database.port").trim();
    	String dbAction = properties.getProperty("database.action").trim();
    	if(StringUtils.isNotBlank(dbName)) {
    		StringBuilder dbNameString  = new StringBuilder();
    		if(dbAction.toLowerCase().equals("create")) {
    			dbNameString.append("jdbc:mysql://").append(dbHost.trim()).append(":").append(dbPort).append("?autoReconnect=true&useSSL=false&allowPublicKeyRetrieval=true");  			
    		}else {
    			dbNameString.append("jdbc:mysql://").append(dbHost.trim()).append(":").append(dbPort).append("/")
				.append(dbName).append("?autoReconnect=true&useSSL=false&allowPublicKeyRetrieval=true");
    		}   		
    		DB_URL = dbNameString.toString();
    	}else {
    		StringBuilder dbNameString  = new StringBuilder();
    		dbNameString.append("jdbc:mysql://").append(dbHost.trim()).append(":").append(dbPort).append("?autoReconnect=true&useSSL=false&allowPublicKeyRetrieval=true");
    		DB_URL = dbNameString.toString();
    	}
//    	#database.url = jdbc:mysql://localhost:5200?autoReconnect=true&useSSL=false&allowPublicKeyRetrieval=true
    	USERNAME = properties.getProperty("database.root-user").trim();
    	PASSWORD = properties.getProperty("database.root-password").trim();  
    	CLASSNAME = properties.getProperty("database.driver-class-name").trim();
		} catch (IOException e ) {
			System.err.println("Cannot read file properties \n");
			e.printStackTrace();			
		}
	}
}
