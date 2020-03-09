package frc.robot.components;

import frc.robot.hardware.*;
import frc.robot.hardware.MotorNG.Model;
import frc.robot.hardware.RangeSensor.Type;
import frc.robot.software.*;

public class Shooter {

    public static MotorNG index, shooter, intake;

    // Internal Motors (Slaves)
    private static MotorNG index2, index3;

    public static Solenoid intakeArm;

    public static RangeSensor indexSensor;

    public static int numBalls = 0;
    public static Timer intakeTimer = new Timer("Intake Timer"),
                        spitOutTimer = new Timer("Spit Out Timer");
    public static boolean isShooting = false,
            // lastBallStatus = false,
            isIntakeDown = false, isSpitOut = false;

    public static void initialize() {
        // Index Motors
        index = new MotorNG(Statics.INDEX_1, Model.VICTOR_SPX, true);
        index2 = new MotorNG(Statics.INDEX_2, Model.VICTOR_SPX, true);
        index3 = new MotorNG(Statics.INDEX_3, Model.VICTOR_SPX);

        // Shooting Motors
        shooter = new MotorNG(Statics.SHOOTER, Model.FALCON_500, true);

        indexSensor = new RangeSensor(Statics.IR_INTAKE, Type.ANALOG_RAW);

        // Intake Mechanism
        intake = new MotorNG(Statics.INTAKE, Model.VICTOR_SPX, true);
        intake.setSpeed(0.65);
        intakeArm = new Solenoid(Statics.MASTER_PCM, Statics.ARM_FORWARD, Statics.ARM_REVERSE, "Intake");

        // Setting up motor speeds
        index.setSpeed(0.7);
        index2.setSpeed(0.7);
        index3.setSpeed(0.4);
        // feeder.setSpeed(0.4);

        index.addSlave(index2);
        index.addSlave(index3);

    }

    public static void setIntake(boolean isDown) {

        isIntakeDown = isDown;

        intake.move(isDown ? 1 : 0);
        intakeArm.move(isDown, !isDown);
    }

    public static void actuateIntake() {

        isIntakeDown = !isIntakeDown;

        setIntake(isIntakeDown);
    }

    public static void update() {

        if (!isSpitOut) {
            boolean ballStatus = indexSensor.getRangeInches() > 1.5;
            // Indexer

            // Ball is in
            if (ballStatus) {

                if (!intakeTimer.isBusy()
                // && numBalls < 4
                ) {

                    numBalls += 1;
                    intakeTimer.start();
                    index.move(0.5);
                }
                /*
                 * else if(numBalls == 4) { numBalls = 5; }
                 */
            }

            if (intakeTimer.getElaspedTimeInMs() > 250) {
                index.stop();
                intakeTimer.stop();
                // intakeTimer.zero();
            }
            else if(spitOutTimer.getElaspedTimeInMs() > 2000) {
                index.stop();
                spitOutTimer.stop();
                isSpitOut = false;
            }

            // lastBallStatus = ballStatus;
        }
    }

    public static void spitOut() {
        index.move(-1);
        isSpitOut = true;
        spitOutTimer.start();
    }

    public static double getVelocity() {
        return shooter.getVelocity();
    }

    public static int getNumBalls() {
        return numBalls;
    }
}