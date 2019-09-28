package frc.robot.hardware;

import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.SPI;
import com.kauailabs.navx.frc.AHRS;
import frc.robot.software.Utils;
import frc.robot.software.Statics;

public class RoboRIO {

    public enum OnBoardDevice {
        ROBORIO,
        NAVX
    }

    private static BuiltInAccelerometer accel;
    private static char rioForwardAxis = 'Y';
    private static char navxForwardAxis = 'Y';
    private static AHRS navx;

    public static void initialize() {
        
        try {
        accel = new BuiltInAccelerometer();

        navx = new AHRS(SPI.Port.kMXP);
        } catch(Exception e) {
            if(Statics.DEBUG_MODE) {
                Utils.report(e.toString());
            }
        }
    }

    public static BuiltInAccelerometer getAccelerometer() {
        return accel;
    }

    public static AHRS getNavx() {
        return navx;
    }

    public static double getAngle() {
        return navx.getAngle();
    }

    public static void setForwardAxis(OnBoardDevice device,char axis) {
        switch(device) {
            case ROBORIO:
                rioForwardAxis = Character.toUpperCase(axis);
                break;
            case NAVX:
                navxForwardAxis = Character.toUpperCase(axis);    
                break;
            default: break;        
        }
    }

    public static double getForwardVelocity() {
        switch(Character.toUpperCase(navxForwardAxis)) {
            case 'X' : 
                return navx.getVelocityX();
            case 'Y' : 
                return navx.getVelocityY();
            case 'Z' : 
                return navx.getVelocityZ();
            default: return 0;
        }
    }

    public static double [] getVelocity(){
        return new double[]{navx.getVelocityX(), navx.getVelocityY(), navx.getVelocityZ()};
    }

    public static double [] getAcceleration(boolean isBuiltInAcclerator) {
        if(isBuiltInAcclerator) {
            return new double[] {accel.getX(), accel.getY(), accel.getZ()};
        }
        else {
            return new double[] {navx.getWorldLinearAccelX(),navx.getWorldLinearAccelY(), navx.getWorldLinearAccelZ()};
        }
    }

    public static double getForwardAcceleration() {

        switch(Character.toUpperCase(rioForwardAxis)) {
            case 'X' : 
                return accel.getX();
            case 'Y' : 
                return accel.getY();
            case 'Z' : 
                return accel.getZ();
            default: return 0;
        }
    }
}
