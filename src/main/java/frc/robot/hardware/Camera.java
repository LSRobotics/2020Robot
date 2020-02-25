package frc.robot.hardware;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoException;
import edu.wpi.cscore.VideoSink;

public class Camera {

    static UsbCamera cam0;
    
    static public void initialize() {

        try {

        cam0 = CameraServer.getInstance().startAutomaticCapture(0);
        cam0.setResolution(1280, 720);
        } catch(VideoException e) {
            //Shhhhh
        }
    }

}