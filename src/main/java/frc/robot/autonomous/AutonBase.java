package frc.robot.autonomous;

import frc.robot.hardware.*;
import frc.robot.*;
import frc.robot.software.*;

public class AutonBase {
    
    Gamepad interruptGamepad;
    Gamepad.Key interruptKey;
    Robot robot;
    boolean isAutonPeriod = false;

    public AutonBase(Gamepad interruptGamepad, Gamepad.Key interruptKey) {
        this.interruptGamepad = interruptGamepad;
        this.interruptKey = interruptKey;
        isAutonPeriod = false;
        robot = Core.robot;
    }

    public AutonBase() {
        isAutonPeriod = true;
        robot = Core.robot;
    }

    final public boolean run() {

        preRun();
    
        while(true) {
        
            duringRun();

            if(!isAutonPeriod && !isGamepadGood()) {
                postRun();
                return false;
            } 
            else if (isActionDone()) {
                break;
            }
 
        }
        
        postRun();
        
        return true;
    }

    public void preRun() {
        
    }

    public void duringRun() {

    }

    final public boolean isGamepadGood() {
        return interruptGamepad.getRawReading(interruptKey) == 0;
    }
 
    public boolean isActionDone() {
        return true;
    }


    public void postRun() {

    }
    
}