package frc.robot.hardware;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;

final public class Camera {

    private UsbCamera cam;

    private double quotient = 640 / 400;
    private int height = 400;
    private boolean isQuickMode = false;
    private double scaleFactor = 1.0; 

    public Camera() {
        cam = CameraServer.getInstance().startAutomaticCapture();
    }

    public Camera(int deviceId) {
        cam = CameraServer.getInstance().startAutomaticCapture(deviceId);
    }

    public UsbCamera getCamera() {
        return cam;
    }

    public void updateDynamicRes(int targetFps) {

        double currentFps = cam.getActualFPS();
        double downscaleFactor = currentFps / targetFps;


        // It's not in game so supersampling is not possible
        if (scaleFactor * downscaleFactor  > 1) {
            return;
        }

        scaleFactor *= downscaleFactor;

        setResolution((int) (height * scaleFactor));

    }

    public void setScreenRatio(double quotient) {
        this.quotient = quotient;
    }

    public void setResolution(int height) {
        this.height = height;
        cam.setResolution((int) (height * quotient), height);
    }

    public int getHeight() {
        return height;
    }

    public int [] getResolution() {
        return new int [] {(int)(height * quotient), height};
    }

    public boolean isQuickMode() {
        return isQuickMode;
    }

    public void setQuickMode(boolean isQuickMode) {

        this.isQuickMode = isQuickMode;

        if (isQuickMode) {
            cam.setExposureHoldCurrent();
            cam.setWhiteBalanceHoldCurrent();
        } else {
            cam.setExposureAuto();
            cam.setWhiteBalanceAuto();
        }
    }
}