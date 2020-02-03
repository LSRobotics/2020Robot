package frc.robot.hardware;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.SpeedController;

public class MotorNG {

    public enum Model {
        SPARK_MAX,
        FALCON_500,
        TALON_SRX,
        VICTOR_SPX;
    }

    private SpeedController phoenix;
    private WPI_TalonFX falcon;
    private CANSparkMax max;
    
    private double speed = 1.0;
    private double lastPower = 0;
    public static Model DEFAULT_MODEL = Model.FALCON_500;
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

        switch(model) {
            case FALCON_500:
                falcon = new WPI_TalonFX(port);
                break;
            case VICTOR_SPX:
                phoenix = new WPI_VictorSPX(port);
                break;
            case TALON_SRX:
                phoenix = new WPI_TalonSRX(port);
                break;
            case SPARK_MAX:
                max = new CANSparkMax(port,MotorType.kBrushless);
                max.getEncoder();
                break;
        }
        setReverse(isReverse);
    }

    public void setReverse(boolean isReverse) {
        this.isReverse = isReverse;
    
        if(model == Model.FALCON_500) {
            falcon.setInverted(isReverse);
        }
        else if(model == Model.TALON_SRX || model ==Model.VICTOR_SPX) {
            phoenix.setInverted(isReverse);
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

    public void moveRaw(double value) {
        if (model == Model.FALCON_500) {
            falcon.set(value);
        }
        else if(model == Model.TALON_SRX || model == Model.VICTOR_SPX) {
            phoenix.set(value);
        }
        else {
            max.set(value);
        }
    }

    public void move(double value) {

        if(value * speed == lastPower) return;

        lastPower = value * speed;

        moveRaw(lastPower);
    }

    public void stop() {
        move(0);
    }

    public double getCurrentPower() {
        return lastPower;
    }

    public double getEncoderReading() {

        if(model == Model.FALCON_500) {
            return falcon.getSelectedSensorPosition(0);
        }
        else if(model == Model.SPARK_MAX) {
            return max.getEncoder().getPosition();
        }
        return 0;  
    }

    public double getVelocity() {
        switch(model) {
            case SPARK_MAX:
            case TALON_SRX:
            case VICTOR_SPX:
                break;
            case FALCON_500:
                return falcon.getSelectedSensorVelocity();
        }

        return 0;
    }

    public void move(boolean forward, boolean reverse) {

        if (forward == reverse)
            stop();
        else if (forward)
            move(1);
        else
            move(-1);
    }
}