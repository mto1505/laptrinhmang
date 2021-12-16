/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.chatapplication.bai2;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MinhTo
 */
public class FileUDPHandler implements Runnable {
     DatagramPacket datagramPacket;
     DatagramSocket datagramSocket;
     BufferedInputStream bis;
     BufferedOutputStream bos;
     FileOutputStream fos;
     byte[] mybyteArr = new byte[16 * 1024];
         private static final File downloadDir = new File("downloads/");

    public FileUDPHandler(DatagramSocket datagramSocket) {
        this.datagramPacket = datagramPacket;
    }
    @Override
    public void run() {
          FileOutputStream fos = null;
          BufferedOutputStream bos = null;
         try {
             //nhận file từ client
             byte b[] = new byte[60000];
             datagramPacket = new DatagramPacket(b,b.length);
             datagramSocket.receive(datagramPacket);
             // xu ly goi tin nhan ve
             //---- ghi file
//           File file = new File(downloadDir, msg.getMessage());
//           fos = new FileOutputStream(file); //ghi file client gửi lên server
             fos = new FileOutputStream(downloadDir);
             bos=new BufferedOutputStream(fos);
             fos.write(b,0,b.length);
           
             System.out.println("Ghi file thanh cong!!!");
             //---- hien thi thong tin ra man hinh
             System.out.println(new String(datagramPacket.getData(),0,datagramPacket.getLength()));
         } catch (IOException ex) {
             Logger.getLogger(FileUDPHandler.class.getName()).log(Level.SEVERE, null, ex);
         }
    }
}
