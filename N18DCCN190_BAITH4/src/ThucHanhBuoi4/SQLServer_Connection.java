/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ThucHanhBuoi4;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;


public class SQLServer_Connection {
    public Connection getConnection() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String URL = "jdbc:sqlserver://localhost\\DESKTOP-964VM9V:1433;databaseName=KIEMTRALTM;username=sa;password=123456";
            Connection con = DriverManager.getConnection(URL);
            return con;
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.toString(), "Lỗi kết nối", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
}
