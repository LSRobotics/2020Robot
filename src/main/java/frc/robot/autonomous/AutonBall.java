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
            isTimeout    = false,
            isHighGoal = false,
            isInterrupted = false;

    final private double LOW_GOAL_SPEED = 10500,
                         HIGH_GOAL_SPEED = 21000;

    final static double TIMEOUT_SECONDS = 10;

    public AutonBall () {
        this(true);
    }

    public AutonBall (Gamepad killGp, Gamepad.Key killKey) {
        super(killGp,killKey);
    }

    public AutonBall(boolean isHighGoal) {
        super();
        this.isHighGoal = isHighGoal;
    }

    @Override
    public void preRun() {
        Chassis.stop();

        Shooter.shooter.move(1);
        //Shooter.shooter.move(1);

        Shooter.index.move(-0.5);
    
        if(!new AutonSleep(20).run()) {
            isInterrupted = true;
        }
    
        masterTimer.start();
    }

    @Override
    public void duringRun() {

        if(masterTimer.getElaspedTimeInMs() > TIMEOUT_SECONDS * 1000) {
            isTimeout = true;
            return;
        }

        //Wait for the speed to come up
        while(Shooter.shooter.getVelocity() < (isHighGoal ? HIGH_GOAL_SPEED : LOW_GOAL_SPEED)) {
            
            Shooter.index.stop();

            if (!isGamepadGood()) return;
        }

        //Record the speed of the motor at the moment of shooting out (Momentum loss very soon)
        var outSpeed = Shooter.shooter.getVelocity();

        Shooter.index.move(1);

        shootTimer.start();

        //Keep idnex running until momentum loss is detected (threshold: 1500 encoder units per 100 ms)
        while(outSpeed - Shooter.shooter.getVelocity() < 1500) {

            if(shootTimer.getElaspedTimeInMs() > 1500) {
                isNoBallLeft = true;
                return;
            }

            if(masterTimer.getElaspedTimeInMs() > TIMEOUT_SECONDS * 1000) {
                isTimeout = true;
                break;
            }

            if(!isGamepadGood()) return;
        }
        Shooter.numBalls -= 1;
    }

    @Override
    public boolean isActionDone() {
        return isNoBallLeft || isTimeout || isInterrupted;
    }

    @Override
    public void postRun() {
        
        Shooter.shooter.move(0);
        Shooter.index.move(0);
        //Shooter.numBalls = 0;

        //Reset boolean values just in case
        isInterrupted = false;
        isTimeout = false;
        isNoBallLeft = false;

    }

    @Override
    public String toString() {
        
        return "AutonBall " + (isHighGoal ? "Higher Port" : "Lower Port");
    }
}