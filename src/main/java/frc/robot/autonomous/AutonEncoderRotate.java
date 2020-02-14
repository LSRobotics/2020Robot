package frc.robot.autonomous;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.hardware.Chassis;
import frc.robot.hardware.Gamepad;
import frc.robot.software.Statics;

public class AutonEncoderRotate extends AutonBase {

    double degreesCw = 0;
    double target   = 0;

    public AutonEncoderRotate(double degreesCw) {
        super();
        this.degreesCw = degreesCw;
    }

    public AutonEncoderRotate(double degreesCw, Gamepad interruptGamepad, Gamepad.Key interruptKey) {
        super(interruptGamepad, interruptKey);
        this.degreesCw = degreesCw;
    }

    @Override
    public void preRun() {
        Chassis.stop();

        target = Chassis.getEncoderReading()[0] + (degreesCw * Statics.CHASSIS_ENCODER_UNITS_PER_DEGREE);
        Chassis.driveRaw(degreesCw > 0 ? 0.5 : -0.5, 0);
    }

    @Override 
    public void duringRun() {

        SmartDashboard.putNumber("degreesCw Left", degreesCwLeft());

        if(degreesCwLeft() < 20) {
            Chassis.driveRaw(degreesCw > 0 ? 0.2 : -0.2,0);
        }
    }

    @Override
    public void postRun() {
        Chassis.stop();
    }

    @Override
    public boolean isActionDone() {
        return degreesCwLeft() < 2;
    }

    private double degreesCwLeft() {
        return Math.abs(Chassis.getEncoderReading()[0] - target) / Statics.CHASSIS_ENCODER_UNITS_PER_INCH;
    }
}