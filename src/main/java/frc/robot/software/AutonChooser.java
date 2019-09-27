package frc.robot.software;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.software.AutonSensorBasedA.Position;

/**
 * Created by TylerLiu on 2017/03/04.
 */
public class AutonChooser {

    public static SendableChooser<Runnable> chooser;

    public static void init() {
        chooser = new SendableChooser<>();

        // Add options here

        SmartDashboard.putData("Auton Chooser", chooser);
        chooser.setDefaultOption("near CARGO(Default)", new AutonSensorBasedA(Position.CARGO_NEAR));
        chooser.addOption("middle CARGO", new AutonSensorBasedA(Position.CARGO_MID));
        chooser.addOption("right CARGO", new AutonSensorBasedA(Position.CARGO_FAR));
        chooser.addOption("Hab", new AutonSensorBasedA(Position.HAB));
        chooser.addOption("Manual", () -> {return;});
        
    }

    public static Runnable getSelected() {
        return chooser.getSelected();
    }
}