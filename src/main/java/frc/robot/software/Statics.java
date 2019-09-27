package frc.robot.software;

import frc.robot.hardware.*;
import frc.robot.hardware.Motor.WorkMode;

public class Statics {

    //Chassis
    final static public int DRIVE_LF = 0,
                            DRIVE_LB = 1,
                            DRIVE_RF = 2,
                            DRIVE_RB = 3;

    //Xbox Controllers
    final static public int XBOX_CTRL_1 = 0,
                            XBOX_CTRL_2 = 1;
    
    //Actuators
    final static public int MOTOR_LOW_SHOOTER = 4,
                            MOTOR_HIGH_SHOOTER = 5,
                            MOTOR_ARM_ROLLER = 6,
                            MOTOR_ARM_LEFT = 7,
                            MOTOR_ARM_RIGHT = 8;

    //Solenoids
    final static public int SOLENOID_EXTENDERS_FORWARD = 0,
                            SOLENOID_EXTENDERS_REVERSE = 1,
                            SOLENOID_HOOK_FORWARD = 2,
                            SOLENOID_HOOK_REVERSE = 3;

    //PCMs
    final static public int PCM_EXTENDERS = 0,
                            PCM_HOOK = 1;                        

    final static public boolean DEBUG_MODE = false;
    final static public WorkMode MOTOR_MODE = Motor.WorkMode.NORMAL_MODE;

}