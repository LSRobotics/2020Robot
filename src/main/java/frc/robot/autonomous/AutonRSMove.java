package frc.robot.autonomous;

import frc.robot.hardware.*;
import frc.robot.hardware.Gamepad.Key;

//drives robot until the sensor reads a set distance
public class AutonRSMove extends AutonBase{
    
    RangeSensor sensor;
    double targetDistance;
    double diff = 10000;
    //SmartPID pid;

    public AutonRSMove(RangeSensor sensor, double targetDistance) {
        super();
        this.sensor = sensor;
        this.targetDistance = targetDistance;
    }

    public AutonRSMove(RangeSensor sensor, double targetDistance, Gamepad interrputGamepad, Key interrputKey) {
        super(interrputGamepad,interrputKey);
        this.sensor = sensor;
        this.targetDistance = targetDistance;
    }
    
    @Override
    public void preRun() {
        //pid = new SmartPID(1.2, 0, 1);
        //pid.setSetpoint(targetDistance);
    }

    @Override
    public void duringRun() {
        
        double distanceLeft = sensor.getRangeInches() - targetDistance;

        diff = distanceLeft;

        if(Math.abs(distanceLeft) < 10) {
            Chassis.driveRaw((distanceLeft > 0) ? 0.1 : -0.1, 0);
        }
        else {
            Chassis.driveRaw((distanceLeft > 0) ? 0.3 : -0.3, 0);
        }

        //Chassis.driveRaw(-pid.next(sensor.getRangeInches()) * 0.2,0);
    }

    @Override
    public boolean isActionDone() {
        
        return Math.abs(diff) < 3;
        //return pid.atSetpoint();
        //return pid.isActionDone();
    }

    @Override
    public String toString() {
        return "Auton Range Sensor Move " + targetDistance;
    }
}