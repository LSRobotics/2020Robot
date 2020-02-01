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

  public Gamepad gp1,gp2;

  public MotorNG index1, index2, index3, shooter, intake, feeder;

  boolean isShooting = false;

  Compressor compressor;

  Solenoid arm;

  @Override
  public void robotInit() {

    compressor = new Compressor();

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

  public void postData() {
    SmartDashboard.putNumber("FALCON SPEED", shooter.getVelocity());
  }

  @Override
  public void testPeriodic() {
  }
}
