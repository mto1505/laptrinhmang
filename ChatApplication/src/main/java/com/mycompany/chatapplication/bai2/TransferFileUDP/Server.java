/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.chatapplication.bai2.TransferFileUDP;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.nio.file.Path;

/**
 *
 * @author MinhTo
 */
public class Server {

    public static final int PIECES_OF_FILE_SIZE = 1024 * 32;

    private DatagramSocket serverSocket;
    public static final int port = 6677;

    public void openServer() {
        try {
            serverSocket = new DatagramSocket(port);
            System.out.println("server openned");
            byte[] receiveData = new byte[PIECES_OF_FILE_SIZE];
            DatagramPacket recievePacket = null;
            String destionation;
            File file = null;
            while (true) {
                recievePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(recievePacket);
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(receiveData);
                ObjectInputStream ois = new ObjectInputStream(byteArrayInputStream);
                Message message = (Message) ois.readObject();

                if (message != null) {
                    System.out.println("receiving file");
                    //get name file
                    String nameFile = message.getFilename();

                    destionation = message.getDestionationPath();
                    file = new File(destionation, nameFile);
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    FileOutputStream fos = new FileOutputStream(file);
                    BufferedOutputStream bos = new BufferedOutputStream(fos);

                    for (int i = 0; i < message.getPiecesOfFile() - 1; i++) {
                        recievePacket = new DatagramPacket(receiveData, PIECES_OF_FILE_SIZE);
                        serverSocket.receive(recievePacket);
                        bos.write(receiveData, 0, PIECES_OF_FILE_SIZE);
                    }
                    recievePacket = new DatagramPacket(receiveData, receiveData.length);
                    serverSocket.receive(recievePacket);
                    bos.write(receiveData, 0, message.getLastByteLengt());
                    bos.flush();
                    System.out.println("Done!");
                    // close stream
                    byteArrayInputStream.close();

                    bos.close();
                    sapXep(file.getAbsolutePath());
                }

            }

        } catch (SocketException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void convertTo1(int[][] A, int m, int n, int[] B) {
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                B[i * n + j] = A[i][j];
            }
        }
    }

    public void sapXep1Chieu(int[] a, int n) {
        for (int i = 0; i < n - 1; i++) {
            for (int j = n - 1; j > i; j--) {
                if (a[j] < a[j - 1]) {
                    int temp = a[j];
                    a[j] = a[j - 1];
                    a[j - 1] = temp;
                }
            }

        }
    }

    public void convertTo2(int[][] A, int m, int[] B) {
        int col=B.length/m;
         for (int i = 0; i < m; i++) {
            for (int j = 0; j <col ; j++) {
                A[i][j]=B[i*m+j];
            }
        }
    }

    public int[][] sapXep(String pathFile) {
        Path path = Paths.get(pathFile);
        int a[][] = new int[10][10];

        String data = null;
        try {
            BufferedReader bf = new BufferedReader(Files.newBufferedReader(path));
            int count = 0;
            int colNum=0;
            while ((data = bf.readLine()) != null) {
                String[] col = data.split(" ");
                colNum=col.length;
                for (int i = 0; i <colNum; i++) {
                    a[count][i] = Integer.parseInt(col[i]);
                }
                count++;
            }

            //sắp xếp
            int [] b=new int[count*colNum];
            convertTo1(a, count,colNum , b);
            sapXep1Chieu(b, b.length);
            convertTo2(a,count,b);
            
            //lưu lại vào file

        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        return a;
    }

    public static void main(String[] args) {
        Server udpServer = new Server();
        udpServer.openServer();
    }
}
