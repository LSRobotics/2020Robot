package frc.robot.autonomous;

import java.util.ArrayList;

import frc.robot.software.Utils;

public class AutonGroup {
    
    private ArrayList<AutonBase> actions = new ArrayList<AutonBase>();

    public AutonGroup(AutonBase... actions) {
        for(AutonBase i : actions) {
            this.actions.add(i);
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