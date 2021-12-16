/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SocketDB.DAO;

import SocketDB.Mapper.RowMapper;
import java.util.List;

/**
 *
 * @author MinhTo
 */
public interface GenericDAO<T> {
    
    public <T> List<T> query(String sql,RowMapper<T> row,String ...parameter);
    
            
}
