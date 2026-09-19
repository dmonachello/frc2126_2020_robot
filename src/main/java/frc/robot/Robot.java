/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.CommandScheduler;


/** NAME: Robot - WPILib lifecycle shell for mode transitions and command scheduling. */
public class Robot extends TimedRobot {
  private RobotContainer robotContainer;
  private AutonomousMode autonomousMode;

  /** NAME: robotInit - creates the container and mode boundaries. */
  @Override
  public void robotInit() {
    robotContainer = new RobotContainer();
    autonomousMode = new AutonomousMode(robotContainer);
  }

  /** NAME: robotPeriodic - runs the command scheduler every robot packet. */
  @Override
  public void robotPeriodic() {
    // Command-based robots run bindings and default commands through the scheduler every cycle.
    CommandScheduler.getInstance().run();
  }

  /** NAME: autonomousInit - enters the disabled autonomous boundary. */
  @Override
  public void autonomousInit() {
    autonomousMode.enter();
  }

  /** NAME: teleopInit - enables command-based controller operation. */
  @Override
  public void teleopInit() {
    autonomousMode.exit();
  }

  /** NAME: teleopPeriodic - lifecycle hook; commands own teleop behavior. */
  @Override
  public void teleopPeriodic() {
  }

  /** NAME: testInit - cancels active commands before test mode. */
  @Override
  public void testInit() {
    autonomousMode.exit();
    CommandScheduler.getInstance().cancelAll();
  }

  /**
   * NAME
   *     testPeriodic - keeps outputs stopped during test mode.
   */
  @Override
  public void testPeriodic() {
    robotContainer.resetRobot();
  }

  /**
   * NAME
   *     disabledInit - cancels active commands when the robot disables.
   */
  @Override
  public void disabledInit() {
    autonomousMode.exit();
    CommandScheduler.getInstance().cancelAll();
  }
}
