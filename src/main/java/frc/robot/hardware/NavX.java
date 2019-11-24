package frc.robot.hardware;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;
import frc.robot.constants.*;

public class NavX {
    static AHRS navx;

    public static void initialize() {

        try {
            navx = new AHRS(SPI.Port.kMXP);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static double getVelocity (Axis axis) {
        
        if(axis == Axis.X) {
            return navx.getVelocityX();
        }
        else if(axis == Axis.Y) {
            return navx.getVelocityY();
        }
        else {
            return navx.getVelocityZ();
        }

    }

    public static double getDisplacement (Axis axis) {
        
        if(axis == Axis.X) {
            return navx.getDisplacementX();
        }
        else if(axis == Axis.Y) {
            return navx.getDisplacementY();
        }
        else {
            return navx.getDisplacementZ();
        }

    }

    public static double getAngle() {
        return navx.getAngle();
    }

}