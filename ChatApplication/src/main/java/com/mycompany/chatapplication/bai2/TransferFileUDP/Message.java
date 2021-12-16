/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.chatapplication.bai2.TransferFileUDP;

import java.io.Serializable;

/**
 *
 * @author MinhTo
 */
public class Message implements Serializable{
        
    private String type;
    private String destionationPath;
    private long fileSize;
    private int piecesOfFile;
    private int lastByteLengt;
    private int status;
    private String filename;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDestionationPath() {
        return destionationPath;
    }

    public void setDestionationPath(String destionationPath) {
        this.destionationPath = destionationPath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public int getPiecesOfFile() {
        return piecesOfFile;
    }

    public void setPiecesOfFile(int piecesOfFile) {
        this.piecesOfFile = piecesOfFile;
    }

    public int getLastByteLengt() {
        return lastByteLengt;
    }

    public void setLastByteLengt(int lastByteLengt) {
        this.lastByteLengt = lastByteLengt;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
   
}
