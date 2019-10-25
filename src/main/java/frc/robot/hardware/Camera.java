package frc.robot.hardware;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoSink;

public class Camera {

    static UsbCamera cam0;
    static UsbCamera cam1;
    static VideoSink server;
    static boolean switched = false;

    static public void initialize() {
        cam0 = CameraServer.getInstance().startAutomaticCapture(0);
        cam1 = CameraServer.getInstance().startAutomaticCapture(1);

        cam0.setResolution(320, 180);
        cam1.setResolution(320, 180);
        server = CameraServer.getInstance().getServer();
        server.setSource(cam0);
    }

    static public void changeCam() {

        switched = !switched;

        server.setSource(switched ? cam1 : cam0);
    }

}