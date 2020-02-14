package frc.robot;

//FIRST
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Compressor;

//Internal
import frc.robot.hardware.*;
import frc.robot.hardware.Gamepad.Key;
import frc.robot.hardware.MotorNG.Model;
import frc.robot.hardware.RangeSensor.Type;
import frc.robot.software.*;
import frc.robot.constants.*;
import frc.robot.autonomous.*;
import frc.robot.components.Shooter;

public class Robot extends TimedRobot {

  public Gamepad gp1,gp2;
  public double driveSpeed = 1.0;
  public DriveMethod driveMethod = DriveMethod.R_STICK;
  public SendableChooser<DriveMethod> m_chooser = new SendableChooser<>();
  public RGBSensor colorSensor = new RGBSensor();
  public MotorNG intake;

  Solenoid arm;

  @Override
  public void robotInit() {

    // Drive mode GUI setup
    m_chooser.setDefaultOption("Right Stick Drive (Default)",DriveMethod.R_STICK);
    m_chooser.addOption(       "Left Strick Drive",          DriveMethod.L_STICK);
    m_chooser.addOption(       "Both Strick Drive",          DriveMethod.BOTH_STICKS);

    SmartDashboard.putData("Drive Choices",m_chooser);

    //Intake Mechanism
    //intake  = new MotorNG(Statics.INTAKE, Model.TALON_SRX);
    //intake.setSpeed(0.6);

    //arm = new Solenoid(Statics.ARM_PCM, Statics.ARM_FORWARD, Statics.ARM_REVERSE);
    

    //Gamepads
    gp1 = new Gamepad(0);
    //gp2 = new Gamepad(1);

    //Framework Core initialize (Allowing global access to everything in this class -- not safe in real world, but hey this is Robotics)
    Core.initialize(this);

    //NavX
    //NavX.initialize();
    //NavX.navx.zeroYaw();

    Chassis.initialize();
    Chassis.setSpeedCurve(SpeedCurve.SQUARED);

    //Shooter
    //Shooter.initialize();

    //Camera.initialize();

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

    teleopPeriodic();
  }


  @Override
  public void teleopPeriodic() {
    
    gp1.fetchData();
    //gp2.fetchData();

    updateBottom();
    //updateTop();
    
    postData();

  }

  // All code for driving
  public void updateBottom() {

    if(gp1.isKeyToggled(Key.A)) {
      new AutonEncoderForward(50).run();
    }

    //Autonomous Rotation (Experimental)
    if(gp1.isKeyToggled(Key.B)) {
      new AutonGyroTurn(90).run();
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
      //FORCE Override
      Chassis.drive(Utils.mapAnalog(-gp1.getValue(yKey)), Utils.mapAnalog(gp1.getValue(xKey)));
    }
  }

  public void updateTop() {

    Shooter.update();

    //Intake
    intake.move(gp1.isKeyHeld(Key.A),false);

    //Intake Arm
    if(gp1.isKeyToggled(Key.Y)) {
      arm.actuate();
    }

  }

  public void postData() {
    /*
    SmartDashboard.putNumber("FALCON SPEED", Shooter.getVelocity());
    SmartDashboard.putNumber("Ultrasonic Intake", Shooter.usIntake.getRangeInches());
    SmartDashboard.putNumber("NavX Angle", NavX.navx.getYaw());
    SmartDashboard.putNumber("Number of balls", Shooter.numBalls);
    */

    SmartDashboard.putNumberArray("Chassis Encoders", Chassis.getEncoderReading());
    
  }

  @Override
  public void testPeriodic() {
  }
}
