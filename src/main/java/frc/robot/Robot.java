/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;


//WPILib
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Compressor;

//Internal
import frc.robot.hardware.*;
import frc.robot.hardware.Gamepad.Key;
import frc.robot.software.*;

public class Robot extends TimedRobot {

  //Shared
  public Gamepad gp1, gp2;
  public Compressor compressor;
  
  //Private
  boolean isLowSpeed = false,
                 isHookPowered = false,
                 isRollerPowered = false;

  @Override
  public void robotInit() {
    Chassis.init();
    gp1 = new Gamepad(0);
    gp2 = new Gamepad(1);

    Camera.initialize();


    compressor = new Compressor();
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

    updateBottom();
    updateTop();

  }

  public void updateBottom() {
    //Low speed mode
    if(gp1.isKeyToggled(Key.RB)) {
      isLowSpeed = !isLowSpeed;
      Chassis.setSpeedFactor(isLowSpeed? Statics.LOW_SPD : 1.0);
    }

    //Drive
    if(gp1.isKeysChanged(Key.J_LEFT_Y,Key.J_RIGHT_X)) {

      double y = Utils.mapAnalog(-gp1.getValue(Key.J_LEFT_Y));
      double x = Utils.mapAnalog(gp1.getValue(Key.J_RIGHT_X));

      Chassis.drive(y, x);
    }
  }

  public void updateTop() {

    if(gp1.isKeyToggled(Key.Y)) {
      Camera.changeCam();
    }

  }
  
  @Override
  public void testPeriodic() {
  }
}
