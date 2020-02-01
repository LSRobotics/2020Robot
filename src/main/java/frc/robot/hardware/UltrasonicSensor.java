package frc.robot.hardware;

import edu.wpi.first.wpilibj.Ultrasonic;

public class UltrasonicSensor {

    private Ultrasonic sensor;

    public UltrasonicSensor(int ping, int echo) {
        sensor = new Ultrasonic(ping,echo,Ultrasonic.Unit.kMillimeters);
    }

    public void initialize() {
        sensor.setAutomaticMode(true);
    }

    public double getRangeMM() {
        return sensor.getRangeMM();
    }
}