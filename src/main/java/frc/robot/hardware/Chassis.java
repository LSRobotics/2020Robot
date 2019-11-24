package frc.robot.hardware;

import frc.robot.software.*;


public class Chassis {

    static MotorNG l1,l2,l3,r1,r2,r3;
    static double speedFactor = 1;

    static public void init() {
        l1 = new MotorNG(Statics.CHASSIS_L1,true);
        l2 = new MotorNG(Statics.CHASSIS_L2,true);
        l3 = new MotorNG(Statics.CHASSIS_L3,true);
        r1 = new MotorNG(Statics.CHASSIS_R1);
        r2 = new MotorNG(Statics.CHASSIS_R2);
        r3 = new MotorNG(Statics.CHASSIS_R3);
    }

    static public void setSpeedFactor(double factor) {
        speedFactor = factor; 
    }

    static public void drive(double y, double x) {

        final double left  = Utils.clipValue(y + x, -1.0, 1.0) * speedFactor;
        final double right = Utils.clipValue(y - x, -1.0, 1.0) * speedFactor;

        l1.move(left);
        l2.move(left);
        l3.move(left);
        r1.move(right);
        r2.move(right);
        r3.move(right);
        
    }

    static public double getEncoderReading(boolean isLeft) {
        return isLeft? l1.getEncoderReading() : r1.getEncoderReading();
    }

    static public MotorNG getMotor(boolean isLeft, int index) {
        if(index == 0) {
            return isLeft? l1 : r1;
        }
        else if (index == 1) {
            return isLeft? l2 : r2;
        }
        else {
            return isLeft? l3 : r3;
        }
    }

    static public void stop() {
        drive(0,0);
    }
}

