package frc.robot.autonomous;

import frc.robot.hardware.*;
import frc.robot.hardware.Gamepad.Key;
import frc.robot.*;
import frc.robot.software.*;

public class AutonBase {
    
    Gamepad interruptGamepad;
    Gamepad.Key interruptKey;
    Robot robot;

    public AutonBase(Gamepad interruptGamepad, Gamepad.Key interruptKey) {
        this.interruptGamepad = interruptGamepad;
        this.interruptKey = interruptKey;
        //isAutonPeriod = false;
        robot = Core.robot;
    }

    public AutonBase() {
        this(Core.robot.gp1, Key.DPAD_DOWN);
        robot = Core.robot;
    }

    /**
     * Core function for using any object based on this class -- call this to run, DO NOT OVERRIDE
     * @return whether the behavior is successfully executed (Fail: Driver interruption, otherwise success).
     */
    final public boolean run() {

        Utils.report("Start Running " + toString());
        preRun();
        

        Utils.report("Looping " + toString());
        while(true) {

            interruptGamepad.fetchData();

            duringRun();
            robot.postData();

            if(!isGamepadGood() || Core.isDisabled) {
                postRun();
                return false;
            } 
            else if (isActionDone()) {
                break;
            }
 
        }
        
        postRun();
        
        Utils.report("Finish Running " + toString());

        return true;
    }

    /**
     * A function that will be called prior to looping in run(), override this when needed.
     */
    public void preRun() {
        
    }

    /**
     * A function that will be called periodically in run(), override this when needed.
     */
    public void duringRun() {

    }

    /**
     * A function that determines whether the driver has interrupted this autonomous behavior. DO NOT OVERRIDE.
     */
    final public boolean isGamepadGood() {
        return interruptGamepad.getRawReading(interruptKey) == 0;
    }
 
    /**
     * A function that determines whether the action is done (quit looping). MUST OVERRIDE THIS
     * @return
     */
    public boolean isActionDone() {
        return true;
    }

    /**
     * A function that is called after looping in run(). Override when needed, and the robot would stop its chassis by default.
     */
    public void postRun() {
        Chassis.stop();
    }

    /**
     * A function that may be called for debugging usages. It is RECOMMENDED to override this.
     */
    @Override
    public String toString() {
        return "AutonBase";
    }
    
}