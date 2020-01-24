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
                            FALCON_SHOOTER_UP = 5, //Needs change
                            FALCON_SHOOTER_DOWN = 6; //Needs change

//Ultrasonic
    static final public int US_PING = 0,
                            US_ECHO = 1;

//TODO: UPDATE THIS
    static final public int US_ALIGNER_F_PING  = 2,
                            US_ALIGNER_F_ECHO  = 3,
                            US_ALIGNER_B_PING  = 4,
                            US_ALIGNER_B_ECHO  = 5;

//Shifting Gearbox
    static final public int SHIFTER_PCM = 0,
                            SHIFTER_F = 0,
                            SHIFTER_R = 1;

//TODO: Update this
    static final public int TIME_PER_360 = 1800;
}