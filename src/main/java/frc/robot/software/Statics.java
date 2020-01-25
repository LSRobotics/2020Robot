package frc.robot.software;

public class Statics {

//TODO: Update this once the robot gets built

//Environment variables
    static final public boolean DEBUG_MODE = true;
    static final public double LOW_SPD = 0.6;

//Controller
    static final public double OFFSET_MIN = 0.1,
                               OFFSET_MAX = 0.7;

//Chassis

   static final public int CHASSIS_L1 = 4,
                            CHASSIS_L2 = 5,
                            CHASSIS_R1 = 6,
                            CHASSIS_R2 = 7;


//Other Motors
    static final public int SPARK_MAX_1 = 3,
                            SPARK_MAX_2 = 4,
                            FALCON      = 2,
                            FALCON_SHOOTER_UP = 9, //Needs change
                            FALCON_SHOOTER_DOWN = 10, //Needs change
                            SRX_LIFT = 8,
                            SRX_INTAKE = 11,
                            SRX_HOOK   = 7;

//Ultrasonic
    static final public int US_PING = 0,
                            US_ECHO = 1;
}