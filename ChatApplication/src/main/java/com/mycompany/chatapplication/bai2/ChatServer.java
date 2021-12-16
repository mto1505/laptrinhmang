/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.chatapplication.bai2;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MinhTo
 */
public class ChatServer {

    private ArrayList clientOutputStream;
    private static final int CHUNK_SIZE = 10000;
    private static final File downloadDir = new File("downloads/");
    Message msg;

    public class ClientHandler implements Runnable {

        BufferedReader reader;
        Socket socket;
        ObjectInputStream ois;
        ObjectOutputStream oos;
        PrintWriter writer;

        public ClientHandler(Socket socketClient, PrintWriter w) {
            try {
                socket = socketClient;
                ois = new ObjectInputStream(socket.getInputStream());
                InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream()); //đọc Kí Tự từ  luồng Byte từ Client gửi đến
                reader = new BufferedReader(inputStreamReader); // bộ đệm dữ liệu đọc Luồng Kí Tự lưu vào bộ đệm
                writer = w;
            } catch (IOException ex) {
                Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void run() {
            while (true) {

                try {
                    //Đọc message 
                    msg = (Message) ois.readObject();

                    System.out.println("message " + msg.getMessage() + "  -  " + msg.getType());
                    if (msg.getType() == 1) {

                        String message = msg.getMessage();
                        System.out.println("recieveing message " + message);
                        tellAnyOne(message);

                    }
                    if (msg.getType() == 2) {
                        System.out.println("recieving file");
                        Thread t = new Thread(new FileHandler(socket));
                        t.start();
                    }
                } catch (Exception e) {
                    clientOutputStream.remove(writer);
                    System.out.println("Number of current client " + clientOutputStream.size());
                    System.out.println("Can't Read Message Object ");
                    System.out.println("Disconnect with client: SocketPort= " + socket.getPort());

                    break;
                }
//                try {
//                    //đọc dự liệu từ client xong gửi tới các Client khác
//                    //Kiểm tra dữ liệu được send bởi client
//                    //if client send 1, serve will handle message;
//                    //đọc type message
//                    String message;
//                    while ((message = reader.readLine()) != null) {
//                        System.out.println("read " + message);
//                        tellAnyOne(message);
//                    }
//                } catch (IOException ex) {
//                    System.out.println("đóng kết nối " + socket);
//                    System.out.println("so luong hiện tại " + clientOutputStream.size());
//                }

            }
        }

    }

    public class FileHandler implements Runnable {

        Socket socket;
        BufferedInputStream bis;
        InputStream is;
        ObjectInputStream ois;
        PrintWriter writer;

        byte[] mybyteArr = new byte[16 * 1024];

        public FileHandler(Socket socket) {
            try {
                this.socket = socket;
                // ois = new ObjectInputStream(socket.getInputStream());
                writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                //InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream()); 
                //reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            } catch (IOException ex) {
                Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void run() {
            FileOutputStream fos = null;
            BufferedOutputStream bos = null;
            int bytesRead;
            int pos = 0;
            try {
                //BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(msg.getMessage()));
                File file = new File(downloadDir, msg.getMessage());
                fos = new FileOutputStream(file); //ghi file client gửi lên server
                bos = new BufferedOutputStream(fos);
                is = socket.getInputStream();
//              bis=new BufferedInputStream(socket.getInputStream()); 
                int count;
//                        do {
//                            bytesRead
//                                    = is.read(mybyteArr, current, (mybyteArr.length - current));
//                            if (bytesRead >= 0) {
//                                current += bytesRead;
//                            }
//                               System.out.println("File "
//                                + " received (" + current + " bytes read)");
//                        } while (bytesRead >-1);
                while ((count = is.read(mybyteArr)) > 0) {
                    bos.write(mybyteArr, 0, count);
                    bos.flush();
                    System.out.println("File "
                            + " received (" + count + " bytes read)");
                }
                System.out.println("Thanh công");
                bos.close();
                fos.close();
//                        bytesRead = is.read(mybyteArr, 0, (int) file.length());
//                        bos.write(mybyteArr);
//                        bos.flush();
//                        pos += bytesRead;
//                        System.out.println(pos + " bytes (" + bytesRead + " bytes read)");
//                        System.out.println("server receive file cusscess ");
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                System.out.println("read file error " + ex.getMessage());
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException ex) {
                        Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if (bos != null) {
                    try {
                        bos.close();
                    } catch (IOException ex) {
                        Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
//                catch (IOException | ClassNotFoundException e) {
//                    System.out.println("error " + e.getMessage());
//                } finally {
//                    if (fos != null) {
//                        try {
//                            fos.close();
//                        } catch (IOException ex) {
//                            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
//                        }
//                    }
//                    if (bos != null) {
//                        try {
//                            bos.close();
//                        } catch (IOException ex) {
//                            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
//                        }
//                    }
//                }
    }

    public void tellAnyOne(String msg) {
        System.out.println("broadcast");
        Iterator it = clientOutputStream.iterator();

        while (it.hasNext()) {
            try {
                //lấy ra PrintWriter; gưi lại cho Client dữ liệu
                PrintWriter print = (PrintWriter) it.next();
                print.println(msg);
                print.flush();
//                ObjectOutputStream oos=(ObjectOutputStream) it.next();
//                oos.writeObject(msg);

            } catch (Exception ex) {

            }
        }
    }

    public void go() {
        //tạo thư mực lưu file
        if (!downloadDir.exists()) {
            if (downloadDir.mkdir()) {
                System.out.println("location" + downloadDir.getAbsolutePath());
            }
        }
        clientOutputStream = new ArrayList<>();
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(5000);
            while (true) {
                System.out.println("Waiting for connection...");
                Socket sock = serverSocket.accept();//chờ client request và tạo ra 1 socket cho client đó
                PrintWriter writer = new PrintWriter(sock.getOutputStream()); //lấy ra Luồng ghi dữ liệu của socket để ghi dữ liệu vào socket
                // ObjectOutputStream objectOutputStream=new ObjectOutputStream(sock.getOutputStream());

                ClientHandler clientHandler = new ClientHandler(sock, writer);
                clientOutputStream.add(writer);

                Thread t = new Thread(clientHandler);
                t.start();
//                FileHandler fileHandler = new FileHandler(sock);
//                Thread t2 = new Thread(fileHandler);
//                t2.start();
                System.out.println("Got a connection");

            }
//            

        } catch (IOException ex) {
            Logger.getLogger(ChatServer.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        try {
            serverSocket.close();
            for (Iterator iterator = clientOutputStream.iterator(); iterator.hasNext();) {
                ObjectOutputStream next = (ObjectOutputStream) iterator.next();
                next.close();
            }
        } catch (Exception e) {

        }

    }

    public static void main(String[] args) {
        new ChatServer().go();
    }
}
