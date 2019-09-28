package frc.robot.software;

//import frc.robot.Robot;
import frc.robot.hardware.*;
import frc.robot.software.Utils.BotLocation;

/**
 * Constraints: If start from middle station, we can only load in habs (which makes the code more robust instead of being off by a ton)
 * And if start from side, we can only load in the three stations
 */

public class AutonSensorBasedA implements Runnable {

    public enum Position {
        CARGO_NEAR, CARGO_MID, CARGO_FAR, ROCKET, HAB
    }

    Position pos = Position.CARGO_NEAR;

    public AutonSensorBasedA(Position pos) {
        this.pos = pos;
    }

    public void run() {
        switch(Utils.getLocation()) {
            case LEFT:
            case RIGHT:
                runSidePath();
                break;
            case MIDDLE:
            default:
                runStraightPath(); 
        }
        
    }

    private void runStraightPath() {

        //Just go straight, how hard would that be
        if(!Utils.moveByDistance(AutonConstants.MID_STATION_TO_CARGO_DISTANCE, 0.5)) return;

    }

    private void runSidePath() {
      
        double distance = AutonConstants.HALF_DISTANCE - AutonConstants.STATION_TOTAL_LENGTH;

        switch (pos) {
        case CARGO_NEAR:
            distance += AutonConstants.STATION_EACH_LENGTH / 2;
            break;
        case CARGO_MID:
            distance += AutonConstants.STATION_EACH_LENGTH * 1.5;
            break;
        case CARGO_FAR:
            distance += AutonConstants.STATION_EACH_LENGTH * 2.5;
            break;
        default:
            break;
        }
     
        if(!Utils.moveByDistance(distance, 0.5));

        if(!Utils.takeABreak()) return;

        //Turn Left/Right (90 degrees)
        double targetAngle = Utils.getLocation() == BotLocation.LEFT ? 90 : -90;

        if(!Utils.turnRobot(0.5, targetAngle)) return;

        if(!Utils.takeABreak()) return;
        
        //Now go straight forward for 400 miliseconds
        if(!Utils.drive(0.3, 0, 400)) return;

        Chassis.stop();

        //Now the hatch panel is attached, so I don't know what to do next
    }
}