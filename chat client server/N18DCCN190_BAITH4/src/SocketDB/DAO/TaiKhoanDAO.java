/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SocketDB.DAO;

import SocketDB.Mapper.TaiKhoanMapper;
import SocketDB.Model.TaiKhoan;
import java.util.List;

/**
 *
 * @author MinhTo
 */
public class TaiKhoanDAO extends AbstractDAO<TaiKhoan>{
    
    public TaiKhoan findByUsernameAndPassword(String username,String password){
        List<TaiKhoan> taiKhoans=query("Select * from TaiKhoan where taikhoan=? AND matkhau=?",new TaiKhoanMapper(), username,password);
        return taiKhoans.isEmpty()? null : taiKhoans.get(0);
    }
    
}
