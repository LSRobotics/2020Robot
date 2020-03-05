package frc.robot.hardware;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import frc.robot.software.*;
import edu.wpi.first.wpilibj.Compressor;

public class Solenoid {

    public enum Status {
        FORWARD,
        REVERSE,
        DISABLED
    }

    private Compressor compressor;
    private DoubleSolenoid solenoid;
    private String name;
    public Status lastStatus = Status.DISABLED;

    public Solenoid(int fPort, int rPort) {
        this(0,fPort,rPort);
    }

    public Solenoid(int deviceId, int fPort, int rPort) {
        this(deviceId, fPort, rPort, "Default");
    }

    public Solenoid(int deviceId, int fPort, int rPort, String name) {
        
        this.name = name;
        compressor = new Compressor();
        solenoid = new DoubleSolenoid(deviceId, fPort, rPort);
        solenoid.set(DoubleSolenoid.Value.kOff);
    }

    public void move(boolean forward, boolean reverse) {

        Status temp;

        if(forward == reverse) {
            return;
        }
        else if(forward) {
            temp = Status.FORWARD;
        }
        else {
            temp = Status.REVERSE;
        }

        if(temp == lastStatus) {
            return;
        }
        else {
            lastStatus = temp;
            solenoid.set(temp == Status.FORWARD? Value.kForward : Value.kReverse);

            Utils.report(name + " solenoid is now " + (temp == Status.FORWARD? "FORWARD." : "REVERSE."));
        }
    }

    public void actuate() {

        //Flip whatever we have now
        boolean isForward = !(lastStatus == Status.FORWARD);

        move(isForward, !isForward);
    }

    public Status getStatus() {
        return lastStatus;
    }

    @Override
    public String toString() {
        return "Pressure Status: " + (this.compressor.getPressureSwitchValue() ? "Good" : "Too High") + "\tCurrent: "
                + this.compressor.getCompressorCurrent() + "A";
    }
}
