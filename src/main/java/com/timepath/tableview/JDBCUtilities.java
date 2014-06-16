package com.timepath.tableview;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCUtilities {

    public String dbName;
    public String urlString;

    public JDBCUtilities() throws IOException {
        super();
        this.dbName = "test";
        System.out.println("Set the following properties:");
        System.out.println("dbName: " + dbName);
    }

    public static boolean ignoreSQLException(String sqlState) {
        if(sqlState == null) {
            System.out.println("The SQL state is not defined!");
            return false;
        }
        // X0Y32: Jar file already exists in schema
        // 42Y55: Table already exists in schema
        return sqlState.equalsIgnoreCase("X0Y32") || sqlState.equalsIgnoreCase("42Y55");
    }

    public static void printSQLException(SQLException ex) {
        for(Throwable e : ex) {
            if(e instanceof SQLException) {
                if(!ignoreSQLException(( (SQLException) e ).getSQLState())) {
                    e.printStackTrace(System.err);
                    System.err.println("SQLState: " + ( (SQLException) e ).getSQLState());
                    System.err.println("Error Code: " + ( (SQLException) e ).getErrorCode());
                    System.err.println("Message: " + e.getMessage());
                    Throwable t = ex.getCause();
                    while(t != null) {
                        System.out.println("Cause: " + t);
                        t = t.getCause();
                    }
                }
            }
        }
    }

    public Connection getConnection() throws SQLException {
        this.urlString = "jdbc:derby:" + this.dbName;
        Connection conn = DriverManager.getConnection(urlString + ";create=true");
        System.out.println("Connected to database");
        return conn;
    }
}
