package ThucHanhBuoi4;

import java.io.*;
import java.net.*;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.*;
import javax.swing.JOptionPane;
import java.sql.Date;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


public class Server {
    
    public static void dangNhap(DataInputStream din, DataOutputStream dout){
        try{
            String username = din.readUTF();
            String password = din.readUTF();
            DBAccess acc = new DBAccess();
            ResultSet rs = acc.Query("select * from SINHVIEN where username = '"+ username +"' and password = '"+ password +"'");
            if(rs.next()){
                ResultSet rsmsv = acc.Query("select * from SINHVIEN where username = '"+ username +"'");
                dout.writeInt(1);
                if(rsmsv.next()){
                    String maSV = rsmsv.getString("MASV");
                    dout.writeUTF(maSV);
                }
            } else {
                dout.writeInt(0);
            }
        }catch(Exception ex){
        }
    }
    
    public static void layTatCaCauHoi(DataInputStream din, DataOutputStream dout){
        try{
            DBAccess acc = new DBAccess();
            ResultSet rs = acc.Query("select top 20 * from [BODE] order by newid()");
            while(rs.next()){
                int cauHoi = rs.getInt("CAUHOI");
                dout.writeInt(cauHoi);
            }
        }catch(Exception ex){
        }
    }
    
    public static void layCauHoi(DataInputStream din, DataOutputStream dout){
        try{
            int cauHoi = din.readInt();
            DBAccess acc = new DBAccess();
            ResultSet rs = acc.Query("select * from [BODE] where cauhoi = " + cauHoi);
            if(rs.next()){
                dout.writeUTF(rs.getString("NOIDUNG"));
                dout.writeUTF(rs.getString("A"));
                dout.writeUTF(rs.getString("B"));
                dout.writeUTF(rs.getString("C"));
                dout.writeUTF(rs.getString("D"));
            }
        }catch(Exception ex){
        }
    }
    
    public static void layKetQua(DataInputStream din, DataOutputStream dout) throws IOException{
        int soCauDung=0;
        double diem;
        String maSV = din.readUTF();
        int size = din.readInt();
        DBAccess acc = new DBAccess();
        for(int i = 0; i < size; i++){
            int cauHoi = din.readInt();
            String dapAn = din.readUTF();
            try{
                ResultSet rs = acc.Query("select * from [BODE] where cauhoi = " + cauHoi + " and dap_an = N'" + dapAn + "'");
                if(rs.next()){
                    soCauDung+=1;
                }
            }catch(Exception ex){
            }
        }
        diem=soCauDung*10/20.0;
        
        dout.writeInt(soCauDung);
        dout.writeDouble(diem);
        LocalDateTime dateTime = LocalDateTime.now();
        Date sqlDate = Date.valueOf(dateTime.toLocalDate());
        int kq = acc.Update("insert into BANGDIEM(MASV,LAN,NGAYTHI,DIEM) values(N'"+maSV+"','1','"+sqlDate+"',"+diem+")");
        if(kq!=0){
            System.out.println("Luu Diem Thanh Cong");
        } else {
            System.out.println("Luu Diem That Bai");
        }

    }
    
//    public static void main(String[] args) throws IOException {
//        ServerSocket server = new ServerSocket(1234);
//        System.out.println("server đã chạy!");
//        
//        while(true){
//            Socket client = server.accept();
//
//            DataInputStream din = new DataInputStream(client.getInputStream());
//            DataOutputStream dout = new DataOutputStream(client.getOutputStream());
//
//            int type = din.readInt();
//
//            switch(type){
//                case 1: dangNhap(din, dout);
//                break;
//                case 2: layTatCaCauHoi(din, dout);
//                break;
//                case 3: layCauHoi(din, dout);
//                break;
//                case 4: layKetQua(din,dout);
//                break;
//            }
//
//            client.close();
//        }
//    }
}
