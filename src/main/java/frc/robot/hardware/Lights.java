package frc.robot.hardware;

import edu.wpi.first.wpilibj.Spark;
import frc.robot.software.Statics;

public class Lights {
    private static Spark lightSpark;
    private static boolean isLocked = false;

    public static void initialize() {
        lightSpark = new Spark(Statics.Light_PWM_Port);
    }

    public static void lock(boolean isOutputLocked) {
        isLocked = isOutputLocked;
    }

    public static synchronized void lightChange(double lightMode) {
        if(!isLocked) {
            lightSpark.set(lightMode);
        }
    }
}