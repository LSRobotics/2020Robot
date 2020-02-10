package frc.robot.autonomous;

import frc.robot.hardware.*;
import frc.robot.software.*;

public class AutonDetectLine extends AutonBase {
    public double[] color = {};
    public static boolean isBlueLine, isRedLine;

    public AutonDetectLine (double color[]) {
        super();
        this.color = color;
    }

    public AutonDetectLine (double color[], Gamepad killGp, Gamepad.Key killKey) {
        super(killGp,killKey);
        this.color = color;
        
    }
    
    @Override
    public void preRun() {
        Chassis.driveRaw(0.2, 0);
    }

    @Override
    public void duringRun() {
        color = robot.colorSensor.getColor();

        isBlueLine = Utils.isColorMatch(color, Statics.TAPE_BLUE, 0.06);
        isRedLine = Utils.isColorMatch(color, Statics.TAPE_RED, 0.06);
    
    }

    @Override
    public boolean isActionDone() {
        return (isBlueLine || isRedLine);
    }

    @Override
    public void postRun() {
        Chassis.stop();
    }
}