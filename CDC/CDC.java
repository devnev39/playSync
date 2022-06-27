package CDC;

import java.io.IOException;
import java.net.Socket;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import Converter.Converter;
import testPlayer.Player;

public class CDC {
    private static int avgConstant;
    private static Player player;
    private static int playDelay;
    private static int pauseDelay;
    private static boolean isCalculated = false;

    public static void setAveragingConstant(int avgConst){
        CDC.avgConstant = avgConst;
    }

    public static void setPlayer() throws Exception{
        CDC.player = new Player("test.wav");
    }

    public static void CalculateDelayServer(Socket connected) throws Exception{
        setPlayer();
        playDelayServer(connected);
        pauseDelayServer(connected);
        player.Pause();
        isCalculated = true;
    }

    public static void CalculateDelayClient(Socket connected) throws Exception{
        setPlayer();
        playDelayClient(connected);
        pauseDelayClient(connected);
        player.Pause();
    }

    private static void playDelayServer(Socket sck) throws UnsupportedAudioFileException, IOException, LineUnavailableException{
        long[] avgBuff = new long[avgConstant];
        for(int i=0;i<avgConstant;i++){
            sck.getOutputStream().write(Converter.IntToBytes(1));
            player.Play();
            long ts = player.lastCommandRunTime();
            long t1 = System.currentTimeMillis();
            byte[] data = new byte[Long.BYTES];
            sck.getInputStream().read(data,0,data.length);
            long t2 = System.currentTimeMillis();
            player.Pause();
            long tc = Converter.BytesToLong(data);
            System.out.println(t2-t1);
            avgBuff[i] = (tc + ((t2-t1)/2)) - ts;
            player.Quit();
        }
        CDC.playDelay = Converter.getAverage(avgBuff);
        System.out.println("Play Delay : "+CDC.playDelay);
    }

    private static void playDelayClient(Socket sck) throws IOException, UnsupportedAudioFileException, LineUnavailableException{
        for(int i=0;i<avgConstant;i++){
            byte[] data = new byte[Integer.BYTES];
            sck.getInputStream().read(data,0,data.length);
            player.Play();
            sck.getOutputStream().write(Converter.LongToBytes(player.lastCommandRunTime()));
            player.Pause();
            System.out.println(player.lastCommandRunTime());
        }
    }

    private static void pauseDelayServer(Socket sck) throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException{
        long[] avgBuff = new long[avgConstant];
        for(int i=0;i<avgConstant;i++){
            sck.getOutputStream().write(Converter.IntToBytes(1));
            Thread.sleep(playDelay);
            player.Play();
            player.Pause();
            long t1 = System.currentTimeMillis();
            sck.getOutputStream().write(Converter.IntToBytes(2));
            byte[] data = new byte[Long.BYTES];
            sck.getInputStream().read(data,0,data.length);
            long t2 = System.currentTimeMillis();
            avgBuff[i] = (Converter.BytesToLong(data) + ((t2-t1)/2)) - player.lastCommandRunTime();
            System.out.println(t2-t1);
        }
        CDC.pauseDelay = Converter.getAverage(avgBuff);
        System.out.println("Pause Delay : "+CDC.pauseDelay);
    }

    private static void pauseDelayClient(Socket sck) throws IOException, UnsupportedAudioFileException, LineUnavailableException{
        for(int i=0;i<avgConstant;i++){
            byte[] data = new byte[Integer.BYTES];
            sck.getInputStream().read(data,0,data.length);
            player.Play();
            sck.getInputStream().read(data,0,data.length);
            player.Pause();
            sck.getOutputStream().write(Converter.LongToBytes(player.lastCommandRunTime()));
            System.out.println(player.lastCommandRunTime());
        }
    }

    public static int playDelay(){
        return playDelay;
    }

    public static int pauseDelay(){
        return pauseDelay;
    }

    public static boolean isCalculated(){
        return isCalculated;
    }
}
