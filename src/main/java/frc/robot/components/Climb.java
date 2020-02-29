package frc.robot.components;

import frc.robot.software.*;
import frc.robot.hardware.*;
import frc.robot.hardware.MotorNG.Model;

public class Climb {
    
    private static Solenoid lock;
    static MotorNG roller;
    final public static double SLOW_PORTION = 0.3; //Head and Tail, out of 1
    private static boolean isEngaged = false;
    

    public static void initialize() {
        lock = new Solenoid(Statics.MASTER_PCM, Statics.CLIMB_FORWARD, Statics.CLIMB_REVERSE);
        roller = new MotorNG(Statics.CLIMB_ROLLER, Model.FALCON_500);

        roller.setSpeed(0.3);
        lock.move(false, true);
    }


    /**
     * Ditch this method in the future, this is for testing purposes only.
     * Motor would not run if lock is engaged
     * @param speed speed of the roller motor
     */
    public static void test(double speed) {
        if(!isEngaged) {
            roller.move(speed);
        }
        else {
            roller.stop();
        }
    }

    /**
     * Operates roller intelligently
     * @param isUp Is the roller going up or not
     * @param isDown Is the roller going down or not
     * NOTE: If isUp == isDown, then roller would stop (This design makes control code easier to write and simply look better)
     * @return Is roller still good to run (If not, this method would lock the climbing mechanism using the piston attached)
     */
    public static boolean turnRoller(boolean isUp, boolean isDown) {

        /*

        var distance = getDistance();

        if(isUp == isDown) {
            return distance <= Statics.CLIMB_MOTOR_TRAVEL_DISTANCE;
        }

        //In slow portion
        if((distance / Statics.CLIMB_MOTOR_TRAVEL_DISTANCE < SLOW_PORTION) 
         || distance / Statics.CLIMB_MOTOR_TRAVEL_DISTANCE > (1 - SLOW_PORTION) ) {
            roller.move(isUp? 0.3 : -0.3); //FIXME: Tweak this
        }
        else if (distance > Statics.FALCON_UNITS_PER_INCH) {
            roller.stop();
            lock.move(true,false);
            return false;
        }
        //In fast portion
        else {
            roller.move(isUp? 0.6 : -0.6); //FIXME: Tweak this
        }
        */

        return true;
    }

    public static double getDistance() {
        return roller.getEncoderReading() / Statics.FALCON_UNITS_PER_INCH;
    }

    public static void lock(boolean isLocked) {

        isEngaged = isLocked;

        if(isLocked) {
            roller.stop();
            lock.move(true,false);
        }
        else {
            lock.move(false,true);
        }
    }

    public static boolean isEngaged() {
        return isEngaged;
    }
}