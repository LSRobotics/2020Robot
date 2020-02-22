package frc.robot.autonomous;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.hardware.Chassis;
import frc.robot.hardware.Gamepad;
import frc.robot.software.Statics;

public class AutonEncoderForward extends AutonBase {

    double distance = 0;
    double target   = 0;

    public AutonEncoderForward(double distance) {
        super();
        this.distance = distance;
    }

    public AutonEncoderForward(double distance, Gamepad interruptGamepad, Gamepad.Key interruptKey) {
        super(interruptGamepad, interruptKey);
        this.distance = distance;
    }

    @Override
    public void preRun() {
        Chassis.stop();

        target = Chassis.getEncoderReading()[0] + (distance * Statics.CHASSIS_ENCODER_UNITS_PER_INCH);
        Chassis.driveRaw(distance > 0 ? 0.5 : -0.5, 0);
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
    }

    @Override
    public boolean isActionDone() {
        return distanceLeft() < 3;
    }

    private double distanceLeft() {
        return Math.abs(Chassis.getEncoderReading()[0] - target) / Statics.CHASSIS_ENCODER_UNITS_PER_INCH;
    }
}