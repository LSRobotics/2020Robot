package frc.robot.hardware;

import edu.wpi.first.wpilibj.Spark;
import frc.robot.software.Statics;

public class Lights {
    public static Spark lightSpark;
    public static int port = Statics.Light_PWM_Port;
    public static boolean isAuton = false;

    public static void initialize() {
        lightSpark = new Spark(port);
    }

    public static synchronized void lightChange(double lightMode) {
        if(!isAuton) {
            lightSpark.set(lightMode);
        }
    }
}