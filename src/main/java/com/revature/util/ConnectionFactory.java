package com.revature.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Singleton used to set up the connection between this java code and the database.
 */
public class ConnectionFactory {

    private static Connection connection = null;

    private static final Logger log = LoggerFactory.getLogger(ConnectionFactory.class);

    private ConnectionFactory(){super();}

    public static synchronized Connection getConnection() {

        try {
            if(connection != null && !connection.isClosed()){
                return connection;
            }
            //connection = DriverManager.getConnection(System.getenv("db_url"),System.getenv("db_username"),System.getenv("db_password"));
            connection = DriverManager.getConnection("jdbc:postgresql://databank.cbu38mywfjcp.us-east-2.rds.amazonaws.com/postgres","postgres","password");
        } catch (SQLException e) {
            log.error("We failed to reuse a Connection", e);
        }

        Properties prop = new Properties();

        String url = "";
        String username = "";
        String password = "";

        try{
            Class.forName("org.postgresql.Driver");

//            ClassLoader loader = ConnectionUtil.class.getClassLoader();
//            if(loader == null)
//                loader = ClassLoader.getSystemClassLoader();
//            String propFile = "conf/application.properties";
//            java.net.URL jurl = loader.getResource(propFile);
//
//            assert jurl != null;
//            prop.load(jurl.openStream());
//            url = prop.getProperty("url");
//            username = prop.getProperty("username");
//            password = prop.getProperty("password");

            connection = DriverManager.getConnection("jdbc:postgresql://databank.cbu38mywfjcp.us-east-2.rds.amazonaws.com/postgres","postgres","password");
            log.info("Database connection established.");
        } catch(SQLException e){
            log.error("We failed to establish a Connection");
        } catch (ClassNotFoundException e){
            log.error(e.getMessage());
        }

        return connection;
    }

}
