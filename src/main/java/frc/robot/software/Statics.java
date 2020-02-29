package frc.robot.software;

import frc.robot.autonomous.*;
import frc.robot.hardware.Chassis;

public class Statics {

//TODO: Update this once the robot gets built

//Environment variables
    static final public boolean DEBUG_MODE = true;
    static final public double LOW_SPD = 0.6;

//Controller
    static final public double OFFSET_MIN = 0.1,
                               OFFSET_MAX = 0.7;

//Chassis
   static final public int CHASSIS_L1 = 1,
                            CHASSIS_L2 = 2,
                            CHASSIS_R1 = 3,
                            CHASSIS_R2 = 4;

//Motors
    static final public int INDEX_1 = 5,
                            INDEX_2 = 6,
                            INDEX_3 = 7,
                            INTAKE  = 8,
                            SHOOTER = 9,
                            FEEDER  = 10,
                            CLIMB_ROLLER = 11;

//Solenoids
    static final public int MASTER_PCM = 0,
                            ARM_FORWARD = 0,
                            ARM_REVERSE = 1,
                            CLIMB_FORWARD = 2,
                            CLIMB_REVERSE = 3,
                            CHASSIS_FORWARD = 4,
                            CHASSIS_REVERSE = 5;

	public static final int US_ALIGNER_S_PING = 0;
	public static final int US_ALIGNER_F_PING = 1;
	public static final int US_ALIGNER_F_ECHO = 2;
    public static final int US_ALIGNER_S_ECHO = 3;

//Intake Ultrasonic
    public static final int US_INTAKE_PING = 4,
                            US_INTAKE_ECHO = 5;

//Color
    static final public double[] TAPE_RED = {0.47,0.37,0.16},
                                TAPE_BLUE = {0.21,0.42,0.36},
                                TAPE_WHITE = {0.257,.465,.278},
                                CONTROLPANEL_YELLOW = {.324,.555,.121},
                                CONTROLPANEL_RED = {.524,.346,.130},
                                CONTROLPANEL_GREEN = {.170,.579,.251},
                                CONTROLPANEL_BLUE = {.125,.428,.447};

//Analog

    static final public int PIXY_CAM = 0;
    static final public int IR = 1; //TODO: Change this
                                               
    static final public double FALCON_UNITS_PER_INCH = 1286.455191;
    static final public double FALCON_UNITS_PER_DEGREE = 336.666667;

//Init in Core
    public static AutonGroup autonToCenter = new AutonGroup(new AutonGyroTurn(90),
                                new AutonSleep(250),
                                new AutonRSMove(Chassis.sensorIR, 94.125),
                                new AutonSleep(250),
                                new AutonGyroTurn(0),
                                new AutonSleep(250),
                                new AutonRSMove(Chassis.sensorIR, 153.875),
                                new AutonPixyAlign(0));

//Other Constants
    static final public double CLIMB_MOTOR_TRAVEL_DISTANCE = 200; //FIXME: Update this after getting data from build team

    public final static int Light_PWM_Port = 0;

}