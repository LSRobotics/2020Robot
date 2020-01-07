package frc.robot.hardware;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoSink;

public class Camera {

    static UsbCamera cam0;
    static VideoSink server;
    static boolean switched = false;

    static public void initialize() {
        cam0 = CameraServer.getInstance().startAutomaticCapture(0);


        cam0.setResolution(640, 360);
        server = CameraServer.getInstance().getServer();
        server.setSource(cam0);
    }

}