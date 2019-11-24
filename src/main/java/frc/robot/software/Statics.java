package frc.robot.software;

public class Statics {

//TODO: Update this once the robot gets built

//Environment variables
    static final public boolean DEBUG_MODE = false;
    static final public double LOW_SPD = 0.6;

//Controller
    static final public double OFFSET_MIN = 0.1,
                               OFFSET_MAX = 0.7;

//Chassis
    static final public int CHASSIS_L1 = 3,
                            CHASSIS_L2 = 7,
                            CHASSIS_L3 = 8,
                            CHASSIS_R1 = 0,
                            CHASSIS_R2 = 4,
                            CHASSIS_R3 = 5;

//Ultrasonic
    static final public int ULTRASONIC_PING = 1,
                            ULTRASONIC_ECHO = 0;

//Other PWM Devices

    //Intake Roller
    static final public int ROLLER_LOW = 2,
                            ROLLER_HIGH = 1,
                            HOOK = 6;
}