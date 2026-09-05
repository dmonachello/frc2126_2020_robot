/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.RobotBase;

/**
 * NAME
 *     Main - starts the WPILib robot application.
 *
 * DESCRIPTION
 *     This class intentionally contains no robot hardware or control initialization.
 */
public final class Main {
  /** NAME: Main - prevents construction of the application entry-point class. */
  private Main() {
  }

  /**
   * NAME
   *     main - gives WPILib the class that implements the robot lifecycle.
   *
   * PARAMETERS
   *     args - command-line arguments supplied by the Java runtime; not used by the robot.
   */
  public static void main(String... args) {
    RobotBase.startRobot(Robot::new);
  }
}
