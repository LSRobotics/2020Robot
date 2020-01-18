package frc.robot.software;

import frc.robot.hardware.*;
import frc.robot.constants.*;

public class AutoPilot {
    public static boolean driveForwardByNavX(double meters) {
        
        double integral = 0;

        final Gamepad gp1 = Core.robot.gp1,
                      gp2 = Core.robot.gp2;

        Timer t = new Timer();

        t.start();

        while (integral <= meters) {
            
            long runtime = t.getElaspedTimeInMs();

            if(runtime <= 1000) {

                //Y = 1 / 2000 x + 0.2 where 0 <= x <= 1000
                //So the Chassis would "constantly" accelerate in the first second, ranging motor output from 0.2 to 0.7
                //Hopefully this reduces the error rate from NavX
                Chassis.driveRaw( 1 / 2000 * runtime + 0.2 , 0);
            }
            
            integral += NavX.getDisplacement(Axis.X);

            //Driver Intervention, cuts the current autonomous operation and falls back to manual control
            if(gp1.isGamepadChanged() || gp2.isGamepadChanged()) {
                Chassis.stop();
                return false;
            }

        }
    
        Chassis.stop();

        return true;

    }

    public static boolean turnRobotByNavX(double angle) {
        
        final Gamepad gp1 = Core.robot.gp1,
                      gp2 = Core.robot.gp2;

        double target = NavX.getAngle() + angle;

        final double power = 0.5 * (angle < 0 ? -1 : 1);

        Chassis.driveRaw(0, power);

        while(NavX.getAngle() < target) {
            if(gp1.isGamepadChanged() || gp2.isGamepadChanged()) {
                Chassis.stop();
                return false;
            }
        }

        Chassis.stop();
        
        return true;
    }

    public static boolean turnRobotByTime(boolean isLeft) {

        final double power = isLeft ? -0.5 : 0.5;

        Timer t = new Timer("Robot Turn");

        Chassis.stop();
        Chassis.driveRaw(0, power);

        t.start();

        while (t.getElaspedTimeInMs() < Statics.TIME_PER_360 /4) {
            Core.robot.gp1.fetchData();
            Core.robot.gp2.fetchData();

            // Interrupt action if LB in GP1 is toggled
            if (Core.robot.gp1.isKeyToggled(Gamepad.Key.LB)) {
                Chassis.stop();
                return false;
            }

            Core.robot.updateTop();
        }

        Chassis.stop();

        return true;
    }
}