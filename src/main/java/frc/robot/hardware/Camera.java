package frc.robot.hardware;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;

final public class Camera {

    private UsbCamera cam;

    public Camera() {
        cam = CameraServer.getInstance().startAutomaticCapture();
    }

    public Camera(int deviceId) {
        cam = CameraServer.getInstance().startAutomaticCapture(deviceId);
    }

    public UsbCamera getCamera() {
        return cam;
    }
}