package frc.robot.hardware;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.*;

public class Motor {

    public enum Model {
        
        //Phoenix
        PWM_TALON_SRX, 
        CAN_TALON_SRX, 
        
        //REV Robotics
        CAN_SPARK_MAX,
        PWM_SPARK,
        
        //Vex
        PWM_VICTOR,
        PWM_VICTOR_SP
    }

    private SpeedController motor;
    private double speed = 1.0;
    public static Model DEFAULT_MODEL = Model.CAN_TALON_SRX;
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
        case PWM_TALON_SRX:
            motor = new Talon(port);
            break;
        case PWM_VICTOR:
            motor = new Victor(port);
            break;
        case PWM_VICTOR_SP:
            motor = new VictorSP(port);
            break;
        case PWM_SPARK:
            motor = new Spark(port);
            break;
        case CAN_TALON_SRX:
            motor = new WPI_TalonSRX(port);
        case CAN_SPARK_MAX:
            motor = new CANSparkMax(port,MotorType.kBrushless);
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

    public void move(double value) {
        motor.set(value * speed);
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