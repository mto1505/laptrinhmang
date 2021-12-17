/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SocketDB.ClientChat;

import SocketDB.View.FormMessage;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextField;

/**
 *
 * @author MinhTo
 */
public class ClientChat {

    private Socket socket;
    private static final int SERVER_PORT = 6677;
    ObjectInputStream ois;
    ObjectOutputStream oos;
    String messageRecived;
    FormMessage formMessage;
    private BufferedWriter writer;

    public ClientChat() {

        formMessage = new FormMessage();

    }

    public void go() {
        try {
            socket = new Socket("localhost", SERVER_PORT);
//            this.ois = new ObjectInputStream(socket.getInputStream());
//            this.oos = new ObjectOutputStream(socket.getOutputStream());
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            System.out.println("Connected to server");

            formMessage.getBtnSendMessage().addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        String message = formMessage.getTfOutgoing().getText();
                        writer.write(message);
                        writer.newLine();
                        writer.flush();
                        System.out.println("sended message");
                    } catch (IOException ex) {
                        Logger.getLogger(ClientChat.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            ImcomingMessageHandler imcomingMessageHandler = new ImcomingMessageHandler();
            Thread t = new Thread(imcomingMessageHandler);
            t.start();
            formMessage.setVisible(true);

        } catch (IOException ex) {
            Logger.getLogger(ClientChat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public class ImcomingMessageHandler implements Runnable {

        private BufferedReader reader;

        public ImcomingMessageHandler() {
            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException ex) {
                Logger.getLogger(ClientChat.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void run() {
            while (true) {
                System.out.println("Waiting message incoming");
                try {
                    messageRecived = formMessage.getIncoming().getText();
                    String message = null;
                    message = reader.readLine();
                    messageRecived = messageRecived.concat(message);
                    messageRecived = messageRecived.concat("\n");
                    formMessage.getIncoming().setText(messageRecived);

                } catch (IOException ex) {
                    Logger.getLogger(ClientChat.class.getName()).log(Level.SEVERE, null, ex);
                    break;
                }
            }
            try {
                reader.close();

            } catch (IOException e) {
            }

        }

    }

    public static void main(String[] args) {
        ClientChat clientChat = new ClientChat();
        clientChat.go();
    }

}
