package frc.robot.autonomous;

import frc.robot.hardware.*;
import frc.robot.*;
import frc.robot.software.*;

public class AutonBase {
    
    Gamepad interruptGamepad;
    Gamepad.Key interruptKey;
    Robot robot;
    //boolean isAutonPeriod = false;

    public AutonBase(Gamepad interruptGamepad, Gamepad.Key interruptKey) {
        this.interruptGamepad = interruptGamepad;
        this.interruptKey = interruptKey;
        robot = Core.robot;
    }

    public AutonBase() {
        this(Core.robot.gp1,Gamepad.Key.DPAD_DOWN);
    }

    final public boolean run() {

        preRun();
    
        while(true) {
        
            duringRun();

            if(!isGamepadGood()) {
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