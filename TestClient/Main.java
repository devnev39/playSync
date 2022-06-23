package TestClient;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import CDC.CDC;
import Connector.Connector;
import testPlayer.Player;

public class Main {

    static Socket sck;
    static Player player;

    public static void Connect(int port) throws UnknownHostException, IOException{
        sck = Connector.getReachableSocket(port);
    }

    public static void performDelayCalculation() throws Exception{
        CDC.setAveragingConstant(10);
        CDC.CalculateDelayClient(sck);
    }

    public static void listenToCommand() throws IOException, UnsupportedAudioFileException, LineUnavailableException{
        byte[] data = new byte[256];
        sck.getInputStream().read(data,0,data.length);
        String cmd = new String(data).replace("\0", "");
        System.out.println(cmd);
        if(cmd.equals("play")){
            player.Play();
        }
        if(cmd.equals("pause")){
            player.Pause();
        }
    }

    public static void setPlayer() throws Exception{
        player = new Player("test.wav");
    }
    public static void main(String[] args) throws Exception{
        Connect(8888);
        performDelayCalculation();
        System.out.println("Calibration completed !");
        setPlayer();
        while(true){
            listenToCommand();
        }
    }
}
