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
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
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

  public Gamepad gp1,gp2;

  public static double driveSpeed = 1.0;

  // Drive mode GUI variables and setup
  public static final String kDefaultDrive = "Default (Right Stick)";
  public static final String kCustomDrive = "Right Stick Drive";
  public static final String kCustomDrive1 = "Left Stick Drive";
  public static final String kCustomDrive2 = "Both Stick Drive";
  
  public String m_driveSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  public MotorNG index1, index2, index3, shooter, intake, feeder;

  boolean isShooting = false;

  Compressor compressor;

  Solenoid arm;

  @Override
  public void robotInit() {

    compressor = new Compressor();

    // Drive mode GUI setup
    m_chooser.setDefaultOption("Default (Right Stick)", kDefaultDrive);
    m_chooser.addOption("Right Stick Drive", kCustomDrive);
    m_chooser.addOption("Left Strick Drive", kCustomDrive1);
    m_chooser.addOption("Both Strick Drive", kCustomDrive2);
    SmartDashboard.putData("Drive choices", m_chooser);
    System.out.println("Drive Selected: " + m_driveSelected);

    index1 = new MotorNG(Statics.INDEX_1, Model.VICTOR_SPX,true);
    index2 = new MotorNG(Statics.INDEX_2, Model.TALON_SRX);
    index3 = new MotorNG(Statics.INDEX_3, Model.VICTOR_SPX);

    feeder = new MotorNG(Statics.FEEDER, Model.VICTOR_SPX,true);
    shooter = new MotorNG(Statics.SHOOTER, Model.FALCON_500,true);

    intake  = new MotorNG(Statics.INTAKE, Model.TALON_SRX);
    arm = new Solenoid(Statics.ARM_PCM, Statics.ARM_FORWARD, Statics.ARM_REVERSE);

    index1.setSpeed(0.4);
    index2.setSpeed(0.4);
    index3.setSpeed(0.4);
    
    feeder.setSpeed(0.4);

    intake.setSpeed(0.6);

    gp1 = new Gamepad(0);
    gp2 = new Gamepad(1);

    Core.initialize(this);

    Camera.initialize();

  }

  @Override
  public void disabledPeriodic() {
    postData();
  }

  @Override
  public void autonomousInit() {
    m_driveSelected = m_chooser.getSelected();
  }

  @Override
  public void autonomousPeriodic() {
    teleopPeriodic();
  }


  @Override
  public void teleopPeriodic() {
    
    gp1.fetchData();


    if(shooter.getVelocity() < 20300) {
      isShooting = false;
      feeder.stop();
      if(!gp1.isKeyHeld(Key.A)) {
        index1.move(0);
        index2.move(0);
        index3.move(0);
      }
    }
    else {
      isShooting = true;
      feeder.move(1);
      index1.move(1);
      index2.move(1);
      index3.move(1);

    }

    //Intake
    intake.move(gp1.isKeyHeld(Key.A),false);
    
    if(!isShooting) {
    //Index
    index1.move(gp1.isKeyHeld(Key.A),false);
    index2.move(gp1.isKeyHeld(Key.A),false);
    index3.move(gp1.isKeyHeld(Key.A),false);
    }

    //Shooter
    shooter.move(gp1.isKeyHeld(Key.RT),false);

    //Feeder
    //feeder.move(gp1.isKeyHeld(Key.DPAD_UP),false);

    if(gp1.isKeyToggled(Key.Y)) {
      arm.actuate();
    }
    
    postData();

  }

  // All code for driving
  public void updateBottom() {

    // Gearbox
    if (gp1.isKeyToggled(Key.DPAD_UP)) {
      Chassis.shift();
    }

    // raise drive speed
    if (gp1.isKeyToggled(Key.RB)) {
      if (driveSpeed + 0.25 <= 1.0) {
        driveSpeed += 0.25;
        Chassis.setSpeedFactor(driveSpeed);
      }
    }
    // lower drive speed
    else if (gp1.isKeyToggled(Key.LB)) {
      if (driveSpeed - 0.25 >= 0) {
        driveSpeed -= 0.25;
        Chassis.setSpeedFactor(driveSpeed);
      }
    }
    // Drive control
    else {
      Gamepad.Key yKey = Key.J_RIGHT_Y;
      Gamepad.Key xKey = Key.J_RIGHT_X;

      switch (m_driveSelected) {
      // Right Stick Drive
      case kCustomDrive:
        yKey = Key.J_RIGHT_Y;
        xKey = Key.J_RIGHT_X;
        break;
      // Left Stick Drive
      case kCustomDrive1:
        yKey = Key.J_LEFT_Y;
        xKey = Key.J_LEFT_X;
        break;
      // Both Stick Drive
      case kCustomDrive2:
        yKey = Key.J_LEFT_Y;
        xKey = Key.J_RIGHT_X;
        break;
      // Default is right stick drive
      case kDefaultDrive:
        yKey = Key.J_RIGHT_Y;
        xKey = Key.J_RIGHT_X;
        break;

      }
      Chassis.drive(Utils.mapAnalog(gp1.getValue(yKey)), -gp1.getValue(xKey));
    }
  }

  public void postData() {
    SmartDashboard.putNumber("FALCON SPEED", shooter.getVelocity());
    //SmartDashboard.putString("Current Gear", (Chassis.shifter.status == Status.FORWARD ? "Low" : "High"));
  }

  @Override
  public void testPeriodic() {
  }
}
