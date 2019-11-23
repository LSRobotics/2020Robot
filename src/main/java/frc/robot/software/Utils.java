package frc.robot.software;

import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.Robot;
import frc.robot.hardware.*;

public class Utils {

    public enum BotLocation {
        LEFT, MIDDLE, RIGHT;
    }

    private static String gameData;
    final public static int DEFAULT_BREAK_TIME = 1200;
    public static boolean isOutputEnabled = true;

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
            DriverStation.reportWarning(message, false);
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

    public static double mapAnalog(double value, double absMin, double absMax) {
        
        boolean isNegative = (value < 0);
        
        value = Math.abs(value);

        if(value < absMin) {
            return 0;
        }
        else if(value > absMax || value == absMax) {
            return isNegative ? -1 : 1;
        }
        else {
            return (value - absMin) / absMax * (isNegative ? -1 : 1);
        }
    }
}
