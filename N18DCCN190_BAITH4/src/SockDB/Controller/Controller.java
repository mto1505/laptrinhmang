/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SockDB.Controller;

import SockDB.Message.Message;
import SockDB.Message.Response;
import SocketDB.DAO.TaiKhoanDAO;
import SocketDB.Model.TaiKhoan;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author MinhTo
 */
public class Controller {

    private TaiKhoanDAO taiKhoanDAO = new TaiKhoanDAO();
    
    public void dispatcher(Message mess, ObjectOutputStream oos,ObjectInputStream ois,Socket socketClient) {
        if (mess.getType().equals("DANGNHAP")) {
            // TaiKhoan taiKhoan = (TaiKhoan) mess.getObject();
            String userName = mess.getMap().get("username").toString();
            String password = mess.getMap().get("password").toString();
            TaiKhoan taiKhoan = taiKhoanDAO.findByUsernameAndPassword(userName, password);
            if (taiKhoan==null) {
                try {
                    Response response = new Response();
                    response.setObject(taiKhoan);
                    response.setStatus("false");
                    response.setAttribute("message", "Tài khoản hoặc mật khẩu không hợp lệ");
                    oos.writeObject(response);
                } catch (IOException ex) {
                    System.out.println("IO in controller " + ex.getMessage());
                }
            } else {

                try {
                    Response response = new Response();
                    response.setObject(taiKhoan);
                    response.setStatus("true");
                    response.setAttribute("message", "Đăng nhập thành công");
                    oos.writeObject(response);

                } catch (IOException ex) {
                    System.out.println("IO in controller " + ex.getMessage());
                }

            }

        }
 

    }
}
