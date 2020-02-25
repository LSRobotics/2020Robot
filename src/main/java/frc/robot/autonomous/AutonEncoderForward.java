package frc.robot.autonomous;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.hardware.Chassis;
import frc.robot.hardware.Gamepad;
import frc.robot.software.Statics;
import frc.robot.software.*;

public class AutonEncoderForward extends AutonBase {

    double distance = 0;
    double target   = 0;
    boolean isIntakeEnabled = false;

    public AutonEncoderForward(double distance, boolean isIntakeEnabled) {
        super();
        this.distance = distance;
        this.isIntakeEnabled = isIntakeEnabled;
    }

    public AutonEncoderForward(double distance, Gamepad interruptGamepad, Gamepad.Key interruptKey, boolean isIntakeEnabled) {
        super(interruptGamepad, interruptKey);
        this.distance = distance;
        this.isIntakeEnabled = isIntakeEnabled;
    }

    //Overloaded Constructors -- No intake by default
    public AutonEncoderForward(double distance, Gamepad interruptGamepad, Gamepad.Key interruptKey) {
        this(distance, interruptGamepad, interruptKey, false);
    }

    public AutonEncoderForward(double distance) {
        this(distance, false);
    }


    @Override
    public void preRun() {
        Chassis.stop();

        target = Chassis.getEncoderReading()[0] + (distance * Statics.CHASSIS_ENCODER_UNITS_PER_INCH);
        Chassis.driveRaw(distance > 0 ? 0.5 : -0.5, 0);
        
        if(isIntakeEnabled) {
            Core.robot.intake.move(1);
        }
    }

    @Override 
    public void duringRun() {

        SmartDashboard.putNumber("Distance Left", distanceLeft());

        if(distanceLeft() < 20) {
            Chassis.driveRaw(distance > 0 ? 0.2 : -0.2,0);
        }
    }

    @Override
    public void postRun() {
        Chassis.stop();
        if(isIntakeEnabled) {
            Core.robot.intake.move(0);
        }
    }

    @Override
    public boolean isActionDone() {
        return distanceLeft() < 3;
    }

    private double distanceLeft() {
        return Math.abs(Chassis.getEncoderReading()[0] - target) / Statics.CHASSIS_ENCODER_UNITS_PER_INCH;
    }
}