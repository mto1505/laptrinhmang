/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SocketDB.Model;

import java.io.Serializable;

/**
 *
 * @author MinhTo
 */
public class TaiKhoan implements Serializable{
    public String username;
    public String passwordl;

    public TaiKhoan() {
    }

    public TaiKhoan(String username, String passwordl) {
        this.username = username;
        this.passwordl = passwordl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordl() {
        return passwordl;
    }

    public void setPasswordl(String passwordl) {
        this.passwordl = passwordl;
    }
    
    
}
