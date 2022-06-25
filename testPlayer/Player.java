package testPlayer;

import static javax.sound.sampled.Clip.LOOP_CONTINUOUSLY;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Player {
    private String filePath;
    private AudioInputStream audioStream;
    private Clip clip;
    private String status;
    private long frame;
    private long lastCommandSystemRunTime;

    public Player(String filePath) throws Exception{
        if(Files.exists(Path.of(filePath))){
            this.filePath = filePath;
            audioStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
            clip = AudioSystem.getClip();
            // clip.open(audioStream);
            clip.loop(LOOP_CONTINUOUSLY);
            status = "idle";
            frame = 0L;
            // this.Play();
        }else{
            throw new Exception("File not found !");
        }
    }

    public void Play() throws UnsupportedAudioFileException, IOException, LineUnavailableException{
        long t1 = System.currentTimeMillis();
        if(this.status.equals("playing")){
            System.out.println("Already playing !");
            return;
        }
        if(this.status.equals("idle")){
            clip.open(audioStream);
            clip.start();
            status = "playing";
            return;
        }
        clip.close();
        resetAudioStream();
        clip.open(audioStream);
        clip.setMicrosecondPosition(frame);
        clip.loop(LOOP_CONTINUOUSLY);
        clip.start();
        status = "playing";
        long t2 = System.currentTimeMillis();
        this.lastCommandSystemRunTime = t2-t1;
    }

    public void Pause(){
        long t1 = System.currentTimeMillis();
        if(this.status.equals("paused")){
            System.out.println("Already pasued !");
            return;
        }
        frame = this.clip.getMicrosecondPosition();
        clip.stop();
        status = "paused";
        long t2 = System.currentTimeMillis();
        this.lastCommandSystemRunTime = t2-t1;
    }

    public void Seek() throws UnsupportedAudioFileException, IOException, LineUnavailableException{
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter time in ms (0 to "+clip.getMicrosecondLength()+"): ");
        long seek = sc.nextLong();
        if(seek>=0 && seek<=clip.getMicrosecondLength()){
            frame = seek;
            status = "paused";
            this.Play();
        }
    }

    public void Quit(){
        clip.stop();
        clip.close();
        status = "Quit";
        frame = 0L;
    }

    public void resetAudioStream() throws UnsupportedAudioFileException, IOException, LineUnavailableException{
        audioStream = AudioSystem.getAudioInputStream(new File(this.filePath).getAbsoluteFile());
        // clip.open(audioStream);
        // clip.loop(LOOP_CONTINUOUSLY);
    }

    public String Status(){
        return this.status;
    }

    public String toString(){
        String[] name = this.filePath.split(File.pathSeparator);
        return "File : "+name[name.length-1]+"\nStatus : "+this.status;
    }

    public long lastCommandRunTime() {
        return lastCommandSystemRunTime;
    }
}
