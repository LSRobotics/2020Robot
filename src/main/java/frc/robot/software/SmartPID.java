package frc.robot.software;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.controller.PIDController;

public class SmartPID extends PIDController {

    ArrayList<Double> history = new ArrayList<Double>();
    Timer timer = new Timer("SmartPID Timer");
    double oscilateCutOff = 0.1;
    boolean isActionDone = false;

    public SmartPID(double Kp, double Ki, double Kd) {
        super(Kp,Ki,Kd);
        timer.start();
    }

    public double next(double reading) {
        
        double result = calculate(reading);

        if(timer.getElaspedTimeInMs() > 100) {
            history.add(result);
            timer.start();
        }

        if(history.size() > 4 && history.size() % 5 == 0) {

            double max = 0;

            for(int i = history.size() - 5; i < history.size(); ++i) {
                double val = Math.abs(history.get(i));

                if(val > max) {
                    max = val;
                }
            }

            if(max < 0.1) {
                Utils.report("Detected, Current Value:" + result);
                isActionDone = true;
            }
        }

        return result;

    }

    public void setCutOff(double oscilateCutOff) {
        this.oscilateCutOff = Math.abs(oscilateCutOff);
    }

    public boolean isActionDone() {
        return isActionDone;
    }
}