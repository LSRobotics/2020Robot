package frc.robot;

//FIRST
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
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

  public Timer spitOutTimer = new Timer("Ball spit out");
  public boolean isLinearAutonOK = false;
  public Gamepad gp1, gp2;
  public double driveSpeed = 1.0;
  public DriveMethod driveMethod = DriveMethod.R_STICK;
  public SendableChooser<DriveMethod> driveChooser;
  public RGBSensor colorSensor = new RGBSensor();
  public PowerDistributionPanel pdp;
  public double lightMode;
  public static boolean isBlueLine, isRedLine, isWhiteLine, isYellowCP, isRedCP, isGreenCP, isBlueCP; // CP = control
                                                                                                      // panel
  private static Gamepad.Key xKey = Key.J_RIGHT_X, yKey = Key.J_RIGHT_Y;

  public double[] color = {};

  @Override
  public void robotInit() {

    driveChooser = new SendableChooser<>();

    // Drive mode GUI setup
    driveChooser.setDefaultOption("Right Stick Drive (Default)", DriveMethod.R_STICK);
    driveChooser.addOption("Left Strick Drive", DriveMethod.L_STICK);
    driveChooser.addOption("Both Strick Drive", DriveMethod.BOTH_STICKS);

    SmartDashboard.putData("Drive Choices", driveChooser);

    // Gamepads
    gp1 = new Gamepad(0);
    gp2 = new Gamepad(1);

    pdp = new PowerDistributionPanel();

    // Framework Core initialize (Allowing global access to everything in this class
    // -- not safe in real world, but hey this is Robotics)
    Core.initialize(this);

    NavX.initialize();
    NavX.navx.zeroYaw();

    Chassis.initialize();
    Chassis.setSpeedCurve(SpeedCurve.HYBRID);

    Shooter.initialize();

    Camera.initialize();

    Climb.initialize();

    PixyCam.initialize();

    Lights.initialize();

    AutonChooser.initialize();

    Utils.report("Robot Initialization Complete.");
  }

  @Override
  public void disabledPeriodic() {
    postData();
    Core.isDisabled = true;

    updateGUI();
  }

  @Override
  public void teleopInit() {
    Core.isDisabled = false;
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
    // NavX.navx.zeroYaw();
    if (!isLinearAutonOK) {
      isLinearAutonOK = true;
      AutonChooser.getSelected().run();
    }
  }

  @Override
  public void teleopPeriodic() {

    gp1.fetchData();
    gp2.fetchData();

    updateBottom();
    updateTop();

    postData();

    updateLights();

  }

  // All code for driving
  public void updateBottom() {

    if (gp1.isKeyToggled(Key.Y)) {
      Chassis.filp();
    }

    // Autonomous Rotation (Experimental)
    if (gp1.isKeyToggled(Key.B)) {
      new AutonPixyAlign(0).run();
    }

    if (gp1.isKeyToggled(Key.DPAD_UP)) {
      new AutonGyroTurn(0);
    }

    if(gp1.isKeyToggled(Key.DPAD_LEFT)) {
      Chassis.shift(true);
    }
    else if(gp1.isKeyToggled(Key.DPAD_RIGHT)) {
      Chassis.shift(false);
    }

    if (gp1.isKeysChanged(Key.LB, Key.RB)) {
      driveSpeed += gp1.isKeyHeld(Key.LB) ? -0.25 : 0.25;

      // Out-of-bound check
      driveSpeed = (driveSpeed < 0 ? 0 : driveSpeed);
      driveSpeed = (driveSpeed > 1 ? 1 : driveSpeed);

      Chassis.setSpeedFactor(driveSpeed);
    }
    // Drive control
    else {
      Chassis.drive(Utils.mapAnalog(-gp1.getValue(yKey)), Utils.mapAnalog(gp1.getValue(xKey)));
    }

    if(gp1.isKeysChanged(Key.LT,Key.RT)) {
      Chassis.drive(gp1.getValue(Key.RT) - gp1.getValue(Key.LT),0);
    }

  }

  public void updateTop() {

    Shooter.update();

    if(spitOutTimer.isBusy() && spitOutTimer.getElaspedTimeInMs() > 200) {
      spitOutTimer.stop();
      Shooter.index.stop();
      Shooter.numBalls = 0;
    }

    // FIXME: Uncomment this when tweaking is done
    /*
     * // Experimental Climb.turnRoller(gp1.isKeyHeld(Key.X), gp1.isKeyHeld(Key.Y));
     */
    Climb.test(gp2.getValue(Key.RT) - gp2.getValue(Key.LT));

    // Toggle intake (bringing it down & run and vice versa)
    if (gp2.isKeyToggled(Key.A)) {
      Shooter.actuateIntake();
    }

    // Ball Shooting
    if (gp2.isKeyToggled(Key.B) || gp2.isKeyToggled(Key.Y)) {
      new AutonGroup(
          // new AutonPixyAlign(0),
          new AutonBall((gp2.isKeyToggled(Key.B) ? false : true),gp2,Key.DPAD_DOWN)
          ).run();
    }

    // Spit balls out
    if (gp2.isKeyToggled(Key.DPAD_RIGHT)) {
      Shooter.index.move(-1);
      spitOutTimer.start();
    }

    if (gp2.isKeyToggled(Key.BACK)) {

      Gamepad.toggleRecording();

      if (!Gamepad.isRecording()) {
        Utils.report(Gamepad.getParsedEvents());
      }
    }

    if (gp2.isKeyToggled(Key.START)) {
      Gamepad.setRecording(false);
      new AutonRunRecord(Gamepad.getEvents()).run();
    }
  }

  public void updateLights() {
    if (Utils.mapAnalog(gp1.getValue(Key.J_RIGHT_Y)) != 0) {
      lightMode = -.07;
    } else {
      lightMode = DriverStation.getInstance().getAlliance() == DriverStation.Alliance.Blue ? 0.85 : 0.61;
    }
    Lights.lightChange(lightMode);
  }

  public void updateGUI() {
    switch (driveChooser.getSelected()) {
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
  }

  public void postData() {

    //SmartDashboard.putNumber("Shooter Speed", Shooter.getVelocity());
    SmartDashboard.putNumber("Ultrasonic Intake", Shooter.indexSensor.getRangeInches());
    SmartDashboard.putNumber("NavX Angle", NavX.navx.getYaw());
    SmartDashboard.putNumber("Number of balls", Shooter.getNumBalls());
    SmartDashboard.putNumber("Front Ultrasonic", Chassis.maxbotix.getRangeInches());
    SmartDashboard.putNumber("IR Sensor", Chassis.sensorIR.getRangeInches());
    SmartDashboard.putString("Current Gear", (Chassis.shifter.lastStatus == Status.FORWARD ? "Low" : "High"));
    SmartDashboard.putNumber("Angle", NavX.navx.getYaw());
    SmartDashboard.putBoolean("Drive Inverted", Chassis.isInverted());
    // SmartDashboard.putString("Color Sensor (R,G,B)", color[0] + ", " + color[1] +
    // ", " + color[2]);
    SmartDashboard.putNumber("PIXY CAM", PixyCam.getTargetLocation());

    // color sensor booleans
    /*
    SmartDashboard.putBoolean("Is Blue Line Detected", isBlueLine);
    SmartDashboard.putBoolean("Is Red Line Detected", isRedLine);
    SmartDashboard.putBoolean("Is White Line Detected", isWhiteLine);
    SmartDashboard.putBoolean("Yellow", isYellowCP);
    SmartDashboard.putBoolean("Red", isRedCP);
    SmartDashboard.putBoolean("Green", isGreenCP);
    SmartDashboard.putBoolean("Blue", isBlueCP);
    SmartDashboard.putNumber("LED", lightMode);
    */

    SmartDashboard.putNumber("Intake power", Shooter.intake.getCurrentPower());

    SmartDashboard.putNumberArray("Chassis Encoders", Chassis.getEncoderReading());
    SmartDashboard.putNumber("Climb Encoder", Climb.getLocation());


  }

  @Override
  public void testPeriodic() {
  }
}
