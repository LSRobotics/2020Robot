package frc.robot.hardware;

import java.util.ArrayList;

import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;
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

    public TalonFXConfiguration config = new TalonFXConfiguration();
    public SpeedController phoenix;
    public WPI_TalonFX falcon;
    public CANSparkMax max;
    
    private double speed = 1.0;
    private double lastPower = 0;
    final public static Model DEFAULT_MODEL = Model.FALCON_500;
    private Model model = DEFAULT_MODEL;
    private boolean isReverse = false,
                    isSimulated = false;
    private ArrayList<MotorNG> slaves = new ArrayList<>();

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
                falcon.setSelectedSensorPosition(0);
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

    public void addSlave(MotorNG slave) {
        slaves.add(slave);
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

    public void setCurrentLimit(double amps) {
        if(model == Model.FALCON_500) {
            config.statorCurrLimit.currentLimit = 70;
            config.statorCurrLimit.enable = true;
            updateSettings();
        }
    }

    public void setMaxAcceleration(int unitsPerSec) {
        if(model == Model.FALCON_500) {
        falcon.configMotionAcceleration(unitsPerSec);
        }
    }

    private void updateSettings() {
        if(model == Model.FALCON_500) {
            falcon.configAllSettings(config);
        }
    }

    public boolean isReverse() {
        return isReverse;
    }

    public void flip() {
        setReverse(!isReverse);
    }

    public synchronized void setSpeed(double newSpeed) {
        speed = newSpeed;
    }


    public synchronized void moveRaw(double value) {
        moveRaw(value,false);
    }

    public synchronized void moveRaw(double value, boolean isFromMove) {

        if(!isFromMove) {
            lastPower = value;
        }

        if(isSimulated) {
            lastPower = value;
            return;
        }

        if (model == Model.FALCON_500) {
            falcon.set(value);
        }
        else if(model == Model.TALON_SRX || model == Model.VICTOR_SPX) {
            phoenix.set(value);
        }
        else {
            max.set(value);
        }

        if(!isFromMove) {
            if(slaves.size() > 0) {
                for(MotorNG slave : slaves) {
                    slave.moveRaw(value);
                }
            }
        }
    }

    public synchronized void move(double value) {

        if(value * speed == lastPower) return;

        lastPower = value * speed;

        moveRaw(lastPower,true);

        //Slaves are served last (Of course!)
        if(slaves.size() > 0) {
            for(MotorNG slave : slaves) {
                slave.move(value);
            }
        }
    }

    /*
    public synchronized void moveBySpeed(double speed) {
        if(model == Model.FALCON_500) {
            falcon.set(ControlMode.Velocity,speed);
        }
        else {
            Utils.report("You tried to call moveBySpeed() but it is not an falcon 500. Motor would not run.");
        }
    }
    */

    public synchronized void stop() {
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

    public void setEmulated(boolean isEmulated) {
        this.isSimulated = isEmulated;
    }
}