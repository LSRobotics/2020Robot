package frc.robot.autonomous;

import javax.annotation.OverridingMethodsMustInvokeSuper;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.hardware.*;
import frc.robot.software.SmartPID;

public class AutonPixyAlign extends AutonBase {
    //221 inches is pixy range

    double target;
    SmartPID pid;

    public AutonPixyAlign (double target) {
        super();
        this.target = target;
    }

    public AutonPixyAlign (double target, Gamepad killGp, Gamepad.Key killKey) {
        super(killGp,killKey);
    }
    
    @Override
    public void preRun() {
        pid = new SmartPID(1.5, 0, 0.0);
        //working values (1.75, 0, 0.6)
        pid.setSetpoint(target);
    }

    @Override
    public void duringRun() {
        double val = -pid.next(PixyCam.getTargetLocation());
        Chassis.driveRaw(0,val);

        SmartDashboard.putNumber("PIXY PID Calc", val);
        robot.postData();
    }

    @Override
    public boolean isActionDone() {
        return pid.isActionDone();
    }

    @Override
    public String toString() {
        return "AutonPixyAlign " + target;
    }
}