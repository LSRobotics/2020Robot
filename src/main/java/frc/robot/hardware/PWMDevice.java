package frc.robot.hardware;

import edu.wpi.first.wpilibj.PWM;

final public class PWMDevice extends PWM {
    private PWM device;

    public PWMDevice(int portNumber) {
        super(portNumber);
    }

    public void moveBySpeed(double speed) {
        device.setSpeed(speed);
    }

    public void moveByPosition(double position) {
        device.setPosition(position);
    }

}
