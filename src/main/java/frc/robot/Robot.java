/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

//WPILib
import edu.wpi.first.wpilibj.TimedRobot;

//Internal
import frc.robot.hardware.*;
import frc.robot.hardware.Gamepad.Key;
import frc.robot.software.*;

public class Robot extends TimedRobot {

  //Shared
  public static Gamepad gp1, gp2;
  
  //Private
  static boolean isLowSpeed = false;


  @Override
  public void robotInit() {
    Chassis.init();
    gp1 = new Gamepad(0);
    gp2 = new Gamepad(1);
  }
  @Override
  public void robotPeriodic() {
  }
  @Override
  public void autonomousInit() {
  }

  @Override
  public void autonomousPeriodic() {
    //TODO: ACTUALLY DO SOME KIND OF AUTON
    teleopPeriodic();
  }


  @Override
  public void teleopPeriodic() {
    
    gp1.fetchData();
    gp2.fetchData();

    updateChassis();


  }

  public void updateChassis() {
    //Low speed mode
    if(gp1.isKeyToggled(Key.Y)) {
      isLowSpeed = !isLowSpeed;
      Chassis.setSpeedFactor(isLowSpeed? Statics.LOW_SPD : 1.0);
    }

    //Reverse Mode
    if(gp1.isKeyToggled(Key.RB)) {
      Chassis.flip();
    }

    //Gearbox
    if(gp1.isKeyToggled(Key.LB)) {
      Chassis.shift();
    }

    //Drive
    if(gp1.isKeyChanged(Key.RT) || gp1.isKeyChanged(Key.LT) || gp1.isKeyChanged(Key.J_LEFT_X)) {

      double y = - Utils.mapAnalog(gp1.getValue(Key.RT),
                                  Statics.OFFSET_MIN,
                                  Statics.OFFSET_MAX)
                                  + Utils.mapAnalog(gp1.getValue(Key.LT), Statics.OFFSET_MIN, Statics.OFFSET_MAX);

      double x = Utils.mapAnalog(gp1.getValue(Key.J_LEFT_X), Statics.OFFSET_MIN, Statics.OFFSET_MAX);

      Chassis.drive(y, x);
    }


  }

  @Override
  public void testPeriodic() {
  }
}
