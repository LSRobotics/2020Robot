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
   static final public int CHASSIS_L1 = 7,
                            CHASSIS_L2 = 8,
                            CHASSIS_R1 = 9,
                            CHASSIS_R2 = 10,
                            SHIFTER_PCM = 1,
                            SHIFTER_F = 0,
                            SHIFTER_R = 1;

//Motors
    static final public int INDEX_1 = 0,
                            INDEX_2 = 2,
                            INDEX_3 = 3,
                            INTAKE  = 4,
                            SHOOTER = 5,
                            FEEDER  = 6;

    static final public int ARM_PCM = 0,
                            ARM_FORWARD = 0,
                            ARM_REVERSE = 1;
	public static final int US_ALIGNER_S_PING = 0;
	public static final int US_ALIGNER_F_PING = 1;
	public static final int US_ALIGNER_F_ECHO = 2;
    public static final int US_ALIGNER_S_ECHO = 3;

//Intake Ultrasonic
    public static final int US_INTAKE_PING = 2,
                            US_INTAKE_ECHO = 3;

//Color
    static final public double[] TAPE_RED = {0.47,0.37,0.16},
                                 TAPE_BLUE = {0.21,0.42,0.36};
                        
                        
}