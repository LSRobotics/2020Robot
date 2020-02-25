package frc.robot.components;

import frc.robot.software.*;
import frc.robot.hardware.*;
import frc.robot.hardware.MotorNG.Model;

public class Climb {
    
    static Solenoid lock;
    static MotorNG roller;
    final public static double SLOW_PORTION = 0.3; //Head and Tail, out of 1
    

    public static void initialize() {
        lock = new Solenoid(Statics.MASTER_PCM, Statics.CLIMB_FORWARD, Statics.CLIMB_REVERSE);
        roller = new MotorNG(Statics.CLIMB_ROLLER, Model.FALCON_500);
        lock.move(false, true);
    }

    /**
     * Operates roller intelligently
     * @param isUp Is the roller going up or not
     * @param isDown Is the roller going down or not
     * NOTE: If isUp == isDown, then roller would stop (This design makes control code easier to write and simply look better)
     * @return Is roller still good to run (If not, this method would lock the climbing mechanism using the piston attached)
     */
    public static boolean turnRoller(boolean isUp, boolean isDown) {

        double currentDistance = roller.getEncoderReading() / Statics.FALCON_UNITS_PER_INCH;

        if(isUp == isDown) {
            return currentDistance <= Statics.CLIMB_MOTOR_TRAVEL_DISTANCE;
        }

        //In slow portion
        if((currentDistance / Statics.CLIMB_MOTOR_TRAVEL_DISTANCE < SLOW_PORTION) 
         || currentDistance / Statics.CLIMB_MOTOR_TRAVEL_DISTANCE > (1 - SLOW_PORTION) ) {
            roller.move(isUp? 0.7 : -0.7); //FIXME: Tweak this
        }
        else if (currentDistance > Statics.FALCON_UNITS_PER_INCH) {
            roller.stop();
            lock.move(true,false);
            return false;
        }
        //In fast portion
        else {
            roller.move(isUp? 0.3 : -0.3); //FIXME: Tweak this
        }

        return true;
    }
}