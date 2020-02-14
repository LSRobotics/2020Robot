package frc.robot.autonomous;

import java.util.ArrayList;

public class AutonGroup {
    
    private ArrayList<AutonBase> actions;

    public AutonGroup(AutonBase... actions) {
        for(AutonBase i : actions) {
            this.actions.add(i);
        }
    }

    public boolean run() {
        for(AutonBase i : actions) {
            if(!i.run()) {
                return false;
            }
        }
        return true;
    }

}