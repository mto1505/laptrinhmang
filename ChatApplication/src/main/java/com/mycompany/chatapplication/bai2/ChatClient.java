/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.chatapplication.bai2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author MinhTo
 */// Dữ liệu ghi trên luồng đầu vào của Socker này là dữ liệu sẽ chuyển đi cho luồng đầu ra của Socket kia
//Dữ liệu ghi vào luồng đầu ra trên socket kia sẽ được chuyển vào  đầu vào cho socket này
public class ChatClient {  //Gửi và nhận dũ liệu tại 1 thời điểm

    JTextArea inComing;
    JTextField outComing;

    JFileChooser fileChooser;

    String username;
    String filename;
    byte[] mybyteArr;
    
    
    ObjectOutputStream oos;
    ObjectInputStream ois;
    Socket sock;
    File myFile;
    BufferedReader reader; //luồng đọc dữ liệu từ server
    PrintWriter writer; // luồng ghi dữ liệu đến sever
    OutputStream os;
    Message msg;
    private static final int CHUNK_SIZE = 1024;

    public void go() {
        FormSendMessage frame = new FormSendMessage();
        inComing = frame.getInComing();
        outComing = frame.getOutComing();
        fileChooser = frame.getjFileChooser();

        frame.getBtnSend().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    msg = new Message(1, outComing.getText());
//                    writer.println(msg);
//                    writer.flush();
                    msg.setMessage(sock.getLocalSocketAddress()+": "+ outComing.getText());
                    oos.writeObject(msg);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        frame.getBtnSendFile().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Thread t = new Thread(new UploadFile());
                t.start();
            }
        });
        setUpNetWorking();
        Thread t = new Thread(new IncomingReader());
        t.start();

        frame.setVisible(true);
    }

    public void setUpNetWorking() {
        try {
            //Mở kết nối
            sock = new Socket("localhost", 5000);
            //Mở luồng đầu vào và đầu ra với Socket, 
            //Gửi dữ liệu lên server bằng Output
            //Nhận dữ liệu từ server bằng Input 
            InputStreamReader inputStreamReader = new InputStreamReader(sock.getInputStream());
            reader = new BufferedReader(inputStreamReader);
            writer = new PrintWriter(sock.getOutputStream());
            os = sock.getOutputStream();
            oos = new ObjectOutputStream(sock.getOutputStream());
            System.out.println("networking established");
        } catch (IOException ex) {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public class IncomingReader implements Runnable {

        @Override
        public void run() {
            try {
                //Lấy dữ liệu từ server hiện thị lên màn hình
               String message;
                while ((message = reader.readLine()) != null) 
                {
                    inComing.append(message);
                    inComing.append("\n");
                }

            } catch (Exception ex) {
                Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    public class UploadFile implements Runnable {

        Socket socket;
        ObjectOutputStream objectOutputStream;
        OutputStream outputStream;
        
        public UploadFile() {

        }
        public void setUp() {
            try {
                //Mở kết nối
                socket = new Socket("localhost", 5000);
                outputStream = socket.getOutputStream();
                objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                System.out.println("connecting to server to upload file");
            } catch (IOException ex) {
                Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void run() {
            setUp();
            FileInputStream fos = null;
            try {
                File file = fileChooser.getSelectedFile();
                if (file != null) {
                    //send message
                    Message msg = new Message(2, file.getName());
                    System.out.println("Sending file: " + file.getName());
                    objectOutputStream.writeObject(msg);
                    //send file
                    fos = new FileInputStream(file);
                    int bytesRead;
                    int pos = 0;
                    mybyteArr = new byte[CHUNK_SIZE];
                    while ((bytesRead = fos.read(mybyteArr, 0, CHUNK_SIZE)) >= 0) {
                        outputStream.write(mybyteArr);
                        outputStream.flush();
                        pos += bytesRead;
                        System.out.println(pos + " bytes (" + bytesRead + " bytes read)");
                    }
                    outputStream.close();
                    fos.close();
                    System.out.println("sended file");
                }
            } catch (IOException err) {
                System.out.println("err when reading file " + err.getMessage());
            } finally {
                System.out.println("clean connection");
            }
        }

    }

    public static void main(String[] args) {
        new ChatClient().go();
    }
}
