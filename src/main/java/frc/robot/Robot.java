/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

//WPILib
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.revrobotics.ColorSensorV3;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.I2C;
//Internal
import frc.robot.hardware.*;
import frc.robot.hardware.Gamepad.Key;
import frc.robot.hardware.MotorNG.Model;
import frc.robot.software.*;

public class Robot extends TimedRobot {

  //Shared (Make sure these are "public" so that Core can take them in, which allows global access to happen)
  public Gamepad gp1,gp2;
  public MotorNG sparkMax1, sparkMax2, falcon,shooterUp,shooterDown,srxIntake, srxLift;
  public ColorSensorV3 colorSensor;
  public Ultrasonic us;
  public Compressor compressor;
  public double motorSpeed = 1.0,
                shooterSpeed = 1.0;
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
  
    sparkMax1 = new MotorNG(Statics.SPARK_MAX_1, Model.SPARK_MAX,true);
    sparkMax2 = new MotorNG(Statics.SPARK_MAX_2, Model.SPARK_MAX,true);
    falcon    = new MotorNG(Statics.FALCON, Model.FALCON_500);
    shooterUp = new MotorNG(Statics.FALCON_SHOOTER_UP, Model.FALCON_500);
    shooterDown = new MotorNG(Statics.FALCON_SHOOTER_DOWN, Model.FALCON_500);
    srxIntake = new MotorNG(Statics.SRX_INTAKE, Model.TALON_SRX);
    srxLift   = new MotorNG(Statics.SRX_LIFT, Model.TALON_SRX);
    srxLift.setSpeed(0.3);

    colorSensor = new ColorSensorV3(I2C.Port.kOnboard);

    us = new Ultrasonic(Statics.US_PING, Statics.US_ECHO, Ultrasonic.Unit.kMillimeters);
    us.setAutomaticMode(true);
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

    updateTestMotors();

    updateBottom();
    updateTop();

    postData();

  }


  
  public void updateBottom() {
    //TODO: FILL THIS PART OUT
  }




  public void updateTop() {
    updateShooters();
    updateIntake();
  }

  private void updateTestMotors() {
    
    boolean isSpeedChanged = false;
/*
    //Toggle SparkMax Motors
    if(gp1.isKeyToggled(Key.Y)) {
      isFirstSparkMax = !isFirstSparkMax;
  }
  */

  //SparkMax Speed Ajust (For tweaking)
  if(gp1.isKeyToggled(Key.LB)) {
    if(motorSpeed - SPD_TWEAK_INTERVAL >= 0) {
      motorSpeed -= SPD_TWEAK_INTERVAL;

      isSpeedChanged = true;
    }
  }

    if(gp1.isKeyToggled(Key.RB)) {
      if(motorSpeed + SPD_TWEAK_INTERVAL <= 1) {
        motorSpeed += SPD_TWEAK_INTERVAL;

        isSpeedChanged = true;
      }   
  }

  if(isSpeedChanged) {
    sparkMax1.setSpeed(motorSpeed);
    sparkMax2.setSpeed(motorSpeed);

    if(sparkMax1.getCurrentPower() > 0) sparkMax1.move(true,false);
    
    if(sparkMax2.getCurrentPower() > 0) sparkMax2.move(true,false);

    Utils.report("New Motor Speed: " + motorSpeed);
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


  public void updateShooters() {

    boolean isSpeedChanged = false;

    //Shooter Speed Ajust (For tweaking)

    if(gp1.isKeyToggled(Key.DPAD_LEFT)) {

      if(shooterSpeed - SPD_TWEAK_INTERVAL >= 0) {
        shooterSpeed -= SPD_TWEAK_INTERVAL;

        isSpeedChanged = true;
      }
    }
    else if(gp1.isKeyToggled(Key.DPAD_RIGHT)) {
        if(motorSpeed + SPD_TWEAK_INTERVAL <= 1) {
          shooterSpeed += SPD_TWEAK_INTERVAL;
  
          isSpeedChanged = true;
        }   
    }

    if(isSpeedChanged) {
      shooterUp.setSpeed(motorSpeed);
      shooterDown.setSpeed(motorSpeed);
    
      if(shooterUp.getCurrentPower() > 0) {
        shooterUp.move(true,false);
        shooterDown.move(true,false);
      }

      Utils.report("New Shooter Speed: " + shooterSpeed);
    }

    //Shooter Actuation
    if(gp1.isKeyChanged(Key.B)) {
      shooterUp.move(gp1.isKeyHeld(Key.B),false);
      shooterDown.move(gp1.isKeyHeld(Key.B),false);
    }
  }

  public void updateIntake() {
    srxIntake.move(gp1.isKeyHeld(Key.X),false);
    srxLift.move(gp1.isKeyHeld(Key.DPAD_UP),gp1.isKeyHeld(Key.DPAD_DOWN));
  }
  
  public void postData() {
    SmartDashboard.putNumber("SparkMax Speed Level (Fixed)", motorSpeed);
    SmartDashboard.putNumber("SparkMax Power", (isFirstSparkMax? sparkMax1 : sparkMax2).getCurrentPower());
    SmartDashboard.putNumber("Falcon Power", falcon.getCurrentPower());
    SmartDashboard.putNumber("Shooter speed level", shooterSpeed);
    SmartDashboard.putNumber("Ultrasonic",us.getRangeMM());
    SmartDashboard.putString("Color Sensor (R,G,B)",colorSensor.getRed() + ", " + colorSensor.getGreen() + ", " + colorSensor.getBlue());
  }

  @Override
  public void testPeriodic() {
  }
}
