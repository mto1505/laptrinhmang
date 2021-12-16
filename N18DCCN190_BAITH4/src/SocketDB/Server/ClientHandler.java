/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SocketDB.Server;

import SockDB.Controller.Controller;
import SockDB.Message.Message;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MinhTo
 */
public class ClientHandler implements Runnable {

    private Controller controller;
    private Socket socketClient;
    BufferedReader reader;
    ObjectInputStream ois;
    ObjectOutputStream oos;
    PrintWriter writer;
    private Map<String, Object> mapClient;

    public ClientHandler(Controller controller, Socket socketClient, Map<String, Object> mapClient) {
        try {
            this.controller = controller;
            this.socketClient = socketClient;
            this.ois = new ObjectInputStream(socketClient.getInputStream());
            this.oos = new ObjectOutputStream(socketClient.getOutputStream());
            this.mapClient = mapClient;
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {

        while (true) {
            try {
                Message msg = (Message) ois.readObject();
                if (msg.getType().equals("EXIT")) {
                    String client = socketClient.getInetAddress().toString() + ":" + socketClient.getPort();
                    System.out.println("disconnect client  " + client);

                    mapClient.remove(client);
                    System.out.println("client còn lại " + mapClient.size());
                    break;
                } else {
                    System.out.println("message " + msg.getType());
                    controller.dispatcher(msg, oos, ois, socketClient);
                }
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
                break;
            }
        }
        try {
            ois.close();
            oos.close();
        } catch (IOException ex) {
            System.out.println("error when closing stream at clientHandler");
        }
    }

}
