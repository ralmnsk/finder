package com.finder.camera;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundWriter {
    public static void main(String[] args){
        AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,44100,16,2,4,44100,false);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        if(!AudioSystem.isLineSupported(info)){
            System.out.println("Line not supported");
        }

        try(final TargetDataLine targetLine = (TargetDataLine)AudioSystem.getLine(info);) {

            targetLine.open();
            System.out.println("Staring recording");
            targetLine.start();
            Thread thread = new Thread(){
                @Override
                public void run() {
                    AudioInputStream audioStream = new AudioInputStream(targetLine);
                    File audioFile = new File("record.wav");
                    try {
                        AudioSystem.write(audioStream, AudioFileFormat.Type.WAVE,audioFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };

            thread.start();
            Thread.sleep(10000);
            targetLine.stop();
            System.out.println("Ending recording");
        } catch (LineUnavailableException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
