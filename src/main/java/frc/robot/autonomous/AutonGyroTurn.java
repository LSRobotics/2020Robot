package frc.robot.autonomous;

import edu.wpi.first.wpilibj.controller.PIDController;
import frc.robot.hardware.*;

public class AutonGyroTurn extends AutonBase {
    
    double targetAngle;
    PIDController pid = new PIDController(.045, .85, .005); // variables you test

    public AutonGyroTurn (double targetAngle) {
        super();
        this.targetAngle = targetAngle;
    }

    public AutonGyroTurn (double targetAngle, Gamepad killGp, Gamepad.Key killKey) {
        super(killGp,killKey);

        this.targetAngle = targetAngle;
    }
    
    @Override
    public void preRun() {
        pid.setSetpoint(targetAngle);
    }

    @Override
    public void duringRun() {
        Chassis.driveRaw(0,-pid.calculate(NavX.navx.getYaw())* 0.15);
    }

    @Override
    public boolean isActionDone() {
        return pid.atSetpoint();
    }
}