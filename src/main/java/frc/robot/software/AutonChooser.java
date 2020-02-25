package frc.robot.software;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import frc.robot.hardware.*;
import frc.robot.autonomous.*;

/**
 * Created by TylerLiu on 2017/03/04.
 */
public class AutonChooser {

    public static SendableChooser<AutonGroup> chooser;

    public static void initialize() {
        
        //TODO: Add stuff to here
        
        chooser = new SendableChooser<>();

        chooser.setDefaultOption("Default Auton", new AutonGroup(new AutonRSMove(Chassis.sensorIR, 90),
                                                                 new AutonPixyAlign(0),
                                                                 new AutonBall()
                                                                 ));
        chooser.addOption("Turn to Goal and shoot", new AutonGroup(new AutonPixyAlign(0),
                                                   //shooter
                                                    new AutonBall(),
                                                   //drive off line (Back off by 30 inches maybe?)
                                                    new AutonEncoderForward(-30)
                                                   ));
        chooser.addOption("Drive to goal from left", new AutonGroup(new AutonGyroTurn(90),
                                                   new AutonRSMove(Chassis.sensorIR, 94.66 - 15), //subtract to center of bot
                                                   new AutonGyroTurn(0),
                                                   new AutonPixyAlign(0),
                                                   //shooter
                                                   new AutonBall(),
                                                   //drive off line
                                                   new AutonEncoderForward(-30)
                                                   ));
        chooser.addOption("Drive to goal from right", new AutonGroup(new AutonGyroTurn(-90),
                                                   new AutonRSMove(Chassis.sensorIR, 219.18), //subtract to center of bot
                                                   new AutonGyroTurn(0),
                                                   new AutonPixyAlign(0),
                                                   //shooter
                                                   new AutonBall(),
                                                   //drive off line
                                                   new AutonEncoderForward(-30)
                                                   ));
        chooser.addOption("Pick up 5 balls in our trench", new AutonGroup(new AutonPixyAlign(0),
                                                   //shoot
                                                   new AutonBall(),
                                                   new AutonGyroTurn(180),
                                                   //encoders or back distance sensor to drive forward and pick up five balls
                                                   new AutonEncoderForward(150,true), //TODO: Change the actual distance of this
                                                   //might need to turn to pick up last 2 balls

                                                   //FIXME: Meh...Think about the robustness of our robot

                                                   //encoders or back distance sensor to drive backwards (or stop using initiation line/trench line)
                                                   new AutonGyroTurn(0),
                                                   
                                                   //Drive back a little bit
                                                   new AutonEncoderForward(20),
                                                   new AutonPixyAlign(0),
                                                   //shooter
                                                   new AutonBall()
                                                   //drive off line
                                                   ));    
        chooser.addOption("Auton5", new AutonGroup(//Finish later
                                                   ));
        SmartDashboard.putData(chooser);
    }

    public static AutonGroup getSelected() {
        return chooser.getSelected();
    }
}