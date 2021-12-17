/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SocketDB.DBUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MinhTo
 */
public class DatabaseUtil {

    public static String url = "jdbc:sqlserver://MinhTo-PC\\SQLMINHTO;databaseName=TESTSOCKET";
    public static String username = "minhto";
    public static String password = "minhto123";
    public static String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    public static Connection con = null;

    static {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DatabaseUtil.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static Connection getConnection() throws SQLException {
        if (con != null) {
            return con;
        } else {
            con = DriverManager.getConnection(url, username, password);
        }
        return con;
    }
}
