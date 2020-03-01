package frc.robot.components;

import frc.robot.hardware.*;
import frc.robot.hardware.Gamepad.Key;
import frc.robot.hardware.MotorNG.Model;
import frc.robot.hardware.RangeSensor.Type;
import frc.robot.software.*;
import frc.robot.autonomous.*;

public class Shooter {

    
    public static MotorNG index, shooter, intake;
    
    //Internal Motors (Slaves)
    private static MotorNG index2, index3;
    
    public static Solenoid intakeArm;

    public static RangeSensor usIntake;
    
    public static int numBalls = 0;
    public static Timer intakeTimer = new Timer("Intake Timer");
    public static boolean isShooting = false, 
                          lastBallStatus = false, 
                          isIntakeDown = false;

    public static void initialize() {
        // Index Motors
        index = new MotorNG(Statics.INDEX_1, Model.VICTOR_SPX,true);
        index2 = new MotorNG(Statics.INDEX_2, Model.VICTOR_SPX,true);
        index3 = new MotorNG(Statics.INDEX_3, Model.VICTOR_SPX);

        // Shooting Motors
        shooter = new MotorNG(Statics.SHOOTER, Model.FALCON_500, true);

        usIntake = new RangeSensor(Statics.US_INTAKE_PING, Statics.US_INTAKE_ECHO, Type.DIO_US_HC_SR04);

        // Intake Mechanism
        intake = new MotorNG(Statics.INTAKE, Model.TALON_SRX);
        intake.setSpeed(1);
        intakeArm = new Solenoid(Statics.MASTER_PCM, Statics.ARM_FORWARD, Statics.ARM_REVERSE);

        // Setting up motor speeds
        index.setSpeed(0.7);
        index2.setSpeed(0.7);
        index3.setSpeed(0.5);
        //feeder.setSpeed(0.4);

        index.addSlave(index2);
        index.addSlave(index3);

    }

    public static void update() {

        boolean ballStatus = usIntake.getRangeInches() < 3;

        //Intake Arm / Intake
        if(Core.robot.gp1.isKeyToggled(Key.A)) {
            isIntakeDown = !isIntakeDown;
            intake.move(isIntakeDown? 1 : 0);
            intakeArm.move(isIntakeDown, !isIntakeDown);
        }

        // Autonomous Shooter
        if (Core.robot.gp1.isKeyToggled(Key.X)) {
                new AutonBall(Core.robot.gp1, Key.DPAD_DOWN).run();
                numBalls = 0;
        }

        //Indexer

        // Ball is in
        if (usIntake.getRangeInches() < 3 && (ballStatus != lastBallStatus)) {

            if (!intakeTimer.isBusy() && numBalls < 4) {

                numBalls += 1;
                intakeTimer.start();
                index.move(1);
            }
            else if(numBalls == 4) {
                numBalls = 5;
            }
        }

        if (intakeTimer.getElaspedTimeInMs() > 80) {
            index.stop();
            intakeTimer.stop();
            intakeTimer.zero();
        }


        lastBallStatus = ballStatus;
    }

    public static double getVelocity() {
        return shooter.getVelocity();
    }

    public static int getNumBalls() {
        return numBalls;
    }
}