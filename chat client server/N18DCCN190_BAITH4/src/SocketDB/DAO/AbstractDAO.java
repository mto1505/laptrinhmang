/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SocketDB.DAO;

import SocketDB.DAO.GenericDAO;
import SocketDB.DBUtil.DatabaseUtil;
import SocketDB.Mapper.RowMapper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MinhTo
 */
public class AbstractDAO<T> implements GenericDAO<T> {

    public Connection getConnection() {
        try {
            Connection con = DatabaseUtil.getConnection();
            System.out.println("connected successfully");
            return con;
        } catch (SQLException ex) {
            Logger.getLogger(AbstractDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public <T> List<T> query(String sql, RowMapper<T> row, String... parameter) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<T> results = new ArrayList<>();
        Connection con = null;

        try {
            con = getConnection();
            ps = con.prepareStatement(sql);
            setParameter(ps, parameter);
            rs = ps.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getString(1));
                System.out.println(rs.getString(2));
                results.add(row.getRow(rs));
            }
            return results;

        } catch (SQLException ex) {
            Logger.getLogger(AbstractDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    public void setParameter(PreparedStatement ps, String... parameter) {
        try {

            for (int i = 0; i < parameter.length; i++) {
                int index = i + 1;
                Object param = parameter[i];
                if (param instanceof String) {
                    ps.setString(index, (String) param);
                } else if (param instanceof Integer) {
                    ps.setInt(index, (int) param);
                } else if (param instanceof Long) {
                    ps.setLong(index, (long) param);
                }
            }

        } catch (SQLException e) {

        }

    }

}
