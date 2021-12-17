/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SocketDB.Server;

import SocketDB.Controller.Controller;
import SocketDB.Message.Message;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MinhTo
 */
public class Server {

    private Map<String, Object> mapClient = new HashMap<>();
    private Socket socketClient;
    private static final int SERVER_PORT = 7766;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private Controller controller = new Controller();

    public Server() {

    }

//    public void handleClient(Socket socket) {
//        try {
//            this.socketClient = socket;
//            oos = new ObjectOutputStream(socket.getOutputStream());
//            ois = new ObjectInputStream(socket.getInputStream());
//
//            Message msg = (Message) ois.readObject();
//            System.out.println("message " + msg.getType());
//            controller.dispatcher(msg, oos);
//
//             
//        } catch (IOException ex) {
//         
//        } catch (ClassNotFoundException ex) {
//            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        finally {
////            try {
////                ois.close();
////                oos.close();
////                socketClient.close();
////                String client=socketClient.getInetAddress().toString()+":"+socketClient.getPort();
////                mapClient.remove(client);
////                System.out.println("client "+client +" removed");
////            } catch (IOException ex) {
////                System.out.println("error when close stream");
////            }
//            }
//
//    }
    public void run() {
        try {
            ServerSocket socketServer = null;
            socketServer = new ServerSocket(SERVER_PORT);
            while (true) {
                System.out.println("Waiting for connection...");
                socketClient = socketServer.accept();
                System.out.println("Got a connection");
                String client = socketClient.getInetAddress().toString() + ":" + socketClient.getPort();
                mapClient.put(client, socketClient);
                System.out.println(client + " connecting");
                ClientHandler clientHandler = new ClientHandler(controller, socketClient, mapClient);
                Thread t = new Thread(clientHandler);
                t.start();
            }
        } catch (IOException ex) {

            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void main(String[] args) {
        Server server = new Server();
        server.run();
    }
}
