/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SocketDB.Mapper;

import SocketDB.Model.TaiKhoan;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MinhTo
 */
public class TaiKhoanMapper implements RowMapper<TaiKhoan>{

    @Override
    public TaiKhoan getRow(ResultSet rs) {
        try {
            TaiKhoan taiKhoan=new TaiKhoan();
            taiKhoan.setUsername(rs.getString(1));
            taiKhoan.setPasswordl(rs.getString(2));
            return taiKhoan;
        } catch (SQLException ex) {
            System.out.println("SQL EXCEPTION IN MAPPER");
        }
        return null;
    }
    
}
