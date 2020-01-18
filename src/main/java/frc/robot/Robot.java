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
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import com.revrobotics.ColorSensorV3;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.I2C;
//Internal
import frc.robot.hardware.*;
import frc.robot.hardware.Gamepad.Key;
import frc.robot.constants.SpeedCurve;
import frc.robot.hardware.MotorNG.Model;
import frc.robot.software.*;

public class Robot extends TimedRobot {

  //Shared (Make sure these are "public" so that Core can take them in, which allows global access to happen)
  public Gamepad gp1,gp2;

  public MotorNG sparkMax1, sparkMax2, falcon,shooterUp,shooterDown;

  public ColorSensorV3 colorSensor;
  public Ultrasonic us;
  public Compressor compressor;

  public double motorSpeed = 1.0,
                shooterSpeed = 1.0;
  public final double SPD_TWEAK_INTERVAL = 0.2;
  public boolean isFirstSparkMax = true;
  public static double driveSpeed = 1.0;

  //Private
  boolean isLowSpeed = false,
                 isHookPowered = false,
                 isRollerPowered = false;

  //Drive mode GUI variables and setup
  public static final String kDefaultDrive = "Default";
  public static final String kCustomDrive = "Right Stick Drive";
  public static final String kCustomDrive1 = "Left Stick Drive";
  public static final String kCustomDrive2 = "Both Stick Drive";
  public String m_driveSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  @Override
  public void robotInit() {

    //Drive mode GUI setup
    m_chooser.setDefaultOption("Default", kDefaultDrive);
    m_chooser.addOption("Right Stick Drive", kCustomDrive);
    m_chooser.addOption("Left Strick Drive", kCustomDrive1);
    m_chooser.addOption("Both Strick Drive", kCustomDrive2);
    SmartDashboard.putData("Drive choices", m_chooser);
    System.out.println("Drive Selected: " + m_driveSelected);

    Core.initialize(this);

    Chassis.initialize();
    gp1 = new Gamepad(0);
    //gp2 = new Gamepad(1);

    Camera.initialize();

    compressor = new Compressor();
  
    sparkMax1 = new MotorNG(Statics.SPARK_MAX_1, Model.SPARK_MAX);
    sparkMax2 = new MotorNG(Statics.SPARK_MAX_2, Model.SPARK_MAX,true);
    falcon    = new MotorNG(Statics.FALCON, Model.FALCON_500);
    shooterUp = new MotorNG(Statics.FALCON_SHOOTER_UP, Model.FALCON_500);
    shooterDown = new MotorNG(Statics.FALCON_SHOOTER_DOWN, Model.FALCON_500);

    colorSensor = new ColorSensorV3(I2C.Port.kOnboard);

    us = new Ultrasonic(Statics.US_PING, Statics.US_ECHO, Ultrasonic.Unit.kMillimeters);
    us.setAutomaticMode(true);
  }

  @Override
  public void robotPeriodic() {
  }

  @Override
  public void autonomousInit() {
    m_driveSelected = m_chooser.getSelected();
  }

  @Override
  public void teleopInit() {
    m_driveSelected = m_chooser.getSelected();
  }

  @Override
  public void autonomousPeriodic() {
    //TODO: ACTUALLY DO SOME KIND OF AUTON
    teleopPeriodic();
  }


  @Override
  public void teleopPeriodic() {
    
    gp1.fetchData();

    updateShooters();
    updateTestMotors();

    updateBottom();
    updateTop();

    postData();

  }


  
   //All code for driving
   public void updateBottom() {

    //raise drive speed
    if(gp1.isKeyToggled(Key.RB)) {
      if(driveSpeed + 0.25 <= 1.0) {
        driveSpeed += 0.25;
        Chassis.setSpeedFactor(driveSpeed);
        SmartDashboard.putNumber("Speed", driveSpeed);
      }
    }
    //lower drive speed
    else if(gp1.isKeyToggled(Key.LB)) {
      if(driveSpeed - 0.25 >= 0) {
        driveSpeed -= 0.25;
        Chassis.setSpeedFactor(driveSpeed);
        SmartDashboard.putNumber("Speed", driveSpeed);
      }
    }

    // Assistive Autonomous
    if (gp1.isKeyToggled(Key.DPAD_LEFT)) {
      AutoPilot.turnRobotByTime(true);
    } 
    else if (gp1.isKeyToggled(Key.DPAD_RIGHT)) {
      AutoPilot.turnRobotByTime(false);
    }


    
    // Drive control 
    else {
      double x = 0,y = 0;
      switch(m_driveSelected){
        //Right Stick Drive
        case kCustomDrive:
           y = Utils.mapAnalog(gp1.getValue(Key.J_RIGHT_Y));
           x = Utils.mapAnalog(gp1.getValue(Key.J_RIGHT_X));
          break;
        //Left Stick Drive
        case kCustomDrive1:
           y = Utils.mapAnalog(gp1.getValue(Key.J_LEFT_Y));
           x = Utils.mapAnalog(gp1.getValue(Key.J_LEFT_X));
          break;
        //Both Stick Drive
        case kCustomDrive2:
           y = Utils.mapAnalog(gp1.getValue(Key.J_LEFT_Y));
           x = Utils.mapAnalog(gp1.getValue(Key.J_RIGHT_X));
          break;
        //Default is right stick drive
        case kDefaultDrive:
          y = Utils.mapAnalog(gp1.getValue(Key.J_RIGHT_Y));
          x = Utils.mapAnalog(gp1.getValue(Key.J_RIGHT_X));
          break;

      }
      Chassis.drive(y,x);
    }
  }


  private void updateTestMotors() {
    
    boolean isSpeedChanged = false;

    //Toggle SparkMax Motors
    if(gp1.isKeyToggled(Key.Y)) {
      isFirstSparkMax = !isFirstSparkMax;
  }

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
  /*
  if(gp1.isKeysChanged(Key.LT,Key.RT)) {
    double speed = gp1.getValue(Key.RT) - gp1.getValue(Key.LT);
    falcon.move(speed);
  }
  */

  if (gp1.isKeysChanged(Key.LT)) {
    leftMotor.set(gp1.getValue(Key.J_RIGHT_Y));
    rightMotor.set(gp1.getTriggerAxis(Hand.kLeft));
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

  public void updateTop() {

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
