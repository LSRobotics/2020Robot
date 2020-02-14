package frc.robot.hardware;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.AnalogOutput;
import frc.robot.software.*;

public class PixyCam {

    public static AnalogInput pixy;
    public static AnalogOutput led;
    public static boolean isLedOn = false;

    public static void initialize() {

        pixy = new AnalogInput(Statics.PIXY_CAM);
        led  = new AnalogOutput(0);
        switchLED(true);
    }

    public static double getTargetLocation() {

        /*
        if(!isLedOn) {
            switchLED(true);
        }
        */

        return (pixy.getAverageVoltage()/3.3 * 2) - 1;
    }

    public static void switchLED(boolean on) {
        isLedOn = on;
        if(on) {
            Utils.report("LED On");
            led.setVoltage(5);
        }
        else {
            Utils.report("LED Off");
            led.setVoltage(0);
        }
    }
} 