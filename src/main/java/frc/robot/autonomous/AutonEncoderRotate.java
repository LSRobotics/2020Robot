package frc.robot.autonomous;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.hardware.Chassis;
import frc.robot.hardware.Gamepad;
import frc.robot.software.SmartPID;
import frc.robot.software.Statics;

public class AutonEncoderRotate extends AutonBase {

    //TODO: Tweak PID
    double degreesCw = 0;
    double target    = 0;
    SmartPID pid;

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

        target = Chassis.getEncoderReading()[0] + (degreesCw * Statics.FALCON_UNITS_PER_INCH);
        
        pid = new SmartPID(.045, .85, .05);
        
        pid.setSetpoint(target);
    }

    @Override 
    public void duringRun() {

        SmartDashboard.putNumber("degreesCw Left", degreesCwLeft());

        Chassis.drive(0, pid.calculate(Chassis.getEncoderReading()[0]) * 0.5);

    }

    @Override
    public void postRun() {
        Chassis.stop();
        pid.clearHistory();
    }

    @Override
    public boolean isActionDone() {
        return pid.isActionDone();
    }

    private double degreesCwLeft() {
        return Math.abs(Chassis.getEncoderReading()[0] - target) / Statics.FALCON_UNITS_PER_INCH;
    }
}