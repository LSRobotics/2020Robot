package frc.robot.hardware;

import frc.robot.software.*;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.Ultrasonic.Unit;
import frc.robot.constants.*;
import frc.robot.hardware.MotorNG.Model;

public class Chassis {

    static MotorNG l1,l2,r1,r2;
    static double speedFactor = 1;
    static Compressor compressor;
    static Solenoid shifter;
    static Ultrasonic frontAligner, backAligner;

    static SpeedCurve curve = SpeedCurve.LINEAR;

    static public void initialize() {
        l1 = new MotorNG(Statics.CHASSIS_L1,Model.FALCON_500);
        l2 = new MotorNG(Statics.CHASSIS_L2,Model.FALCON_500);
        r1 = new MotorNG(Statics.CHASSIS_R1,Model.FALCON_500,true);
        r2 = new MotorNG(Statics.CHASSIS_R2,Model.FALCON_500,true);

        compressor = new Compressor();

        shifter = new Solenoid(Statics.SHIFTER_PCM,
                               Statics.SHIFTER_F,
                               Statics.SHIFTER_R);

        frontAligner = new Ultrasonic(Statics.US_ALIGNER_F_PING,Statics.US_ALIGNER_F_ECHO,Unit.kMillimeters);
        backAligner = new Ultrasonic(Statics.US_ALIGNER_B_PING,Statics.US_ALIGNER_B_ECHO,Unit.kMillimeters);
    }

    static public void shift() {
        shifter.actuate();
    }

    static public void setSpeedCurve(SpeedCurve newCurve) {
        curve = newCurve;
    }

    static public void setSpeedFactor(double factor) {
        speedFactor = factor; 
    }


    static private double getCurvedSpeed(double speed) {
        if(curve == SpeedCurve.LINEAR) return speed;
        else if(curve == SpeedCurve.SQUARED) {
            boolean isNegative = speed < 0;

            return Math.pow(speed, 2) * (isNegative? -1 : 1);
        }
        else {
            return Math.pow(speed, 3);
        }
    }

    static public boolean align() {
        
        while(true) {

            double front = frontAligner.getRangeMM();
            double back  = frontAligner.getRangeMM();
            
            Core.robot.gp1.fetchData();
            
            if(Core.robot.gp1.isKeyHeld(Gamepad.Key.LB)) {
                stop();
                return false;
            }

            if(Math.abs(front - back) < 50) {
                stop();
                break;
            }
            else if (front > back) {
                driveRaw(0,0.5);
            }
            else {
                driveRaw(0,-0.5);
            }

        }
        
        return true;
    }

    static public void drive(double y, double x) {

        drive(getCurvedSpeed(y) * speedFactor, getCurvedSpeed(x) * speedFactor);
        
    }
    static public void driveRaw(double y, double x) {
        final double left  = Utils.clipValue(y + x, -1.0, 1.0);
        final double right = Utils.clipValue(y - x, -1.0, 1.0);

        l1.move(left);
        l2.move(left);
        r1.move(right);
        r2.move(right);
    }

    static public double getEncoderReading(boolean isLeft) {
        return isLeft? l1.getEncoderReading() : r1.getEncoderReading();
    }

    static public MotorNG getMotor(boolean isLeft, int index) {
        if(index == 0) {
            return isLeft? l1 : r1;
        }
        else {
            return isLeft? l2 : r2;
        }
    }

    static public void stop() {
        drive(0,0);
    }
}

