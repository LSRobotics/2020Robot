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
import frc.robot.constants.SpeedCurve;
//Internal
import frc.robot.hardware.*;
import frc.robot.hardware.Gamepad.Key;
import frc.robot.hardware.MotorNG.Model;
import frc.robot.software.*;

public class Robot extends TimedRobot {

  //Shared (Make sure these are "public" so that Core can take them in, which allows global access to happen)
  public Gamepad gp1, gp2;
  public MotorNG highShooterUp, highShooterDown;
  public Compressor compressor;
  public double highShooterSpeed = 1.0;
  public final double SPD_TWEAK_INTERVAL = 0.2;

  //Private
  boolean isLowSpeed = false,
                 isHookPowered = false,
                 isRollerPowered = false;

  @Override
  public void robotInit() {

    Core.initialize(this);

    Chassis.initialize();
    gp1 = new Gamepad(0);
    gp2 = new Gamepad(1);

    Camera.initialize();

    compressor = new Compressor();
  
    highShooterUp = new MotorNG(Statics.HIGH_SHOOTER_UPPER, Model.FALCON_500,true);
    highShooterDown = new MotorNG(Statics.HIGH_SHOOTER_LOWER, Model.FALCON_500);
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
    if(gp1.isKeysChanged(Key.RT,Key.LT,Key.J_RIGHT_X)) {

      double y = gp1.getValue(Key.RT) - gp1.getValue(Key.LT);
      double x = Utils.getCurvedValue(SpeedCurve.SQUARED, Utils.mapAnalog(gp1.getValue(Key.J_RIGHT_X)));

      Chassis.drive(y, x);
    }
  }

  public void updateTop() {

    //Shooter Speed Ajust (For tweaking)
    if(gp2.isKeyToggled(Key.LB)) {
      if(highShooterSpeed - SPD_TWEAK_INTERVAL >= 0) {
        highShooterSpeed -= SPD_TWEAK_INTERVAL;

        highShooterUp.setSpeed(highShooterSpeed);
        highShooterDown.setSpeed(highShooterSpeed);

        Utils.report("New Motor Speed: " + highShooterSpeed);
      }
    }

      if(gp2.isKeyToggled(Key.RB)) {
        if(highShooterSpeed + SPD_TWEAK_INTERVAL <= 1) {
          highShooterSpeed += SPD_TWEAK_INTERVAL;
  
          highShooterUp.setSpeed(highShooterSpeed);
          highShooterDown.setSpeed(highShooterSpeed);
  
          Utils.report("New Motor Speed: " + highShooterSpeed);
        }   
    }

    //Shooter Actuation
    if(gp2.isKeyChanged(Key.A)) {
      highShooterUp.move(gp2.isKeyHeld(Key.A), false);
      highShooterDown.move(gp2.isKeyHeld(Key.A), false);
    }

  }
  
  @Override
  public void testPeriodic() {
  }
}
