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


/** NAME: Robot - WPILib lifecycle shell for autonomous and command scheduling. */
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
  /** NAME: robotInit - creates the container and publishes autonomous choices. */
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

  /** NAME: robotPeriodic - runs the command scheduler every robot packet. */
  @Override
  public void robotPeriodic() {
    // Command-based robots run bindings and default commands through the scheduler every cycle.
    CommandScheduler.getInstance().run();
  }

  /** NAME: reset - stops container-owned outputs before a mode transition. */
  private void reset() {
    robotContainer.resetRobot();
  }

  /** NAME: autonomousInit - cancels commands, selects autonomous mode, and resets outputs. */
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

  /** NAME: autonomousPeriodic - runs the retained 2020 autonomous state machine. */
  @Override
  public void autonomousPeriodic() {
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

  /** NAME: teleopInit - lifecycle hook; bindings already run through robotPeriodic. */
  @Override
  public void teleopInit() {
  }

  /** NAME: teleopPeriodic - lifecycle hook; commands own teleop behavior. */
  @Override
  public void teleopPeriodic() {
  }

  /** NAME: testInit - cancels active commands before test mode. */
  @Override
  public void testInit() {
    CommandScheduler.getInstance().cancelAll();
  }

  /**
   * NAME
   *     testPeriodic - keeps outputs stopped during test mode.
   */
  @Override
  public void testPeriodic() {
    reset();
  }

  /**
   * NAME
   *     disabledInit - cancels active commands when the robot disables.
   */
  @Override
  public void disabledInit() {
    CommandScheduler.getInstance().cancelAll();
  }
}
