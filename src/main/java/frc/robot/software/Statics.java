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
                            CHASSIS_R2 = 7,
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
                        
}