package frc.robot.hardware;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Ultrasonic;

public class RangeSensor {

    public enum Type {
        ANALOG_US_MAXBOTIX,
        DIO_US_HC_SR04,
        ANALOG_IR_GP2Y0A710K0F,
        PIXY_CAM,
        ANALOG_RAW
    }

    private boolean isInitialized = false;
    private Ultrasonic dio;
    public AnalogInput analog;
    private Type type;


    public RangeSensor(int port1, int port2, Type type) {

        this.type = type;

        switch(type) {
            case DIO_US_HC_SR04:
                dio = new Ultrasonic(port1,port2,Ultrasonic.Unit.kInches);
                break;
            case ANALOG_US_MAXBOTIX:
            case ANALOG_IR_GP2Y0A710K0F:
            case PIXY_CAM:
            case ANALOG_RAW:
                analog = new AnalogInput(port1);
                break;
        }
    }

    public RangeSensor(int port1, Type type) {
        this(port1, 0, type);
    }

    public double getRangeInches() {

        if(!isInitialized && type == Type.DIO_US_HC_SR04) {
            isInitialized = true;
            dio.setAutomaticMode(true);
        }

        double voltage = 0;

        if(type != Type.DIO_US_HC_SR04) {
            voltage = analog.getAverageVoltage();
        }

        switch(type) {
            case ANALOG_US_MAXBOTIX:
                return 
                57.793*voltage + 2.2858;
                //0.2886*Math.pow(voltage, 4)-2.9291*Math.pow(voltage, 3)+11.575*Math.pow(voltage, 2)+21.765*voltage+12.541;
                //((analog.getValue())*.125);
                //1.7503*Math.pow(voltage, 2)+34.221*voltage+7.6138;
            case DIO_US_HC_SR04:
                return dio.getRangeInches();
            case ANALOG_IR_GP2Y0A710K0F:
                return 
                //430.75 * Math.pow(voltage, -3.7031) + 23.645; //Stole from FRC 2017 haha
                89.217*Math.pow(voltage, 4)-922.91*Math.pow(voltage, 3)+3522.2*Math.pow(voltage, 2)-5925.3*voltage+3788.1; //good!!!
                //371.67*Math.pow(voltage, 2)-1502.3*voltage+1583.4;
            case PIXY_CAM:
                return (voltage / 3.3 * 2) - 1;
            case ANALOG_RAW:
                return voltage;
            default: return 0;
        }
    }
}