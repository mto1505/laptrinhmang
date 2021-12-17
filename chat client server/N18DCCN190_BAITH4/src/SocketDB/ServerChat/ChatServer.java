/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SocketDB.ServerChat;

import SocketDB.Message.Message;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MinhTo
 */
public class ChatServer {

    private List clients = new ArrayList();
    private static final int SERVER_PORT = 6677;

//    private BufferedInputStream bis;
//    private BufferedOutputStream bos;
//
//    private ObjectOutputStream oos;
//    private ObjectInputStream ois;

    public ChatServer() {
    }

    public void run() {
        try {
            ServerSocket socket = new ServerSocket(SERVER_PORT);
            System.out.println("Server running at port: " + SERVER_PORT);
            while (true) {
                System.out.println("Wating connection");
                Socket clientSocket = socket.accept();
                System.out.println("Get a connection");

                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                clients.add(bufferedWriter);

                MessageClientHandler messageClientHandler = new MessageClientHandler(clientSocket, bufferedWriter);
                Thread t = new Thread(messageClientHandler);
                t.start();

            }
        } catch (IOException ex) {
            System.out.println("error " + ex.getMessage());
        } finally {

        }
    }

    public class MessageClientHandler implements Runnable {

        private Socket socketClient;
        private BufferedWriter writer;
        private BufferedReader reader;

        public MessageClientHandler(Socket socket, BufferedWriter writer) {
            try {
                this.socketClient = socket;
                this.writer = writer;
                reader = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
            } catch (IOException ex) {
                Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void run() {
            while (true) {
                try {
                    System.out.println("waiting message from client");
                    //get message from client
                    String bufferMessage = "";
                    String message = null;
                    message = reader.readLine();
                    bufferMessage = bufferMessage.concat(message);
                    //send to all client
                    System.out.println("sended to anyone");
                    tellAnyOne(bufferMessage);

                } catch (IOException e) {
                    System.out.println("Error " + e.getMessage());
                    break;
                }
            }
            try {
                System.out.println("close read/write stream");
                reader.close();
                writer.close();
                clients.remove(writer);
            } catch (IOException ex) {
                Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void tellAnyOne(String message) throws IOException {
        for (Object writer : clients) {
            BufferedWriter printWrite = (BufferedWriter) writer;
            printWrite.write(message);
            printWrite.newLine();
            printWrite.flush();

        }
    }

    public static void main(String[] args) {
        ChatServer chatServer = new ChatServer();
        chatServer.run();
    }

}
