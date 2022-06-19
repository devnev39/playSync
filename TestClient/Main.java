package TestClient;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import CDC.CDC;
import Converter.Converter;
import testPlayer.Player;

public class Main {

    static Socket sck;
    static Player player;

    public static void Connect(int port) throws UnknownHostException, IOException{
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Ip : ");
        String ip = sc.nextLine();
        sck = new Socket(ip,port);
    }

    public static void performDelayCalculation() throws Exception{
        CDC.CalculateDelayClient(sck);
    }

    public static void listenToCommand() throws IOException, UnsupportedAudioFileException, LineUnavailableException{
        byte[] data = new byte[256];
        sck.getInputStream().read(data,0,data.length);
        String cmd = new String(data);
        switch (cmd) {
            case "play":
                player.Play();
                break;
            
            case "pause":
                player.Pause();
                break;

            default:
                return;
        }
    }
    public static void main(String[] args) throws Exception{
        Connect(8888);
        performDelayCalculation();
        while(true){
            listenToCommand();
        }
    }
}
