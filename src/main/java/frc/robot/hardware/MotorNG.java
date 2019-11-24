package frc.robot.hardware;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.*;

public class MotorNG {

    public enum Model {
        SPARK_MAX,
        FALCON_500,
        TALON_SRX
    }

    private WPI_TalonSRX srx;
    private CANSparkMax max;
    
    private double speed = 1.0;
    public static Model DEFAULT_MODEL = Model.TALON_SRX;
    private Model model = DEFAULT_MODEL;
    private boolean isReverse = false;

    public MotorNG(int port) {
        this(port, DEFAULT_MODEL);
    }

    public MotorNG(int port, Model model) {
        this(port, model, false);
    }

    public MotorNG(int port, boolean isReverse) {
        this(port, DEFAULT_MODEL, isReverse);
    }

    public MotorNG(int port, Model model, boolean isReverse) {

        this.model = model;

        if(model == Model.FALCON_500 || model == Model.TALON_SRX) {
            srx = new WPI_TalonSRX(port);
            setReverse(isReverse);
        }
        else {
            max = new CANSparkMax(port, MotorType.kBrushless);   
            setReverse(isReverse);
        }
    }

    public void setReverse(boolean isReverse) {
        this.isReverse = isReverse;
    
        if(model == Model.FALCON_500 || model == Model.TALON_SRX) {
            srx.setInverted(isReverse);
        }
        else {
            max.setInverted(isReverse);
        }

    }

    public boolean isReverse() {
        return isReverse;
    }

    public void flip() {
        setReverse(!isReverse);
    }

    public void setSpeed(double newSpeed) {
        speed = newSpeed;
    }

    public void move(double value) {

        if(model == Model.FALCON_500 || model == Model.TALON_SRX) {
            srx.set(value * speed);
        }
        else {
            max.set(value * speed);
        }
    }

    public void stop() {
        move(0);
    }

    public double getEncoderReading() {

        if(model == Model.FALCON_500 || model == Model.TALON_SRX) {
            return srx.getSelectedSensorPosition(0);
        }
        else {
            return max.getEncoder().getPosition();
        }
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