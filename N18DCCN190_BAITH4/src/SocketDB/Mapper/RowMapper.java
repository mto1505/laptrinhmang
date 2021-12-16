/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SocketDB.Mapper;

import java.sql.ResultSet;

/**
 *
 * @author MinhTo
 */
public interface RowMapper<T> {
    
    public T getRow(ResultSet rs);
           
    
}
