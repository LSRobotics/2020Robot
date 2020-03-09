package frc.robot.autonomous;

import frc.robot.hardware.*;
import frc.robot.software.*;
import frc.robot.components.*;
import frc.robot.constants.*;

public class AutonClimb extends AutonBase {
    
    private boolean isRetract = false;
    public SmartPID pid;

    public AutonClimb(boolean isRetract) {
        super();
        this.isRetract = isRetract;
    }

    @Override
    public void preRun() {
        pid = new SmartPID(0.25,0,0);
        pid.setSetpoint(isRetract? 55600 : 728100);

        Chassis.stop();

        //Unlock
        Climb.run(0.5);

        Utils.sleep(300);
    }

    @Override
    public void duringRun() {
        Climb.manualRun(pid.calculate(Climb.getLocation()));
    }

    @Override
    public boolean isActionDone() {
        return pid.isActionDone();
    }

    @Override
    public void postRun() {

        //Lock
        Climb.run(0);
    }
}