package frc.robot.hardware;

import edu.wpi.first.wpilibj.DoubleSolenoid;
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
    public Status status = Status.DISABLED;

    public Solenoid(int fPort, int rPort) {
        this(0,fPort,rPort);
    }

    public Solenoid(int deviceId, int fPort, int rPort) {
        
        compressor = new Compressor();
        solenoid = new DoubleSolenoid(deviceId, fPort, rPort);
        solenoid.set(DoubleSolenoid.Value.kOff);
    }

    public void move(boolean forward, boolean reverse) {
        if (forward == reverse) {
            return;
        } // Ignore input if both buttons are held / released
        else if (forward) {
            solenoid.set(DoubleSolenoid.Value.kForward);
            Utils.report("Solenoid Forward");
            status = Status.FORWARD;
        } else {
            solenoid.set(DoubleSolenoid.Value.kReverse);
            Utils.report("Soleniod Reverse");
            status = Status.REVERSE;
        }
    }

    public void actuate() {

        //Flip whatever we have now
        boolean isForward = !(status == Status.FORWARD);

        move(isForward, !isForward);
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Pressure Status: " + (this.compressor.getPressureSwitchValue() ? "Good" : "Too High") + "\tCurrent: "
                + this.compressor.getCompressorCurrent() + "A";
    }
}
