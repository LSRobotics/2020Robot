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

    public static boolean drive(double leftRight, double forwardBack, int millisecond) {
        try {
            report("Moving at speed of (" + leftRight + ", " + forwardBack + ") for" + millisecond + " ms");
            Chassis.drive(forwardBack, leftRight);
            if (!takeABreak(millisecond))
                return false;
            report("Moving done");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean takeABreak() {
        return takeABreak(DEFAULT_BREAK_TIME);
    }

    public static boolean takeABreak(int millisecond) {

        long time = System.currentTimeMillis();

        try {
            report("Idle for " + millisecond + " ms");
            while ((System.currentTimeMillis() - time) < millisecond) {
                if (Robot.gp1.isGamepadChanged() || Robot.gp2.isGamepadChanged()) {
                    report("Interrupted by controller actions");
                    return false;
                }
                // I still need to sleep a bit -- how about a 1/200 sec nap?
                Thread.sleep(5);
            }
            report("Woke up");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    public static boolean turnRobot(double power, double angle) {

        final double ANGLE_TOLERANCE = 1;
        
        if(angle > 0) {power = Math.abs(power);}
        else {
            power = -Math.abs(power);
        }

        double targetAngle = RoboRIO.getAngle() + angle;

        Chassis.drive(0, power);

        while(Math.abs(targetAngle - RoboRIO.getAngle()) > ANGLE_TOLERANCE) {
            if(Robot.gp1.isGamepadChanged() || Robot.gp2.isGamepadChanged()) {
                return false;
            }
        }

        Chassis.stop();

        return true;
        
    }
 
    public static boolean moveByDistance(double targetDistance, double power) {
        
        double integral = 0,
               leftVelocity = 0,
               rightVelocity = 0;
        final int MEASURE_INTERVAL_IN_MILLS  = 20;
        Timer t  = new Timer("Integral-timer");
        
        if(targetDistance < 0) {
            power = -Math.abs(power);
        }
        else if(targetDistance > 0) {
            power = Math.abs(power);
        }

        Chassis.drive(power,0);

        //Make everything positive for easier calculation (In my head lol)
        targetDistance = Math.abs(targetDistance);
        power = Math.abs(power);

        //Start integrating
        while(integral < targetDistance) {
            leftVelocity = Math.abs(RoboRIO.getForwardVelocity());
            t.start();
            
            while(t.getElaspedTimeInMs() < MEASURE_INTERVAL_IN_MILLS) {
                if(Robot.gp1.isGamepadChanged() || Robot.gp2.isGamepadChanged()) {
                    return false;
                }
            }

            t.stop();
            rightVelocity = Math.abs(RoboRIO.getForwardVelocity());

            integral += (leftVelocity + rightVelocity) /2 * t.getElaspedTimeInMs() / 1000;
        }

        //Stop robot
        Chassis.stop();

        return true;
    }

    public static double estimateDriveTime(double speed, double distance) {
        return distance / speed;
    }

    public static void report(String message) {
        if (isOutputEnabled) {
            DriverStation.reportWarning(message, false);
        }
    }

    public static boolean isGamepadBusy() {
        return Robot.gp1.isGamepadChanged() || Robot.gp2.isGamepadChanged();
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
