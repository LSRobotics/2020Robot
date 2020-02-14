package frc.robot.autonomous;

import frc.robot.software.Timer;
import frc.robot.hardware.*;
import frc.robot.components.*;

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
        Shooter.shooter.move(1);
        masterTimer.start();
    }

    @Override
    public void duringRun() {

        if(masterTimer.getElaspedTimeInMs() > 6000) {
            isTimeout = true;
            return;
        }

        //Wait for the speed to come up
        while(Shooter.shooter.getVelocity() < 20500) {
            
            Shooter.index1.stop();
            Shooter.index2.stop();
            Shooter.index3.stop();
            Shooter.feeder.stop();

            if (!isGamepadGood()) return;
        }

        Shooter.feeder.move(1);
        Shooter.index1.move(1);
        Shooter.index2.move(1);
        Shooter.index3.move(1);
        

        shootTimer.start();

        while(Shooter.shooter.getVelocity() > 19500) {

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
        Shooter.shooter.move(0);
        Shooter.feeder.move(0);
        Shooter.index1.move(0);
        Shooter.index2.move(0);
        Shooter.index3.move(0);
    }
}