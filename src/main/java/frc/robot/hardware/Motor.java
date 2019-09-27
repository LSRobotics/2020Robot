package frc.robot.hardware;

import edu.wpi.first.wpilibj.*;

public class Motor {

    public enum Model {
        TALON_SRX, VICTOR_SP, SPARK, VICTOR;
    }

    public enum WorkMode {
        NORMAL_MODE,
        SAFE_MODE,
        SELF_DESTRUCT_MODE
    }

    private PWMSpeedController motor;
    private double speed = 1.0;
    public static Model DEFAULT_MODEL = Model.VICTOR_SP;
    private static WorkMode wMode = WorkMode.NORMAL_MODE;
    private boolean isReverse = false;

    public Motor(int port) {
        this(port, DEFAULT_MODEL);
    }

    public Motor(int port, Model model) {
        this(port, model, false);
    }

    public Motor(int port, boolean isReverse) {
        this(port, DEFAULT_MODEL, isReverse);
    }

    public Motor(int port, Model model, boolean isReverse) {
        switch (model) {
        case TALON_SRX:
            motor = new Talon(port);
            break;
        case VICTOR:
            motor = new Victor(port);
            break;
        case VICTOR_SP:
            motor = new VictorSP(port);
            break;
        case SPARK:
            motor = new Spark(port);
        default:
            break;
        }
        motor.setInverted(isReverse);
    }

    public void setReverse(boolean isReverse) {
        this.isReverse = isReverse;
        motor.setInverted(isReverse);
    }

    public boolean isReverse() {
        return isReverse;
    }

    public void flip() {
        motor.setInverted(!isReverse);
    }

    public void setSpeed(double newSpeed) {
        speed = newSpeed;
    }

    public static void setMode(WorkMode workMode) {
        wMode = workMode;
    }

    public void move(double value) {

        switch (wMode) {
        case NORMAL_MODE:
            motor.set(value * speed);
            break;
        case SAFE_MODE:
            break;
        case SELF_DESTRUCT_MODE:
            motor.set(1);
        }

    }

    public void stop() {
        move(0);
    }

    public void move(boolean forward, boolean reverse) {

        if (forward == reverse)
            move(0);
        else if (forward)
            move(1);
        else
            move(-1);
    }
}