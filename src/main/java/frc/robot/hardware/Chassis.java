package frc.robot.hardware;

import frc.robot.software.*;
import edu.wpi.first.wpilibj.Compressor;
import frc.robot.constants.*;
import frc.robot.hardware.MotorNG.Model;
import frc.robot.hardware.RangeSensor.Type;

public class Chassis {

    static MotorNG l1, l2, r1, r2;
    static double speedFactor = 1;
    public static Solenoid shifter;
    private static double [] lastPower = {0,0};
/*
    public static RangeSensor frontAligner = new RangeSensor(Statics.US_ALIGNER_F_PING, Statics.US_ALIGNER_F_ECHO,Type.DIO_US_HC_SR04),
                                   sideAligner  = new RangeSensor(Statics.US_ALIGNER_S_PING, Statics.US_ALIGNER_S_ECHO,Type.DIO_US_HC_SR04);
*/
    public static RangeSensor sensorIR = new RangeSensor(Statics.IR, Type.ANALOG_IR_GP2Y0A710K0F),
                              maxbotix = new RangeSensor(Statics.US_MAXBOTIX, Type.ANALOG_RAW);

    static SpeedCurve curve = SpeedCurve.HYBRID;
    static boolean isInverted = false;

    static public void initialize() {

        shifter = new Solenoid(Statics.MASTER_PCM, Statics.CHASSIS_FORWARD, Statics.CHASSIS_REVERSE, "Chassis");

        l1 = new MotorNG(Statics.CHASSIS_L1, Model.FALCON_500,true);
        l2 = new MotorNG(Statics.CHASSIS_L2, Model.FALCON_500,true);
        r1 = new MotorNG(Statics.CHASSIS_R1, Model.FALCON_500);
        r2 = new MotorNG(Statics.CHASSIS_R2, Model.FALCON_500);

        l1.setCurrentLimit(70);
        l2.setCurrentLimit(70);
        r1.setCurrentLimit(70);
        r2.setCurrentLimit(70);

    }

    static public void shift() {
        shifter.actuate();
    }

    static public void shift(boolean isLow) {
        shifter.move(isLow, !isLow);
    }
    
    static public void filp() {
        isInverted = !isInverted;
    }

    static public void setInverted(boolean value) {
        isInverted = value;
    }

    static public void setSpeedCurve(SpeedCurve newCurve) {
        curve = newCurve;
    }

    static public void setSpeedFactor(double factor) {
        speedFactor = factor;
    }

    static public double getSpeedFactor() {
        return speedFactor;
    }

    static private double getCurvedSpeed(double speed,SpeedCurve curve) {
        
        boolean isNegative = speed < 0;
        final var CURVE_PORTION = 0.4;

        //Linear
        if (curve == SpeedCurve.LINEAR)
            return speed;
        
        //Squared
        else if (curve == SpeedCurve.SQUARED) {
            return Math.pow(speed, 2) * (isNegative ? -1 : 1);
        } 
        
        //Cubed
        else if(curve == SpeedCurve.CUBED) {
            return Math.pow(speed, 3);
        } 
        
        //Hybrid
        else {
            if(Math.abs(speed) < 0.4) {
                return Math.pow(speed, 2) * (isNegative ? -1 : 1);
            } 
            else {
                return (2 * CURVE_PORTION * Math.abs(speed) - 2 * CURVE_PORTION + 1) * (isNegative? -1 : 1); 
            }
        }
    }


    static public void drive(double y, double x) {

        lastPower[0] = y;
        lastPower[1] = x;

        driveRaw(getCurvedSpeed(y,curve) * speedFactor, getCurvedSpeed(x,SpeedCurve.SQUARED) * speedFactor);

    }

    static public void driveRaw(double y, double x) {

        var invertFactor = (isInverted? 1 : -1);

        final double left = Utils.clipValue(y + x, -1.0, 1.0) *invertFactor;
        final double right = Utils.clipValue(y - x, -1.0, 1.0) *invertFactor;

        l1.move(left);
        l2.move(left);
        r1.move(right);
        r2.move(right);
    }

    static public double [] getEncoderReading() {
        return new double [] {
            l1.getEncoderReading(),
            l2.getEncoderReading(),
            r1.getEncoderReading(),
            r2.getEncoderReading()
        }; 
    }

    static public MotorNG getMotor(boolean isLeft, int index) {
        if (index == 0) {
            return isLeft ? l1 : r1;
        } else {
            return isLeft ? l2 : r2;
        }
    }

    static public void stop() {
        drive(0, 0);
    }

    static public boolean isInverted() {
        return isInverted;
    }

    static public double [] getLastPower() {
        return lastPower;
    }
}
