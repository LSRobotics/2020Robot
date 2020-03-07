package frc.robot.autonomous;

import frc.robot.hardware.*;
import frc.robot.software.*;

import java.util.ArrayList;

import frc.robot.hardware.Gamepad.Event;

public class AutonRunRecord extends AutonBase {
    
    ArrayList<Event> events;
    int totalEvents;
    int currentEvent = 0;
    boolean isInterrupted = false;
    Timer timer = new Timer();

    public AutonRunRecord(ArrayList<Event> events) {
        super();
        this.events = events;
        totalEvents = events.size();
    }

    public void preRun() {
        Chassis.stop();
        Gamepad.setEmulated(true);
    }

    public void duringRun() {
        if(totalEvents == 0) {
            return;
        }

        long time = events.get(currentEvent).time;

        var end = currentEvent;

        for(int i = currentEvent + 1; i < totalEvents - 1; ++i) {
            if(events.get(i).time > time) {
                break;
            }
            else {
                end = i;
            }
        }

        for(int i = currentEvent; i <= end; ++i) {
            var e = events.get(i);
         
            (e.isGp1? Core.robot.gp1 : Core.robot.gp2).override(e.key, e.value);
        }

        currentEvent = end;

        Core.robot.teleopPeriodic();

        if(currentEvent == totalEvents) {
            return;
        }
        else {
            var timeToSleep = events.get(end+1).time - events.get(end).time;

            Utils.sleep(timeToSleep);

            currentEvent += 1;
        }

    }

    public boolean isActionDone() {
        return currentEvent == totalEvents;
    }
    
    public void postRun() {
        Chassis.stop();
        Gamepad.setEmulated(false);
    }

    public String toString() {
        return "AutonRunRecord";
    }

}