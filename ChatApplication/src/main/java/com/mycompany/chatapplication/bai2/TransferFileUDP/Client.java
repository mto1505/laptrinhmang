/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.chatapplication.bai2.TransferFileUDP;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author MinhTo
 */
public class Client {

    private static final int PIECES_OF_FILE_SIZE = 1024 * 32;
    private DatagramPacket datagramPacket;
    private DatagramSocket datagramSocket;
    private InetAddress inetAddress;
    private static int SERVER_PORT = 6677;
    public final static String SERVER_IP = "127.0.0.1";

    public Client() throws SocketException, UnknownHostException {
        datagramSocket = new DatagramSocket();
        inetAddress = InetAddress.getByName(SERVER_IP);
    }

    public void sendFile(String pathFile,String destionationPath) throws FileNotFoundException {
      
        File file = new File(pathFile);
        System.out.println(file.getAbsoluteFile());
       // file.exists();
        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis);
        //GET FILE SIZE
        long fileSize = file.length();
        int pieciesOfFile = (int) (fileSize / PIECES_OF_FILE_SIZE);
        
        int lastByteLenght = (int) (fileSize % PIECES_OF_FILE_SIZE);

        if (lastByteLenght > 0) {
            pieciesOfFile++;
        }
        byte[] bytePart = new byte[PIECES_OF_FILE_SIZE];
        //split file into pieces and assign to fileByte
        
        byte[][] fileBytes = new byte[pieciesOfFile][PIECES_OF_FILE_SIZE];
        int count = 0;

        try {
            while (bis.read(bytePart, 0, PIECES_OF_FILE_SIZE) > 0) {
                fileBytes[count++] = bytePart;
                bytePart = new byte[PIECES_OF_FILE_SIZE];
            }
         
            fis.close();
            bis.close();
              // String to be scanned to find the pattern.
            String line = file.getName();
//            String pattern = "^(\\w+)\\.(\\w+)$";
//            
//            // Create a Pattern object
//            Pattern r = Pattern.compile(pattern);
//
//            // Now create matcher object.
//            Matcher m = r.matcher(line);
//                m.find();
//            //read info file
            Message infoFile=new Message();
            infoFile.setFilename(line);
            
            infoFile.setFileSize(fileSize);
            infoFile.setPiecesOfFile(pieciesOfFile);
            infoFile.setType("file");
            infoFile.setDestionationPath(destionationPath);
            infoFile.setLastByteLengt(lastByteLenght);
            
            //send Message to Server;
            ByteArrayOutputStream out=new ByteArrayOutputStream();
            ObjectOutputStream oos=new ObjectOutputStream(out);
            oos.writeObject(infoFile);
 
            datagramPacket=new DatagramPacket(out.toByteArray(), out.toByteArray().length,inetAddress, SERVER_PORT);
            datagramSocket.send(datagramPacket);
            
            
            //send file
            for(int i=0;i<pieciesOfFile-1;i++)
            {
                datagramPacket=new DatagramPacket(fileBytes[i], PIECES_OF_FILE_SIZE, inetAddress, SERVER_PORT);
                datagramSocket.send(datagramPacket);
            }   
            //send last bytes of file
            datagramPacket=new DatagramPacket(fileBytes[count-1], PIECES_OF_FILE_SIZE, inetAddress, SERVER_PORT);
            datagramSocket.send(datagramPacket);
            
            
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            //close file read/write streams
        }

    }

}
