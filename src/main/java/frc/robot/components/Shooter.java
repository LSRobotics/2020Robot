package frc.robot.components;

import frc.robot.hardware.*;
import frc.robot.hardware.Gamepad.Key;
import frc.robot.hardware.MotorNG.Model;
import frc.robot.hardware.RangeSensor.Type;
import frc.robot.software.*;
import frc.robot.autonomous.*;

public class Shooter {

    public static MotorNG index1, index2, index3, feeder, shooter;
    private static RangeSensor usIntake;
    public static int numBalls = 0;
    public static Timer intakeTimer = new Timer("Intake Timer");
    public static boolean isShooting = false, lastBallStatus = false;

    public static void initialize() {
        // Index Motors
        index1 = new MotorNG(Statics.INDEX_1, Model.VICTOR_SPX, true);
        index2 = new MotorNG(Statics.INDEX_2, Model.TALON_SRX);
        index3 = new MotorNG(Statics.INDEX_3, Model.VICTOR_SPX);

        // Shooting Motors
        feeder = new MotorNG(Statics.FEEDER, Model.VICTOR_SPX, true);
        shooter = new MotorNG(Statics.SHOOTER, Model.FALCON_500, true);

        usIntake = new RangeSensor(Statics.US_INTAKE_PING, Statics.US_INTAKE_ECHO, Type.DIO_US_HC_SR04);

        // Setting up motor speeds
        index1.setSpeed(0.7);
        index2.setSpeed(0.7);
        index3.setSpeed(0.7);
        feeder.setSpeed(0.4);

    }

    public static void update() {

        boolean ballStatus = usIntake.getRangeInches() < 3;
        
        //Indexer

        // Ball is in
        if (usIntake.getRangeInches() < 3 && (ballStatus != lastBallStatus)) {

            if (!intakeTimer.isBusy() && numBalls < 4) {

                numBalls += 1;
                intakeTimer.start();
                index1.move(1);
                index2.move(1);
                index3.move(1);
            }
            else if(numBalls == 4) {
                numBalls = 5;
            }
        }

        if (intakeTimer.getElaspedTimeInMs() > 80) {
            index1.stop();
            index2.stop();
            index3.stop();
            intakeTimer.stop();
            intakeTimer.zero();
        }

        // Autonomous Shooter
        if (Core.robot.gp1.isKeyToggled(Key.B)) {
            new AutonBall(Core.robot.gp1, Key.DPAD_DOWN).run();
            numBalls = 0;
        }

        lastBallStatus = ballStatus;
    }

    public static double getVelocity() {
        return shooter.getVelocity();
    }
}