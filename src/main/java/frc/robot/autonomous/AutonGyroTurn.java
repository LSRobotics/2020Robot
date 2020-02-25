package frc.robot.autonomous;

import frc.robot.hardware.*;
import frc.robot.software.SmartPID;

public class AutonGyroTurn extends AutonBase {
    
    double targetAngle;
    SmartPID pid;

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
        //pid = new SmartPID(1.5, 0, 0);
        pid =  new SmartPID(.045, .85, .05);
        pid.setSetpoint(targetAngle);
    }

    @Override
    public void duringRun() {
        //Chassis.driveRaw(0,pid.next(NavX.navx.getYaw()));
        Chassis.driveRaw(0,pid.next(NavX.navx.getYaw()) * .2);
    }

    @Override
    public boolean isActionDone() {
        return pid.atSetpoint();
        //return pid.isActionDone();
    }

    @Override
    public String toString() {
        return "AutonGyroTurn " + targetAngle;
    }
}