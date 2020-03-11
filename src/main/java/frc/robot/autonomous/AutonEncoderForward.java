package frc.robot.autonomous;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.components.Shooter;
import frc.robot.hardware.Chassis;
import frc.robot.hardware.Gamepad;
import frc.robot.software.SmartPID;
import frc.robot.software.Statics;
import frc.robot.software.Utils;

public class AutonEncoderForward extends AutonBase {

    //TODO: Tweak PID

    double distance = 0;
    double finalTarget = 0;
    boolean isIntakeEnabled = false;
    SmartPID pid;

    public AutonEncoderForward(double distance, boolean isIntakeEnabled) {
        super();
        this.distance = distance;
        this.isIntakeEnabled = isIntakeEnabled;
    }

    public AutonEncoderForward(double distance, Gamepad interruptGamepad, Gamepad.Key interruptKey, boolean isIntakeEnabled) {
        super(interruptGamepad, interruptKey);
        this.distance = distance;
        this.isIntakeEnabled = isIntakeEnabled;
    }

    //Overloaded Constructors -- No intake by default
    public AutonEncoderForward(double distance, Gamepad interruptGamepad, Gamepad.Key interruptKey) {
        this(distance, interruptGamepad, interruptKey, false);
    }

    public AutonEncoderForward(double distance) {
        this(distance, false);
    }


    @Override
    public void preRun() {
        Chassis.stop();
        Chassis.shift(false);

        pid = new SmartPID(0.005,0,0);

        finalTarget = Chassis.getEncoderReading()[0] + distance * Statics.FALCON_UNITS_PER_INCH;
        
        pid.getSetpoint();

        if(isIntakeEnabled) {
            Shooter.setIntake(true);
        }
        else {
            Shooter.setIntake(false);
        }
    }

    @Override 
    public void duringRun() {  
        Chassis.driveRaw(Utils.cap(pid.calculate(Chassis.getEncoderReading()[0]),0.5),0);
    }

    @Override
    public void postRun() {
        Chassis.stop();
        if(isIntakeEnabled) {
            Shooter.setIntake(false);
        }
    }

    @Override
    public boolean isActionDone() {
        return pid.isActionDone();
    }

}