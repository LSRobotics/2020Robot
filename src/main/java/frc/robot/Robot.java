package frc.robot;

//FIRST
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DriverStation;

//Internal
import frc.robot.hardware.*;
import frc.robot.hardware.Gamepad.Key;
import frc.robot.hardware.Solenoid.Status;
import frc.robot.software.*;
import frc.robot.constants.*;
import frc.robot.autonomous.*;
import frc.robot.components.Climb;
import frc.robot.components.Shooter;

public class Robot extends TimedRobot {

  public boolean isLinearAutonOK = false;
  public Gamepad gp1, gp2;
  public double driveSpeed = 1.0;
  public DriveMethod driveMethod = DriveMethod.R_STICK;
  public SendableChooser<DriveMethod> m_chooser = new SendableChooser<>();
  public RGBSensor colorSensor = new RGBSensor();
  public double lightMode;
  public static boolean isBlueLine, isRedLine, isWhiteLine, isYellowCP, isRedCP, isGreenCP, isBlueCP; //CP = control panel

  public double[] color = {};

  @Override
  public void robotInit() {

    // Drive mode GUI setup
    m_chooser.setDefaultOption("Right Stick Drive (Default)", DriveMethod.R_STICK);
    m_chooser.addOption("Left Strick Drive", DriveMethod.L_STICK);
    m_chooser.addOption("Both Strick Drive", DriveMethod.BOTH_STICKS);

    SmartDashboard.putData("Drive Choices", m_chooser);

    // Gamepads
    gp1 = new Gamepad(0);
    gp2 = new Gamepad(1);

    // Framework Core initialize (Allowing global access to everything in this class
    // -- not safe in real world, but hey this is Robotics)
    Core.initialize(this);

    NavX.initialize();
    NavX.navx.zeroYaw();

    Chassis.initialize();
    Chassis.setSpeedCurve(SpeedCurve.SQUARED);

    Shooter.initialize();

    Camera.initialize();

    Climb.initialize();

    AutonChooser.initialize();
    PixyCam.initialize();

    Lights.initialize();
  }

  @Override
  public void disabledPeriodic() {
    postData();
    Core.isDisabled = true;
  }

  @Override
  public void teleopInit() {
    Core.isDisabled = false;
    driveMethod = m_chooser.getSelected();
    System.out.println("Drive Selected: " + driveMethod);
  }

  @Override
  public void autonomousInit() {
    Core.isDisabled = false;
    isLinearAutonOK = false;
  }

  @Override
  public void testInit() {
    Core.isDisabled = false;
  }

  @Override
  public void autonomousPeriodic() {
    //NavX.navx.zeroYaw();
    if(!isLinearAutonOK) {
      isLinearAutonOK = true;
      AutonChooser.getSelected().run();
    }
  }

  @Override
  public void teleopPeriodic() {

    gp1.fetchData();
    // gp2.fetchData();

    updateBottom();
    updateTop();

    postData();

    updateLights();

  }

  // All code for driving
  public void updateBottom() {

    if (gp1.isKeyToggled(Key.J_LEFT_DOWN)) {
      new AutonPixyAlign(0).run();
    }

    // Autonomous Rotation (Experimental)
    if (gp1.isKeyToggled(Key.B)) {
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
      // FORCE Override
      Chassis.drive(Utils.mapAnalog(-gp1.getValue(yKey)), Utils.mapAnalog(gp1.getValue(xKey)));
    }


  }

  public void updateTop() {

    Shooter.update();

    //FIXME: Uncomment this when tweaking is done
    /*
    // Experimental
    Climb.turnRoller(gp1.isKeyHeld(Key.X), gp1.isKeyHeld(Key.Y));
    */
    Climb.test(gp1.getValue(Key.RT) - gp1.getValue(Key.LT));

    if(gp1.isKeyToggled(Key.Y)) {
      Climb.lock(!Climb.isEngaged());
    }

  }

  public void updateLights() {
    if (Utils.mapAnalog(gp1.getValue(Key.J_RIGHT_Y),0.2,1) != 0) {
      lightMode = .57;
    }
    else if (Shooter.numBalls == 5) {
      lightMode = -.07;
    }
    else {
      lightMode = DriverStation.getInstance().getAlliance() == DriverStation.Alliance.Blue ? 0.85 : 0.61;
    }
    
    Lights.lightChange(lightMode);
  }

  public void postData() {
    SmartDashboard.putNumber("Shooter Speed", Shooter.getVelocity());
    SmartDashboard.putNumber("Ultrasonic Intake",
    Shooter.usIntake.getRangeInches()); SmartDashboard.putNumber("NavX Angle",
    NavX.navx.getYaw()); SmartDashboard.putNumber("Number of balls",
    Shooter.getNumBalls());
    SmartDashboard.putNumber("Front Ultrasonic", Chassis.frontAligner.getRangeInches());
    SmartDashboard.putNumber("IR Sensor", Chassis.sensorIR.getRangeInches());
    SmartDashboard.putString("Current Gear", (Chassis.shifter.status == Status.FORWARD ? "Low" : "High"));
    SmartDashboard.putNumber("Angle", NavX.navx.getYaw());
    //SmartDashboard.putString("Color Sensor (R,G,B)", color[0] + ", " + color[1] + ", " + color[2]);
    SmartDashboard.putNumber("PIXY CAM", PixyCam.getTargetLocation());

    //color sensor booleans
    SmartDashboard.putBoolean("Is Blue Line Detected", isBlueLine);
    SmartDashboard.putBoolean("Is Red Line Detected", isRedLine);
    SmartDashboard.putBoolean("Is White Line Detected", isWhiteLine);
    SmartDashboard.putBoolean("Yellow", isYellowCP);
    SmartDashboard.putBoolean("Red", isRedCP);
    SmartDashboard.putBoolean("Green", isGreenCP);
    SmartDashboard.putBoolean("Blue", isBlueCP);
    SmartDashboard.putNumber("LED", lightMode);

    SmartDashboard.putNumberArray("Chassis Encoders", Chassis.getEncoderReading());
    SmartDashboard.putNumber("Chassis Encoders", Climb.getDistance());


  }

  @Override
  public void testPeriodic() {
  }
}
