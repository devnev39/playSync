package playSync;

import java.io.IOException;
import java.lang.System;
import java.net.*;
import java.util.Scanner;

import Converter.Converter;

public class Main{
    public static void main(String[] args) throws Exception {
        StartServer(8888);
    }

    public static void StartServer(int port) throws IOException{
        try (ServerSocket socket = new ServerSocket(port)) {
            System.out.println("Server Started.....");
            Socket acc = socket.accept();
            System.out.println("Client Connected !");
            while(true){
                Scanner sc = new Scanner(System.in);
                System.out.println("Enter : ");
                String msg = sc.nextLine();
                long t1 = System.currentTimeMillis();
                System.out.println("SEND T : "+t1);
                acc.getOutputStream().write(msg.getBytes());
                byte[] buff = new byte[Long.BYTES];
                acc.getInputStream().read(buff, 0, buff.length);
                long t2d = Converter.BytesToLong(buff);
                long t2 = System.currentTimeMillis();
                System.out.println("From c : "+t2d);
                System.out.println("REC T : "+t2);
                System.out.println("Reaching time : "+(t2d-t1));
                System.out.println("Round time : "+(t2-t1));
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public static <T> void printArray(T[] data){
        for(T i : data){
            System.out.println(i);
        }
    }

    public static void printbArray(byte[] data){
        for(byte b : data){
            System.out.println(b);
        }
    }
}