package com.revature.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton used to set up the connection between this java code and the database.
 */
public class ConnectionFactory {

    private static Connection connection;

    public static Connection getConnection() {

        try {
            //connection = DriverManager.getConnection(System.getenv("db_url"),System.getenv("db_username"),System.getenv("db_password"));
            connection = DriverManager.getConnection("jdbc:postgresql://databank.cbu38mywfjcp.us-east-2.rds.amazonaws.com/postgres","postgres","password");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connection;
    }

}
