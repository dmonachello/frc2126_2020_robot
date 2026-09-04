/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.motorcontrol.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.ballmanipulator.BallManipulator;
import frc.robot.ballmanipulator.Belts;
import frc.robot.ballmanipulator.Roller;
import frc.robot.climber.Climber;
import frc.robot.climber.Piston;
import frc.robot.controls.Controls;
import frc.robot.drivebase.DriveBase;
import frc.robot.gimbal.Gimbal;
import frc.robot.motorcontrol.DualMotorController;
import frc.robot.pinout.PinOut;
import frc.robot.ultrasonic.Ultrasonic;
import frc.robot.weelspinner.WeelSpinner;
import edu.wpi.first.wpilibj.Compressor;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDontDoAnyThingAuto = "don't do anything";
  private static final String kDriveForwardCountinouslyAuto = "drive forward continously";
  private static final String kDriveForwardAndScore = "drive forward and score";
  private static final String kDriveBackwardsandIntake = "drive backward and intake";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
  private enum DriveForwardScoreState {NotStarted, DrivingForward, Score}
  private enum DriveBackwardIntakeState {NotStarted, DrivingForward, Done}
  private DriveBackwardIntakeState driveBackwardIntakeState;
  private DriveForwardScoreState driveForwardScoreState;
  private long drivingForwardStartTime;

  private Teleop teleop;

  private Climber climber;
  private Controls controls;
  private DriveBase driveBase;
  private WeelSpinner weelSpinner;
  private BallManipulator ballManipulator;
  private PinOut pinout;

  private MotorController speedControllerFrontRightDrive; 
  private MotorController speedControllerFrontLeftDrive;
  private MotorController speedControllerBackRightDrive;
  private MotorController speedControllerBackLeftDrive;
  private MotorController speedControllerBelt;
  private MotorController speedControllerRoller;
  private DualMotorController speedControllerGroupLeftDrive; 
  private DualMotorController speedControllerGroupRightDrive;
  private Encoder encoderLeft;
  private Encoder encoderRight;

  private Joystick joystickLeft;
  private Joystick joystickRight;
  private Joystick gamepad;

  private Piston pistonLeft;
  private Piston pistonRight;

  //private ColorManager colorManager;
  private MotorController spinner;

  private Belts belt;
  private Roller roller;

  private DoubleSolenoid leftSolenoid;
  private DoubleSolenoid rightSolenoid;

  private DigitalInput leftLimitSwitch;
  private DigitalInput rightLimitSwitch;

  private Servo servoXaxis;
  private Servo servoYaxis;
  private Gimbal gimbal;

  private Ultrasonic ultrasonicLeft;
  private Ultrasonic ultrasonicRight;

  private Compressor compressor;
  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    pinout = new PinOut();

    m_chooser.setDefaultOption("Drive Forawrd And Score", kDriveForwardAndScore);
    m_chooser.addOption("Drive Forward", kDriveForwardCountinouslyAuto);
    m_chooser.addOption("Don't Do Anything" , kDontDoAnyThingAuto);
    //m_chooser.addOption("Drive Backwards" , kDriveBackwardsandIntake);
    SmartDashboard.putData("Auto choices", m_chooser);
    
    servoXaxis = new Servo(pinout.PWMServoXAxis);
    servoYaxis = new Servo(pinout.PWMServoYAxis);
    gimbal = new Gimbal(servoXaxis, servoYaxis);
    //gimbal.gimbalInitialPosition();  // sets up the gimbal for autonomous mode

    leftLimitSwitch = new DigitalInput(pinout.DIOleftLimitSwitch);
    rightLimitSwitch = new DigitalInput(pinout.DIOrightLimitSwitch);

    speedControllerFrontLeftDrive = new Talon(pinout.PWMfrontLeftDrive);
    speedControllerBackLeftDrive = new Talon(pinout.PWMbackLeftDrive);
    speedControllerFrontRightDrive = new Talon(pinout.PWMfrontRightDrive);
    speedControllerBackRightDrive = new Talon(pinout.PWMbackRightDrive);
    speedControllerGroupLeftDrive = new DualMotorController(speedControllerFrontLeftDrive, speedControllerBackLeftDrive);
    speedControllerGroupRightDrive = new DualMotorController(speedControllerFrontRightDrive, speedControllerBackRightDrive);
    driveBase = new DriveBase(
      speedControllerGroupLeftDrive,
      speedControllerGroupRightDrive,
      encoderLeft,encoderRight,
      pinout.rightDriveForward,
      leftLimitSwitch,
      rightLimitSwitch,
      pinout.leftLimitSwitchTrippedValue,
      pinout.rightLimitSwitchTrippedValue);
    
    joystickLeft = new Joystick(pinout.leftJoystickNum);
    joystickRight = new Joystick(pinout.rightJoystickNum);
    gamepad = new Joystick(pinout.gamepadNum);
    controls = new Controls(
      joystickLeft,
      joystickRight,
      gamepad,
      pinout.leftDriveAxis,
      pinout.rightDriveAxis,
      pinout.gimbalXAxisChannel,
      pinout.gimbalYAxisChannel,
      pinout.climberButton,
      pinout.reverseButton,
      pinout.beltInButton,
      pinout.beltOutButton,
      pinout.rollerButton,
      pinout.driveSlowButton);

    leftSolenoid = new DoubleSolenoid(
      pinout.CAMpcm,
      PneumaticsModuleType.CTREPCM,
      pinout.ChannelSolenoidLeftForward,
      pinout.ChannelSolenoidLeftReverse);
    rightSolenoid = new DoubleSolenoid(
      pinout.CAMpcm,
      PneumaticsModuleType.CTREPCM,
      pinout.ChannelSolenoidRightForward,
      pinout.ChannelSolenoidRightReverse);
    pistonLeft = new Piston(leftSolenoid, pinout.solenoidLeftIn, pinout.solenoidLeftOut);
    pistonRight = new Piston(rightSolenoid, pinout.solenoidRightIn, pinout.solenoidRightOut);
    climber = new Climber(pistonLeft, pistonRight);
    
    compressor = new Compressor(pinout.CAMpcm, PneumaticsModuleType.CTREPCM);


    spinner = new Talon(pinout.PWMspinner);
    weelSpinner = new WeelSpinner(spinner);

    speedControllerBelt = new Talon(pinout.PWMBelt);
    speedControllerRoller = new Talon(pinout.PWMRoller);

    belt = new Belts(speedControllerBelt);
    roller = new Roller(speedControllerRoller);

    ballManipulator = new BallManipulator(belt, roller, pinout.isBeltPositive, pinout.isRollerPositive);

    ultrasonicRight = new Ultrasonic(pinout.ultrasonicRight);

    teleop = new Teleop(
      climber,
      controls,
      driveBase,
      weelSpinner,
      ballManipulator,
      gimbal,
      pinout.rollerSpeed,
      pinout.beltSpeed,
      pinout.normalValue,
      pinout.slowValue);

    CameraServer.startAutomaticCapture(0);
  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }

  private void reset() {
    driveBase.drive(0, 0);
    ballManipulator.intake(0);
    ballManipulator.outtake(0);
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to
   * the switch structure below with additional strings. If using the
   * SendableChooser make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    driveForwardScoreState = DriveForwardScoreState.NotStarted;
    driveBackwardIntakeState = DriveBackwardIntakeState.NotStarted;
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
    reset();
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {

    ultrasonicRight.updateDashboard();


    switch (m_autoSelected) {
      case kDontDoAnyThingAuto:
        // Put custom auto code here
        break;
      case kDriveForwardCountinouslyAuto:
        driveBase.drive(-0.5, -0.5 * 0.975);
        break;
      /*case kDriveBackwardsandIntake:
        switch (driveBackwardIntakeState) {
          case NotStarted:
          
            drivingForwardStartTime = System.currentTimeMillis();
            driveBackwardIntakeState = DriveBackwardIntakeState.DrivingForward;

          break;
          case DrivingForward: 
            driveBase.drive(0.3, 0.3);
            ballManipulator.intake(1);
            if(System.currentTimeMillis()-drivingForwardStartTime>=5000){
              driveBackwardIntakeState = DriveBackwardIntakeState.Done;

            }
          break;
          case Done:
            driveBase.drive(0, 0);
            ballManipulator.intake(0);
          break;
        }
        break;*/
      case kDriveForwardAndScore:
      default:
        switch (  driveForwardScoreState) {
          case NotStarted:
          
            drivingForwardStartTime = System.currentTimeMillis();
            driveForwardScoreState = DriveForwardScoreState.DrivingForward;

          break;
          case DrivingForward: 
            driveBase.drive(-0.5, -0.5*0.975);
            if(System.currentTimeMillis()-drivingForwardStartTime>=2500){
              driveForwardScoreState = DriveForwardScoreState.Score;

            }
          break;
          case Score:
            driveBase.drive(0, 0);
            ballManipulator.outtake(1);
          break;
        }

        // Put default auto code here
        break;
    }
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    teleop.periodic();

    compressor.enableDigital();

    /*if(compressor.getPressureSwitchValue() == false)
    {
      compressor.start();
    }
    else
    {
      compressor.stop();
    }*/
  
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
    compressor.enableDigital();
    reset();
  }
}
