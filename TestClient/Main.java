package TestClient;

import java.io.IOException;
import java.net.Socket;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import CDC.CDC;
import Connector.Connector;
import Converter.Converter;
import testPlayer.Player;

public class Main {

    static Socket sck;
    static Player player;

    public static void Connect(int port) throws Exception{
        sck = Connector.getReachableSocket(port);
    }

    public static void performDelayCalculation() throws Exception{
        CDC.setAveragingConstant(10);
        CDC.CalculateDelayClient(sck);
    }

    public static void listenToCommand() throws IOException, UnsupportedAudioFileException, LineUnavailableException{
        byte[] data = new byte[Integer.BYTES];
        sck.getInputStream().read(data,0,data.length);
        int cmd = Converter.BytesToInt(data);
        if(cmd == 1){
            System.out.println("play");
            player.Play();
        }
        if(cmd == 2){
            System.out.println("pause");
            player.Pause();
        }
    }

    public static void setPlayer() throws Exception{
        player = new Player("test.wav");
    }
    public static void main(String[] args){
        try {
            Connect(8888);
            performDelayCalculation();
            System.out.println("Calibration completed !");
            setPlayer();
            while(true){
                listenToCommand();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
