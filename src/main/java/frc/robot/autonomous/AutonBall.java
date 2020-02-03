package frc.robot.autonomous;

import frc.robot.software.Timer;
import frc.robot.hardware.*;

/**
 * THE IQ 200 SHOOTER -- Does everything we need;
 */

public class AutonBall extends AutonBase {

    Timer shootTimer = new Timer("Auton Shooter Shoot Timer"); //For determining whether the shooter motor took too short to reload -- no balls left?
    Timer masterTimer = new Timer("Auton Shooter Master Timer"); //For determining whether the shooter took too long to shoot -- mechanical faliure?
    boolean isNoBallLeft = false,
            isTimeout    = false;
    
    boolean isFirstBall = true;

    public AutonBall () {
        super();
    }

    public AutonBall (Gamepad killGp, Gamepad.Key killKey) {
        super(killGp,killKey);
    }

    @Override
    public void preRun() {
        //Chassis.stop();
        robot.shooter.move(1);
        masterTimer.start();
    }

    @Override
    public void duringRun() {

        if(masterTimer.getElaspedTimeInMs() > 6000) {
            isTimeout = true;
            return;
        }

        //Wait for the speed to come up
        while(robot.shooter.getVelocity() < 20500) {
            
            robot.index1.stop();
            robot.index2.stop();
            robot.index3.stop();
            robot.feeder.stop();

            if(!isAutonPeriod && !isGamepadGood()) return;
        }

        robot.feeder.move(1);
        robot.index1.move(1);
        robot.index2.move(1);
        robot.index3.move(1);
        

        shootTimer.start();

        while(robot.shooter.getVelocity() > 19500) {

            if(shootTimer.getElaspedTimeInMs() > 2000) {
                isNoBallLeft = true;
                break;
            }

            if(masterTimer.getElaspedTimeInMs() > 6000) {
                isTimeout = true;
                break;
            }

            if(!isGamepadGood()) return;
        }
    }

    @Override
    public boolean isActionDone() {
        return isNoBallLeft || isTimeout;
    }

    @Override
    public void postRun() {
        robot.shooter.move(0);
        robot.feeder.move(0);
        robot.index1.move(0);
        robot.index2.move(0);
        robot.index3.move(0);
    }
}