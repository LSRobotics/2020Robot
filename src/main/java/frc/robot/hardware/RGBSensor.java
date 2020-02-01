package frc.robot.hardware;

import com.revrobotics.ColorSensorV3;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.util.Color;

public class RGBSensor {
    ColorSensorV3 sensor;

    public RGBSensor(Port port) {
        sensor = new ColorSensorV3(port);
    }

    public RGBSensor() {
        this(I2C.Port.kOnboard);
        
    }
    
    public double[] getColor() {
        Color color = sensor.getColor();
        
        return new double[]{color.red, color.green, color.blue};
    }
}