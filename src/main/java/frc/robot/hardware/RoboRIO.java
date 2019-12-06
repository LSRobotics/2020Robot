package frc.robot.hardware;

import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import frc.robot.constants.*;

public class RoboRIO {


    private static BuiltInAccelerometer accel;
    private static Axis _axis;

    public static void initialize() {
        accel = new BuiltInAccelerometer();
    }

    public static BuiltInAccelerometer getAccelerometer() {
        return accel;
    }

    public static void setForwardAxis(Axis axis) {
        _axis = axis;
    }

    public static double [] getAccelerations() {
        return new double[] {accel.getX(), accel.getY(), accel.getZ()};
    }

    public static double getAcceleration(Axis axis) {
        switch(axis) {
            case X: 
                return accel.getX();
            case Y: 
                return accel.getY();
            case Z: 
            default:
                return accel.getZ();
        }
    }

    public static double getForwardAcceleration() {

        return getAcceleration(_axis);
    }
}
