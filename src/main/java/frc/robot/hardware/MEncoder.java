package frc.robot.hardware;

import edu.wpi.first.wpilibj.Encoder;

public class MEncoder extends Encoder{

    public MEncoder(int portA, int portB) {
        super(portA,portB,false,EncodingType.k2X);
        reset();
    }

    public void calibrate(double wheelDiameter, double gearRatio, double pulsesPerRev) {
        setDistancePerPulse(wheelDiameter * Math.PI / pulsesPerRev);
    }

    public void reset() {
        reset();
    }


}