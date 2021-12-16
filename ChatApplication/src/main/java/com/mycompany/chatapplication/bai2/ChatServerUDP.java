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
import java.io.FileInputStream;
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
public class ChatServerUDP {

    private ArrayList clientOutputStream;
    private static final int CHUNK_SIZE = 10000;
    private static final File downloadDir = new File("downloads/");
    Message msg;
    public final static byte[] BUFFER = new byte[4096]; // Vùng đệm chứa dữ liệu cho gói tin nhận

    public class ClientUDPHandler implements Runnable {
        
        DatagramPacket datagramPacket;
        DatagramSocket datagramSocket;
        BufferedInputStream bis;
        BufferedOutputStream bos;
        FileOutputStream fos;
//        BufferedReader reader;
//        Socket socket;
//        ObjectInputStream ois;
//        ObjectOutputStream oos;
//        PrintWriter writer;

        public ClientUDPHandler(DatagramSocket datagramSocket) {
            //this.datagramPacket = datagramPacket;
            this.datagramSocket=datagramSocket;
         //   this.datagramPacket= new DatagramPacket(b,b.length);
        }
        @Override
        public void run() {
                while (true) {
                try {
                    //Đọc message 
                
                   {
                        System.out.println("recieving file");
                        Thread t = new Thread(new FileUDPHandler(datagramPacket, datagramSocket));
                        t.start();
                    }
                } catch (Exception e) {
                   // clientOutputStream.remove(writer);
                    System.out.println("Number of current client " + clientOutputStream.size());
                    System.out.println("Can't Read Message Object ");
                    System.out.println("Disconnect with client: SocketPort= " + datagramPacket.getSocketAddress());

                    break;
                }             

            }
        }


    }
 
    
//                

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
        DatagramSocket ds = null;
        try {
            //serverSocket = new ServerSocket(5000);
            System.out.println("Binding to port " + 5000 + ", please wait  ...");
            ds = new DatagramSocket(5000); // Tạo Socket với cổng là 7
            System.out.println("Server started ");
            System.out.println("Waiting for messages from Client ... ");
            while (true) {
                DatagramPacket incoming = new DatagramPacket(BUFFER, BUFFER.length);
                ds.receive(incoming); // Chờ nhận gói tin gởi đến

                // xu ly goi tin nhan
                String kq = new String(incoming.getData(), 0, incoming.getLength());
                System.out.println("Xu ly: " + kq);
                String file = kq.substring(("READ ").length(), kq.length());
                System.out.println("GHI file: " + file);
                // lấy file từ client gưi lên
                
                
//                File docfile = new File(file);
//                if (docfile.exists()) {
//                    FileInputStream fo = new FileInputStream(file);
//                    int n = fo.available();
//                    byte b2[] = new byte[n];
//                    fo.read(b2, 0, n);
//                    // gui file qua server
//                    DatagramPacket dp_send = new DatagramPacket(b2, b2.length, incoming.getAddress(), incoming.getPort());
//                    ds.send(dp_send);
//                    // hien thi thong tin file ra man hinh
//                    System.out.println(new String(dp_send.getData(), 0, dp_send.getLength()));
//                    System.out.println("Gui file thanh cong!!!");
//                    fo.close();
//                } else {
//                    String err_notfound = "Khong tim thay file " + file;
//                    System.out.println(err_notfound);
//                    // Gui thong bao loi sang client
//                    DatagramPacket dp_send_err = new DatagramPacket(err_notfound.getBytes(), err_notfound.length(), incoming.getAddress(), incoming.getPort());
//                    ds.receive(incoming);
//                }
//                ds.close();
                // gui ket qua sau khi xu ly

//                Socket sock = serverSocket.accept();//chờ client request và tạo ra 1 socket cho client đó
//                PrintWriter writer = new PrintWriter(sock.getOutputStream()); //lấy ra Luồng ghi dữ liệu của socket để ghi dữ liệu vào socket
//                 ObjectOutputStream objectOutputStream=new ObjectOutputStream(sock.getOutputStream());
//                
//                ClientHandler clientHandler = new ClientHandler(sock,writer);
//                clientOutputStream.add(writer);
//               
//                Thread t = new Thread(clientHandler);
//                t.start();
//                FileHandler fileHandler = new FileHandler(sock);
//                Thread t2 = new Thread(fileHandler);
//                t2.start();
//                System.out.println("Got a connection");
            }
//              System.out.println("Binding to port " + SERVER_PORT + ", please wait  ...");
//            ds = new DatagramSocket(SERVER_PORT); // Tạo Socket với cổng là 7
//            System.out.println("Server started ");
//            System.out.println("Waiting for messages from Client ... ");
// 
//            while (true) { // Tạo gói tin nhận
//                DatagramPacket incoming = new DatagramPacket(BUFFER, BUFFER.length);
//                ds.receive(incoming); // Chờ nhận gói tin gởi đến
// 
//                // Lấy dữ liệu khỏi gói tin nhận
//                String message = new String(incoming.getData(), 0, incoming.getLength());
//                System.out.println("Received: " + message);
// 
//                // Tạo gói tin gởi chứa dữ liệu vừa nhận được
//                DatagramPacket outsending = new DatagramPacket(message.getBytes(), incoming.getLength(),
//                        incoming.getAddress(), incoming.getPort());
//                ds.send(outsending);

        } catch (IOException ex) {
            Logger.getLogger(ChatServerUDP.class
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
   
public class FileUDPHandler implements Runnable {
     DatagramPacket datagramPacket;
     DatagramSocket datagramSocket;
     BufferedInputStream bis;
     BufferedOutputStream bos;
     FileOutputStream fos;
     byte[] mybyteArr = new byte[16 * 1024];
     

    public FileUDPHandler(DatagramPacket datagramPacket, DatagramSocket datagramSocket) {
        this.datagramPacket = datagramPacket;
        this.datagramSocket = datagramSocket;
        
    }

     
    @Override
    public void run() {
         try {
             //nhận file từ client
             byte b[] = new byte[60000];
             DatagramPacket dp_receive = new DatagramPacket(b,b.length);
             datagramSocket.receive(dp_receive);
             // xu ly goi tin nhan ve
             //---- ghi file
             File file = new File(downloadDir, msg.getMessage());
             fos = new FileOutputStream(file); //ghi file client gửi lên server
             fos.write(b,0,b.length);
             System.out.println("Ghi file thanh cong!!!");
             //---- hien thi thong tin ra man hinh
             System.out.println(new String(dp_receive.getData(),0,dp_receive.getLength()));
         } catch (IOException ex) {
             Logger.getLogger(FileUDPHandler.class.getName()).log(Level.SEVERE, null, ex);
         }
    }}
    public static void main(String[] args) {
        new ChatServerUDP().go();
    }
}
