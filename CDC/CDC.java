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
        playDelayServer(connected);
        pauseDelayServer(connected);
        isCalculated = true;
    }

    public static void CalculateDelayClient(Socket connected) throws Exception{
        playDelayClient(connected);
        pauseDelayClient(connected);
    }

    private static void playDelayServer(Socket sck) throws UnsupportedAudioFileException, IOException, LineUnavailableException{
        long[] avgBuff = new long[avgConstant];
        for(int i=0;i<avgConstant;i++){
            player.Play();
            long t1 = System.currentTimeMillis();
            sck.getOutputStream().write("play".getBytes());
            byte[] data = new byte[256];
            sck.getInputStream().read(data,0,data.length);
            long t2 = System.currentTimeMillis();
            player.Pause();
            long d_sck_t = Converter.BytesToLong(data);
            avgBuff[i] = t2 - t1;
            player.Quit();
        }
        CDC.playDelay = Converter.getAverage(avgBuff) / 2;
    }

    private static void playDelayClient(Socket sck) throws IOException, UnsupportedAudioFileException, LineUnavailableException{
        for(int i=0;i<avgConstant;i++){
            byte[] data = new byte[256];
            sck.getInputStream().read(data,0,data.length);
            long t1 = System.currentTimeMillis();
            // String cmd = new String(data);
            player.Play();
            long t2 = System.currentTimeMillis();
            sck.getOutputStream().write(Converter.LongToBytes(t2-t1));
            player.Pause();
        }
    }

    private static void pauseDelayServer(Socket sck) throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException{
        long[] avgBuff = new long[avgConstant];
        for(int i=0;i<avgConstant;i++){
            sck.getOutputStream().write("play".getBytes());
            Thread.sleep(playDelay);
            player.Play();
            player.Pause();
            long t1 = System.currentTimeMillis();
            sck.getOutputStream().write("pause".getBytes());
            byte[] data = new byte[256];
            long d_sck_t = sck.getInputStream().read(data,0,data.length);
            long t2 = System.currentTimeMillis();
            avgBuff[i] = t2-t1;
        }
        CDC.pauseDelay = Converter.getAverage(avgBuff) / 2;
    }

    private static void pauseDelayClient(Socket sck) throws IOException, UnsupportedAudioFileException, LineUnavailableException{
        for(int i=0;i<avgConstant;i++){
            byte[] data = new byte[256];
            sck.getInputStream().read(data,0,data.length);
            player.Play();
            sck.getInputStream().read(data,0,data.length);
            long t1 = System.currentTimeMillis();
            player.Pause();
            long t2 = System.currentTimeMillis();
            sck.getOutputStream().write(Converter.LongToBytes(t2-t1));
        }
    }

    public static int playDelay(){
        return playDelay;
    }

    public static int pauseDelay(){
        return pauseDelay;
    }
}
