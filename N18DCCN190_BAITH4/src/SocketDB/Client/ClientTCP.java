/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SocketDB.Client;

import SockDB.Message.Message;
import SockDB.Message.Response;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MinhTo
 */
public class ClientTCP {

    private Socket socket;
    private static final int SERVER_PORT = 7766;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    public void setUpNetworking() throws IOException {
        socket = new Socket("localhost", SERVER_PORT);
        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());
        System.out.println("Connection established");

    }

    public void sendRequest(Message message) throws IOException {
        oos.writeObject(message);

    }

//    public Response recieveResponseDangNhap() {
//        try {
//            Response response = (Response) ois.readObject();
//            return response;
//        } catch (IOException ex) {
//            Logger.getLogger(ClientTCP.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (ClassNotFoundException ex) {
//            Logger.getLogger(ClientTCP.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        return null;
//    }

    public Response dangNhap(String username, String matkhau) {
        try {
            Message message = new Message();
            message.setType("DANGNHAP");
            message.addAttribute("username", username);
            message.addAttribute("password", matkhau);
            //send to server
            System.out.println("sending req");
            oos.writeObject(message);
            System.out.println("sended ");
            // get response form server\
            System.out.println("waiting response");
            Response response = new Response();
            response = (Response) ois.readObject();
            System.out.println("recieved response");
            return response;

        } catch (IOException ex) {
            System.out.println("error in dangnhap " + ex.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClientTCP.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
//            try {
//                ois.close();
//                oos.close();
//            } catch (IOException ex) {
//                Logger.getLogger(ClientTCP.class.getName()).log(Level.SEVERE, null, ex);
//            }
        }
        return null;

    }

    public void close() {
        try {
            Message message=new Message();
            message.setType("EXIT");
            //send to server
            System.out.println("SENDING REQ EXIT");
            oos.writeObject(message);
        } catch (IOException ex) {
            Logger.getLogger(ClientTCP.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
