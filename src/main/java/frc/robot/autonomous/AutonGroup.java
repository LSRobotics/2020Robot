package frc.robot.autonomous;

import java.util.ArrayList;

import frc.robot.software.Utils;

public class AutonGroup {
    
    private ArrayList<AutonBase> actions = new ArrayList<AutonBase>();

    public AutonGroup(AutonBase... actions) {
        this(50, actions);
    }

    public AutonGroup(double sleepTimeInMs, AutonBase... actions) {
        for(int i = 0; i < actions.length; ++i) {
            this.actions.add(actions[i]);

            //Auto Sleep
            if(sleepTimeInMs != 0 && i < actions.length - 1) {
                this.actions.add(new AutonSleep(sleepTimeInMs));
            }

        }
    }

    public boolean run() {
        int counter = 0;
        for(AutonBase i : actions) {
            counter ++;
            Utils.report("Running Action " + counter + ": " + i.toString());
            if(!i.run()) {
                //Utils.report("Interrupted.");
                return false;
            }
        }
        return true;
    }

}