package com.devpaul.datalogger.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Pauly D on 3/18/2015.
 */
public class FileWriter {

    private WriteFileThread writeFileThread;

    public FileWriter(String fileName) {
        writeFileThread = new WriteFileThread(fileName);
        writeFileThread.start();
    }

    public void writeData(byte[] data) {
        if(writeFileThread != null) {
            writeFileThread.writeData(data);
        }
    }

    public void writeData(String data) {
        if(writeFileThread != null) {
            writeFileThread.writeData(data.getBytes());
        }
    }

    public void closeFile() {
        if(writeFileThread != null) {
            writeFileThread.finish();
        }
    }

    private class WriteFileThread extends Thread {
        private String fileName;
        private File file;
        private FileOutputStream outputStream;

        public WriteFileThread(String name) {
            this.fileName = name;
        }

        @Override
        public void run() {
            this.file = new File(fileName);
            if(file.exists()) file.mkdir();
            try {
                outputStream = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        public void writeData(byte[] bytes) {
            if(outputStream != null) {
                try {
                    outputStream.write(bytes);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void finish() {
            if(outputStream != null) {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}