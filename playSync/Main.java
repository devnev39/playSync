package playSync;

import java.io.IOException;
import java.lang.System;
import java.net.*;
import java.util.Scanner;

import CDC.CDC;
import Converter.Converter;
import testPlayer.*;

public class Main{

    static ServerSocket socket;
    static Socket acc;
    public static void main(String[] args) throws Exception {
        // // StartServer(8888);
        // displayPlayer();
        startServer(8888);
        // startAccept();
        CDC.setAveragingConstant(10);
        CDC.CalculateDelayServer(acc);
        System.out.println("Play Delay : "+CDC.playDelay());
        System.out.println("Pause Delay : "+CDC.pauseDelay());
    }

    public static void displayPlayer() throws Exception{
        Player player = new Player("test.wav");
        while(true){
            System.out.println("1.Play\n2.Pause\n3.Seek\n4:Quit");
            Scanner sc = new Scanner(System.in);
            int choice = sc.nextInt();
            switch(choice){
                case 1:
                    player.Play();
                    break;
                case 2:
                    player.Pause();
                    break;
                case 3:
                    player.Seek();
                    break;
                case 4:
                    player.Quit();
                    return;
                default:
                    System.out.println("Wrong choice !");
                    break;
            }
        }
    }

    public static void startServer(int port) throws Exception{
        try(ServerSocket sck = new ServerSocket(port)){
            socket = sck;
            System.out.println("Server started.....");
            acc = socket.accept();
            System.out.println("Client Connected !");
        } catch (Exception e) {
            
        }
    }

    public static void startAccept() throws IOException{
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