package ThucHanhBuoi4;

import java.sql.*;

public class DBAccess {

    private Connection con;
    private Statement stmt;

    public DBAccess() {
        try 
        {
           SQLServer_Connection myCon = new SQLServer_Connection();
            con = myCon.getConnection();
            stmt = con.createStatement();
        } catch (Exception e) {
        }
    }

    public int Update(String str) {
        try 
        {
            int i = stmt.executeUpdate(str);
            return i;
        } catch (Exception e) {
            return -1;
        }
    }

    public ResultSet Query(String str) {
        try 
        {
            ResultSet rs = stmt.executeQuery(str);
            return rs;
        } catch (Exception e) {
            return null;
        }
    }
}
