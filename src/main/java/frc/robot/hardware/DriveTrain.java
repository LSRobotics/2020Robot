package frc.robot.hardware;
import frc.robot.software.*;

/**
 * Created by TylerLiu on 2018/01/22.
 */
public class DriveTrain {

    enum Chassis {
        NORMAL,
        MECANUM,
        OMNI
    }

    private static double speed = 1.0;
    private static Motor lFront,
                         lBack,
                         rFront,
                         rBack;
    private static boolean is4WD;
    private static Chassis chassis = Chassis.NORMAL;
    final private static Motor.Model DEFAULT_MODEL = Motor.Model.VICTOR_SP;

    /**
     * 4WD Constructor
     * 
     * @param lFrontPort
     * @param lBackPort
     * @param rFrontPort
     * @param rBackPort
     * @param model       Motor Model for ALL wheels (It should be the same since
     *                    otherwise it would not function well physically)
     */
    public static void init(int lFrontPort, int lBackPort, int rFrontPort, int rBackPort,
            Motor.Model model) {
        is4WD = true;
        lBack = new Motor(lBackPort, model, false);
        rBack = new Motor(rBackPort, model, true);
        lFront = new Motor(lFrontPort, model, false);
        rFront = new Motor(rFrontPort, model, true);

    }

    /**
     * Overloaded 4WD Constructor using DEFAULT_MODEL
     * @param lFrontPort
     * @param lBackPort
     * @param rFrontPort
     * @param rBackPort
     */
    public static void init(int lFrontPort, int lBackPort, int rFrontPort, int rBackPort) {
        is4WD = true;
        lBack = new Motor(lBackPort, DEFAULT_MODEL, false);
        rBack = new Motor(rBackPort, DEFAULT_MODEL, true);
        lFront = new Motor(lFrontPort, DEFAULT_MODEL, false);
        rFront = new Motor(rFrontPort, DEFAULT_MODEL, true);

    }

    /**
     * 2WD Constructor
     * 
     * @param lPort
     * @param rPort
     * @param model
     */
    public static void init(int lPort, int rPort, Motor.Model model) {
        is4WD = false;
        lBack = new Motor(lPort, model, false);
        rBack = new Motor(rPort, model, true);
    }

    /**
     * Overloaded 2WD Constructor using DEFAULT_MODEL
     * 
     * @param lPort
     * @param rPort
     */
    public static void init(int lPort, int rPort) {
        is4WD = false;
        lBack = new Motor(lPort, DEFAULT_MODEL, false);
        rBack = new Motor(rPort, DEFAULT_MODEL, true);
    }

    public static void setChassisType(Chassis robotChassis) {
        chassis = robotChassis;
    }

    public static void drive(double leftRight, double forwardBack, double sideMove) {
        switch (chassis) {
            case NORMAL:
                tankDrive(leftRight, forwardBack);
                break;
            case MECANUM:
            case OMNI:
                mecanumDrive(sideMove, forwardBack,leftRight);
                break;
        }
    }

    /**
     * tankDrive method
     *
     * @param leftRight x value of the stick (For rotation)
     * @param forwardBack y value of the stick (For forward & back)
     */
    public static void tankDrive(double leftRight, double forwardBack) {
        forwardBack = -forwardBack; // FRC & FTC 2018 Tuning

        // Calculate Adequate Power Level for motors
        final double leftPower = RobotUtil.clipValue(forwardBack + leftRight, -1.0, 1.0);
        final double rightPower = RobotUtil.clipValue(forwardBack - leftRight, -1.0, 1.0);

        // Pass calculated power level to motors
        lBack.move(getLimitedSpeed(leftPower));
        rBack.move(getLimitedSpeed(rightPower));

        if (is4WD) {
            lFront.move(getLimitedSpeed(leftPower));
            rFront.move(getLimitedSpeed(rightPower));
        }
    }

    /**
     *
     * mecanumDrive method
     *
     * @param sideMove    value for sideMove, ranging from -1 to 1
     * @param forwardBack value for moving forward and back, ranging also from -1 to
     *                    1
     * @param rotation    value for robot rotation, ranging from -1 to 1
     */
    public static void mecanumDrive(double sideMove, double forwardBack, double rotation) {

        forwardBack = -forwardBack;
        sideMove = -sideMove;

        // A little Math from
        // https://ftcforum.usfirst.org/forum/ftc-technology/android-studio/6361-mecanum-wheels-drive-code-example
        double r = Math.hypot(sideMove, forwardBack);
        double robotAngle = Math.atan2(forwardBack, sideMove) - Math.PI / 4;

        lFront.move(getLimitedSpeed(r * Math.cos(robotAngle) + rotation));
        rFront.move(getLimitedSpeed(r * Math.sin(robotAngle) - rotation));
        lBack.move(getLimitedSpeed(r * Math.sin(robotAngle) + rotation));
        rBack.move(getLimitedSpeed(r * Math.cos(robotAngle) - rotation));
    }

    /**
     *
     * @param a the original speed
     * @return the limited speed
     */
    private static double getLimitedSpeed(double a) {
        return a * speed;
    }

    /**
     * Updates the speed limit
     *
     * @param newSpeedLimit the new speed limit
     */
    public static void setSpeed(double newSpeed) {
        speed = newSpeed;
    }

    public static void flip() {
        lBack.flip();
        rBack.flip();

        if (is4WD) {
            lFront.flip();
            rFront.flip();
        }
    }
}
