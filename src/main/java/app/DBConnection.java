package app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // Database credentials and connection string
    private static final String URL = "jdbc:mysql://localhost:3306/be_results_db";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "[PASSWORD]"; // As provided by the user

    /**
     * Establishes and returns a connection to the MySQL database.
     * 
     * @return Connection object
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Load the MySQL JDBC driver (optional for newer JDBC versions, but good practice)
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found. Please ensure it is added to your project dependencies.");
            e.printStackTrace();
        }
        
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}
