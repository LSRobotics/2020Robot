package frc.robot;

//FIRST
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.Compressor;

//Internal
import frc.robot.hardware.*;
import frc.robot.hardware.Gamepad.Key;
import frc.robot.hardware.MotorNG.Model;
import frc.robot.hardware.RangeSensor.Type;
import frc.robot.software.*;
import frc.robot.constants.*;
import frc.robot.autonomous.*;

public class Robot extends TimedRobot {

  public Gamepad gp1,gp2;
  public double driveSpeed = 1.0;
  public DriveMethod driveMethod = DriveMethod.R_STICK;
  public SendableChooser<DriveMethod> m_chooser = new SendableChooser<>();
  public MotorNG index1, index2, index3, shooter, intake, feeder;
  public RGBSensor colorSensor = new RGBSensor();
  public PIDController gyroPID;
  public RangeSensor usIntake;

  public Timer intakeTimer = new Timer("Intake Timer");

  boolean isShooting = false,
          isLastBall = false;
  Solenoid arm;

  @Override
  public void robotInit() {

    // Drive mode GUI setup
    m_chooser.setDefaultOption("Right Stick Drive (Default)",DriveMethod.R_STICK);
    m_chooser.addOption(       "Left Strick Drive",          DriveMethod.L_STICK);
    m_chooser.addOption(       "Both Strick Drive",          DriveMethod.BOTH_STICKS);

    SmartDashboard.putData("Drive Choices",m_chooser);

    //Index Motors
    index1 = new MotorNG(Statics.INDEX_1, Model.VICTOR_SPX,true);
    index2 = new MotorNG(Statics.INDEX_2, Model.TALON_SRX);
    index3 = new MotorNG(Statics.INDEX_3, Model.VICTOR_SPX);

    //Shooting Motors
    feeder = new MotorNG(Statics.FEEDER, Model.VICTOR_SPX,true);
    shooter = new MotorNG(Statics.SHOOTER, Model.FALCON_500,true);

    //Intake Mechanism
    intake  = new MotorNG(Statics.INTAKE, Model.TALON_SRX);
    arm = new Solenoid(Statics.ARM_PCM, Statics.ARM_FORWARD, Statics.ARM_REVERSE);

    usIntake = new RangeSensor(Statics.US_INTAKE_PING, Statics.US_INTAKE_ECHO,Type.DIO_US_HC_SR04);

    //Setting up motor speeds
    index1.setSpeed(0.3);
    index2.setSpeed(0.3);
    index3.setSpeed(0.3);
    feeder.setSpeed(0.4);
    intake.setSpeed(0.6);

    //Gamepads
    gp1 = new Gamepad(0);
    gp2 = new Gamepad(1);

    //Framework Core initialize (Allowing global access to everything in this class -- not safe in real world, but hey this is Robotics)
    Core.initialize(this);

    //NavX
    NavX.initialize();
    NavX.navx.zeroYaw();

    //Chassis
    //Chassis.initialize();

    //PID
    gyroPID = new PIDController(.045, .85, .005); // variables you test

    Camera.initialize();

  }

  @Override
  public void disabledPeriodic() {
    postData();
  }

  @Override
  public void teleopInit() {
    driveMethod = m_chooser.getSelected();
    System.out.println("Drive Selected: " + driveMethod);
  }

  @Override
  public void autonomousPeriodic() {

    //TODO: Complete this part
    teleopPeriodic();
  }


  @Override
  public void teleopPeriodic() {
    
    gp1.fetchData();
    gp2.fetchData();

    //updateBottom();
    updateTop();
    
    postData();

  }

  // All code for driving
  public void updateBottom() {

    //Autonomous Rotation (Experimental)
    if(gp1.isKeyToggled(Key.B)) {
      new AutonGyroTurn(90, gp1, Key.DPAD_DOWN).run();
    }

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

      switch (driveMethod) {
      case L_STICK: // Left Stick Drive
        yKey = Key.J_LEFT_Y;
        xKey = Key.J_LEFT_X;
        break;

      case BOTH_STICKS: // Both Stick Drive
        yKey = Key.J_LEFT_Y;
        xKey = Key.J_RIGHT_X;
        break;

      case R_STICK: // Right Stick Drive
        yKey = Key.J_RIGHT_Y;
        xKey = Key.J_RIGHT_X;
        break;
      }
      Chassis.drive(Utils.mapAnalog(gp1.getValue(yKey)), -gp1.getValue(xKey));
    }
  }

  public void updateTop() {

    //Ball is in
    if(usIntake.getRangeInches() < 3) {
      
      if(!intakeTimer.isBusy() && !isLastBall) {
        
          intakeTimer.start();
          index1.move(1);
          index2.move(1);
          index3.move(1);
      }
    }

    if(intakeTimer.getElaspedTimeInMs() > 200) {
      index1.stop();
      index2.stop();
      index3.stop();
      intakeTimer.stop();
      intakeTimer.zero();

      if(usIntake.getRangeInches() < 3) {
        isLastBall = true;
      }
    }


    //Autonomous Shooter
    if(gp1.isKeyToggled(Key.B)) {
      new AutonBall(gp1, Key.DPAD_DOWN).run();
      isLastBall = false;
    }

    if(shooter.getVelocity() < 20300) {
      isShooting = false;
      feeder.stop();
      index3.stop();
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

    //Shooter
    shooter.move(gp1.isKeyHeld(Key.RT),false);

    //Intake Arm
    if(gp1.isKeyToggled(Key.Y)) {
      arm.actuate();
    }
  }

  public void postData() {
    SmartDashboard.putNumber("FALCON SPEED", shooter.getVelocity());
    SmartDashboard.putNumber("Ultrasonic Intake", usIntake.getRangeInches());
    SmartDashboard.putNumber("NavX Angle", NavX.navx.getYaw());
  }

  @Override
  public void testPeriodic() {
  }
}
