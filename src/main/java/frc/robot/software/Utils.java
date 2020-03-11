package frc.robot.software;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.constants.SpeedCurve;

public class Utils {

    public enum BotLocation {
        LEFT, MIDDLE, RIGHT;
    }

    private static String gameData;
    final public static int DEFAULT_BREAK_TIME = 1200;
    public static boolean isOutputEnabled = Statics.DEBUG_MODE;
    public static Timer sysTimer = new Timer();

    static public double getCurvedValue(SpeedCurve curve, double value) {
        if (curve == SpeedCurve.LINEAR)
            return value;
        else if (curve == SpeedCurve.SQUARED) {
            boolean isNegative = value < 0;

            return Math.pow(value, 2) * (isNegative ? -1 : 1);
        } else {
            return Math.pow(value, 3);
        }
    }

    public static void fetchGameData() {

        // gamedata
        String dataBuffer;
        while (true) {
            dataBuffer = DriverStation.getInstance().getGameSpecificMessage();
            if (dataBuffer.length() > 0)
                break;
        }

        gameData = dataBuffer;
        report("GameData: " + gameData);

    }

    public static boolean isBlueAlliance() {
        return DriverStation.getInstance().getAlliance() == DriverStation.Alliance.Blue;
    }

    public static BotLocation getLocation() {

        return BotLocation.values()[(DriverStation.getInstance().getLocation() - 1)];

    }

    public static void report(String message) {
        if (isOutputEnabled) {

            String finalMsg = "[5181] " + ((double)sysTimer.getElaspedTimeInMs() / 1000) + "s : " + message;

            //I am "hacking" driver station -- read their implementation and bypassed their wrappers
            HAL.sendError(false, 1, false, finalMsg, new String(), new String(), true);
            
            //DriverStation.reportWarning(message, false);
        }
    }

    public static void sleep(long miliseconds) {
        try {
            Thread.sleep(miliseconds);
        } catch (Exception e) {
            //Shhhh
        }
    }

    /**
     *
     * @param value the original value
     * @param min   the minimum value allowed in the range
     * @param max   the maximum value allowed in the range
     * @return the value within the range
     */
    public static double clipValue(double value, double min, double max) {
        if (value >= max)
            return max;
        else if (value <= min)
            return min;
        else
            return value;
    }

    public static double mapAnalog(double value) {
        return mapAnalog(value, Statics.OFFSET_MIN, Statics.OFFSET_MAX);
    }

    public static double cap(double value, double absMax) {
        return cap(value, -Math.abs(absMax), Math.abs(absMax));
    }

    public static double cap(double value, double min, double max) {
        if(value > max) {
            return max;
        }
        else if (value < min) {
            return min;
        }
        else {
            return min;
        }
    }

    public static double mapAnalog(double value, double absMin, double absMax) {

        boolean isNegative = (value < 0);

        value = Math.abs(value);

        if (value < absMin) {
            return 0;
        } else if (value > absMax || value == absMax) {
            return isNegative ? -1 : 1;
        } else {
            return (value - absMin) / absMax * (isNegative ? -1 : 1);
        }
    }

    public static boolean isDataInRange(double value, double min, double max) {

        if (min > max) {
            double temp = max;
            max = min;
            min = temp;
        }

        return (value == min || value > min) && (value < max || value == max);
    }

    public static boolean isColorMatch(double[] data, double[] target, double tolerance) {
        for (int i = 0; i < data.length; ++i) {
            if (Math.abs(data[i] - target[i]) > tolerance) {
                return false;
            }
        }
        return true;
    }

    public static boolean isDataClose(double value, double expected, double tolerance) {
        return Math.abs(value - expected) < tolerance || (Math.abs(value - expected)) == tolerance
                || Math.abs(value - expected) == 0;
    }

}