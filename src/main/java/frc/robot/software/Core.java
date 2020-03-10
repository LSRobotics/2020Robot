package frc.robot.software;

import edu.wpi.first.wpilibj.Compressor;
import frc.robot.*;

public class Core {

    public static boolean isDisabled = true;
    public static Robot robot;
    public static Compressor compressor;

    public static void initialize(Robot main) {
        robot = main;
        compressor = new Compressor();
    }

    public static boolean isPressureGood() {
        return compressor.getPressureSwitchValue();
    }

}