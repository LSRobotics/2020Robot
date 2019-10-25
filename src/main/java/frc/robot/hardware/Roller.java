package frc.robot.hardware;

import edu.wpi.first.wpilibj.Ultrasonic;
import frc.robot.software.*;

public class Roller {
    
    public enum Mode {
        IDLE,
        INTAKE,
        INTAKE_DELAY,
        OUTAKE
    }

    private static Motor low, high;
    private static Ultrasonic sensor;
    private static boolean isBusy = false;
    private static Mode mode = Mode.IDLE;
    private static Timer t = new Timer("Roller");
    private static boolean isMovingChange = false; //Edge Case


    public static void initialize() {
        //See I reversed it here
        low = new Motor(Statics.ROLLER_LOW);
        high = new Motor(Statics.ROLLER_HIGH,true);
        sensor = new Ultrasonic(Statics.ULTRASONIC_PING, Statics.ULTRASONIC_ECHO,Ultrasonic.Unit.kInches);
        sensor.setAutomaticMode(true);
    }

    //Set working mode, call this method when new input from human driver is fed.
    public static void setMode(Mode newMode) {

        //Edge case, driver would still make dumb mistakes like this and I have to deal with it in code :(
        if(mode != Mode.IDLE && newMode != Mode.IDLE) {
            isMovingChange = true;
        }

        mode = newMode;
    }

    //Internal method that frees the mechanism for future use
    private static void free() {
        t.stop();
        isBusy = false;
        low.stop();
        high.stop();
        mode = Mode.IDLE;
    }

    //Update Roller movements, call this method in every period
    public static void update() {
        if(!isBusy || isMovingChange) {

            isMovingChange = false;
            
            if(mode == Mode.INTAKE || mode == Mode.OUTAKE) {

                t.start();
                isBusy = true;

                //If human driver attempts to run intake when balls are in, ultrasonic sensor can stop it.
                if(mode == Mode.INTAKE && sensor.getRangeInches() > 10) {
                    low.move(0.2);
                    high.move(0.2);
                }
                else if(mode == Mode.OUTAKE) {
                    low.move(false, true);
                    high.move(false, true);
                }
            }
        }
        else {

            //Ultrasonic Sensor Delay for intake
            if(mode == Mode.INTAKE && sensor.getRangeInches() < 10) {
                t.start();
                mode = Mode.INTAKE_DELAY;
            }
            else if(mode == Mode.INTAKE_DELAY && t.getElaspedTimeInMs() > 200) {
                free();
            }
            //Timeout STOP for outake
            else if(t.getElaspedTimeInMs() > 3000 && mode == Mode.OUTAKE) {
                free();
            }
            //Manual STOP
            else if(mode == Mode.IDLE) {
                free();
            }
        }
    }
}