package com.finder.camera;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamEvent;
import com.github.sarxos.webcam.WebcamListener;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Camera {
    public static void main(String[] args){
        Webcam webcam = Webcam.getDefault();
        webcam.setViewSize(new Dimension(640, 480));
        webcam.addWebcamListener(new WebcamListener() {
            @Override
            public void webcamOpen(WebcamEvent webcamEvent) {
                System.out.println("Camera open");
            }

            @Override
            public void webcamClosed(WebcamEvent webcamEvent) {
                System.out.println("Camera closed");
            }

            @Override
            public void webcamDisposed(WebcamEvent webcamEvent) {
                System.out.println("Camera disposed");
            }

            @Override
            public void webcamImageObtained(WebcamEvent webcamEvent) {
                System.out.println("Image obtained");
            }
        });
        webcam.open();
        try {
            int i=0;
            while( i != 60000){
                Thread.sleep(250);
                ImageIO.write(webcam.getImage(), "JPG", new File("src/main/resources/files/hello-world.jpg"));
                i++;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }finally {
            if (webcam != null){
                webcam.close();
            }
        }
    }
}
