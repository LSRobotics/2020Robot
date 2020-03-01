package frc.robot.hardware;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoException;

public class Camera {

    static UsbCamera cam0;
    
    static public void initialize() {

        try {

        cam0 = CameraServer.getInstance().startAutomaticCapture(0);
        cam0.setResolution(640, 360);
        } catch(VideoException e) {
            //Shhhhh
        }
    }

}