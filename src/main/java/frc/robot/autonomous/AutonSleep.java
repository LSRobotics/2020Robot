package frc.robot.autonomous;

import frc.robot.software.*;

import frc.robot.hardware.*;

public class AutonSleep extends AutonBase {

    double milliseconds;
    Timer timer = new Timer("Auton Timer");

    public AutonSleep(double milliseconds) {
        super();
        this.milliseconds = milliseconds;
    }

    public AutonSleep(double milliseconds, Gamepad interruptGamepad, Gamepad.Key interruptKey) {
        super(interruptGamepad, interruptKey);
        this.milliseconds = milliseconds;
    }

    @Override
    public void preRun() {
        timer.start();
    }

    @Override
    public boolean isActionDone() {
        return timer.getElaspedTimeInMs() >= milliseconds;
    }

    @Override
    public void postRun() {
        timer.stop();
    }
}