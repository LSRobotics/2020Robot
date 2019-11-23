package frc.robot.hardware;

import frc.robot.software.*;


public class Chassis {

    static MotorNG l1,l2,l3,r1,r2,r3;
    static Solenoid shifter;

    static double speedFactor = 1;
    static boolean isFliped = false;

    static public void init() {
        l1 = new MotorNG(Statics.CHASSIS_L1,true);
        l2 = new MotorNG(Statics.CHASSIS_L2,true);
        l3 = new MotorNG(Statics.CHASSIS_L3,true);
        r1 = new MotorNG(Statics.CHASSIS_R1);
        r2 = new MotorNG(Statics.CHASSIS_R2);
        r3 = new MotorNG(Statics.CHASSIS_R3);

        shifter = new Solenoid(Statics.SHIFTER_PCM,
                               Statics.SHIFTER_F,
                               Statics.SHIFTER_R);
    }

    static public void setSpeedFactor(double factor) {
        speedFactor = factor; 
    }

    static public void shift() {
        shifter.actuate();
    }

    static public void drive(double y, double x) {

        final double left  = Utils.clipValue(y + x, -1.0, 1.0) * speedFactor * (isFliped? -1 : 1);
        final double right = Utils.clipValue(y - x, -1.0, 1.0) * speedFactor * (isFliped? -1 : 1);

        l1.move(left);
        l2.move(left);
        l3.move(left);
        r1.move(right);
        r2.move(right);
        r3.move(right);
        
    }

    static public void stop() {
        drive(0,0);
    }

    static public void flip() {
        isFliped = !isFliped;
    }
}

