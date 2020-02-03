package frc.robot.autonomous;

import frc.robot.hardware.*;

public class AutonGyroTurn extends AutonBase {
    
    double targetAngle;

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
        robot.gyroPID.setSetpoint(targetAngle);
    }

    @Override
    public void duringRun() {
        Chassis.driveRaw(0,robot.gyroPID.calculate(NavX.navx.getYaw())* 0.15);
    }

    @Override
    public boolean isActionDone() {
        return robot.gyroPID.atSetpoint();
    }
}