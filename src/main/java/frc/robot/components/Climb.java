package frc.robot.components;

import frc.robot.software.*;
import frc.robot.hardware.*;
import frc.robot.hardware.MotorNG.Model;

public class Climb {

    private static Solenoid lock;
    public static MotorNG roller;
    final public static double SLOW_PORTION = 0.3; // Head and Tail, out of 1
    private static boolean isEngaged = false,
                           isLockBusy = false;

    public static void initialize() {
        lock = new Solenoid(Statics.MASTER_PCM, Statics.CLIMB_FORWARD, Statics.CLIMB_REVERSE, "Climb");
        roller = new MotorNG(Statics.CLIMB_ROLLER, Model.FALCON_500,true);
        roller.setEmulated(true);
        roller.setSpeed(0.85);

        lock(true);
    }

    public static void manualRun(double speed) {
        roller.move(speed);
    }

    /**
     * Whenever motor is not running, engage lock
     * 
     * @param speed speed of the roller motor
     */

    public static void run(double speed) {

        new Thread(() -> {
            
            if (speed != 0) {
                   
                if (isEngaged) {
                    lock(false); 
                    isLockBusy = true;
                    //roller.move(-1);
                    Utils.sleep(300);

                    isLockBusy = false;
                }
                if(!isLockBusy) {
                    if(speed < 0 && roller.getEncoderReading() > 0) {
                        roller.move(speed);
                    }
                    else if(speed >= 0) {
                        roller.move(speed);
                    }
                    else {
                        roller.stop();
                    }
                }
            } 
            
            else {
                roller.stop();
                if (!isEngaged) {
                    isLockBusy = true;
                    Utils.sleep(50);
                    isLockBusy = false;
                }
                lock(true);
            }
        }).start();
    }

    /**
     * Operates roller intelligently
     * 
     * @param isUp   Is the roller going up or not
     * @param isDown Is the roller going down or not NOTE: If isUp == isDown, then
     *               roller would stop (This design makes control code easier to
     *               write and simply look better)
     * @return Is roller still good to run (If not, this method would lock the
     *         climbing mechanism using the piston attached)
     */
    public static boolean turnRoller(boolean isUp, boolean isDown) {

        /*
         * 
         * var distance = getDistance();
         * 
         * if(isUp == isDown) { return distance <= Statics.CLIMB_MOTOR_TRAVEL_DISTANCE;
         * }
         * 
         * //In slow portion if((distance / Statics.CLIMB_MOTOR_TRAVEL_DISTANCE <
         * SLOW_PORTION) || distance / Statics.CLIMB_MOTOR_TRAVEL_DISTANCE > (1 -
         * SLOW_PORTION) ) { roller.move(isUp? 0.3 : -0.3); //FIXME: Tweak this } else
         * if (distance > Statics.FALCON_UNITS_PER_INCH) { roller.stop();
         * lock.move(true,false); return false; } //In fast portion else {
         * roller.move(isUp? 0.6 : -0.6); //FIXME: Tweak this }
         */

        return true;
    }

    public static double getLocation() {
        return roller.getEncoderReading();
    }

    public static void lock(boolean isLocked) {

        isEngaged = isLocked;

        if (isLocked) {
            roller.stop();
            lock.move(false, true);
        } else {
            lock.move(true, false);
        }
    }

    public static boolean isEngaged() {
        return isEngaged;
    }
}