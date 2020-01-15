/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

//WPILib
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Compressor;
//Internal
import frc.robot.hardware.*;
import frc.robot.hardware.Gamepad.Key;
import frc.robot.hardware.MotorNG.Model;
import frc.robot.software.*;

public class Robot extends TimedRobot {

  //Shared (Make sure these are "public" so that Core can take them in, which allows global access to happen)
  public Gamepad gp1,gp2;
  public MotorNG sparkMax1, sparkMax2, falcon;
  public Compressor compressor;
  public double motorSpeed = 1.0;
  public final double SPD_TWEAK_INTERVAL = 0.2;
  public boolean isFirstSparkMax = true;

  //Private
  boolean isLowSpeed = false,
                 isHookPowered = false,
                 isRollerPowered = false;

  @Override
  public void robotInit() {

    Core.initialize(this);

    //Chassis.initialize();
    gp1 = new Gamepad(0);
    //gp2 = new Gamepad(1);

    Camera.initialize();

    //compressor = new Compressor();
  
    sparkMax1 = new MotorNG(Statics.SPARK_MAX_1, Model.SPARK_MAX);
    sparkMax2 = new MotorNG(Statics.SPARK_MAX_2, Model.SPARK_MAX,true);
    falcon    = new MotorNG(Statics.FALCON, Model.FALCON_500);
    //highShooterDown = new MotorNG(Statics.HIGH_SHOOTER_LOWER, Model.SPARK_MAX);
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

    updateTop();

    postData();

  }

  public void updateBottom() {
    //TODO: FILL THIS PART OUT
  }

  public void updateTop() {


    //Toggle SparkMax Motors
    if(gp1.isKeyToggled(Key.Y)) {
        isFirstSparkMax = !isFirstSparkMax;
    }

    //SparkMax Speed Ajust (For tweaking)
    if(gp1.isKeyToggled(Key.LB)) {
      if(motorSpeed - SPD_TWEAK_INTERVAL >= 0) {
        motorSpeed -= SPD_TWEAK_INTERVAL;

        sparkMax1.setSpeed(motorSpeed);
        sparkMax2.setSpeed(motorSpeed);

        Utils.report("New Motor Speed: " + motorSpeed);
      }
    }

      if(gp1.isKeyToggled(Key.RB)) {
        if(motorSpeed + SPD_TWEAK_INTERVAL <= 1) {
          motorSpeed += SPD_TWEAK_INTERVAL;
  
          sparkMax1.setSpeed(motorSpeed);
          sparkMax2.setSpeed(motorSpeed);
         
          Utils.report("New Motor Speed: " + motorSpeed);
        }   
    }

    //SparkMax Actuation
    if(gp1.isKeyChanged(Key.A)) {


      (isFirstSparkMax?sparkMax1 : sparkMax2).move(gp1.isKeyHeld(Key.A), false);

    }

    //Falcon Actuation
    if(gp1.isKeysChanged(Key.LT,Key.RT)) {
      double speed = gp1.getValue(Key.RT) - gp1.getValue(Key.LT);
      falcon.move(speed);
    }

  }
  
  public void postData() {
    SmartDashboard.putNumber("SparkMax Speed Level (Fixed)", motorSpeed);
    SmartDashboard.putNumber("SparkMax ABS Power", (isFirstSparkMax? sparkMax1 : sparkMax2).getCurrentPower());
    SmartDashboard.putNumber("Falcon Power", falcon.getCurrentPower());
  }

  @Override
  public void testPeriodic() {
  }
}
