/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandScheduler;


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
  // Stage 2 transition: hardware wiring moved out of Robot and into RobotContainer.
  private RobotContainer robotContainer;
  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Drive Forawrd And Score", kDriveForwardAndScore);
    m_chooser.addOption("Drive Forward", kDriveForwardCountinouslyAuto);
    m_chooser.addOption("Don't Do Anything" , kDontDoAnyThingAuto);
    //m_chooser.addOption("Drive Backwards" , kDriveBackwardsandIntake);
    SmartDashboard.putData("Auto choices", m_chooser);
    // Stage 2 change from the 2020 source: RobotContainer now owns teleop wiring and commands.
    robotContainer = new RobotContainer();
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
    // Command-based robots run bindings and default commands through the scheduler every cycle.
    CommandScheduler.getInstance().run();
  }

  private void reset() {
    robotContainer.resetRobot();
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
    // Cancel teleop/default commands before the old autonomous state machine runs.
    CommandScheduler.getInstance().cancelAll();
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
    robotContainer.updateAutonomousDashboard();


    switch (m_autoSelected) {
      case kDontDoAnyThingAuto:
        // Put custom auto code here
        break;
      case kDriveForwardCountinouslyAuto:
        robotContainer.drive(-0.5, -0.5 * 0.975);
        break;
      /*case kDriveBackwardsandIntake:
        switch (driveBackwardIntakeState) {
          case NotStarted:
          
            drivingForwardStartTime = System.currentTimeMillis();
            driveBackwardIntakeState = DriveBackwardIntakeState.DrivingForward;

          break;
          case DrivingForward: 
            robotContainer.drive(0.3, 0.3);
            robotContainer.intake(1);
            if(System.currentTimeMillis()-drivingForwardStartTime>=5000){
              driveBackwardIntakeState = DriveBackwardIntakeState.Done;

            }
          break;
          case Done:
            robotContainer.drive(0, 0);
            robotContainer.intake(0);
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
            robotContainer.drive(-0.5, -0.5*0.975);
            if(System.currentTimeMillis()-drivingForwardStartTime>=2500){
              driveForwardScoreState = DriveForwardScoreState.Score;

            }
          break;
          case Score:
            robotContainer.drive(0, 0);
            robotContainer.outtake(1);
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
  public void teleopInit() {
    // The compressor was started directly in teleopPeriodic() in the 2020 code.
    // In the 2026 build we enable closed-loop compressor control once on teleop entry.
    robotContainer.enableCompressor();
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testInit() {
    CommandScheduler.getInstance().cancelAll();
    robotContainer.enableCompressor();
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
    reset();
  }

  @Override
  public void disabledInit() {
    CommandScheduler.getInstance().cancelAll();
  }
}
