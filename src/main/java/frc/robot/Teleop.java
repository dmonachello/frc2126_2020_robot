package frc.robot;

import frc.robot.ballmanipulator.BallManipulator;
import frc.robot.climber.Climber;
import frc.robot.controls.Controls;
import frc.robot.drivebase.DriveBase;
import frc.robot.pinout.PinOut;

// Historical note:
// This class was the original 2020 teleop coordinator. In the TimedRobot design it polled the
// Controls helper every cycle and then directly pushed those decisions into the mechanism classes.
//
// Stage 2 retirement note on September 4, 2026:
// Teleop.java is no longer part of the active runtime path. RobotContainer, subsystem default
// commands, and button bindings now own teleop behavior. The class remains in the repository as
// an archival teaching reference so students can compare the old polling model to the command-based
// structure that replaced it.
public class Teleop
{
  private final Climber climber;
  private final Controls controls;
  private final DriveBase driveBase;
  private final BallManipulator ballManipulator;
  private final double rollerSpeed;
  private final double beltSpeed;
  private final double normalValue;
  private final double slowValue;
 

    public Teleop(
        Climber climber,
        Controls controls,
     DriveBase driveBase,
     BallManipulator ballManipulator,
     double rollerSpeed,
     double beltSpeed,
     double normalValue,
     double slowValue
     )
    {
        this.climber = climber;
        this.controls = controls;
        this.driveBase = driveBase;
        this.ballManipulator = ballManipulator;
        this.rollerSpeed = rollerSpeed;
        this.beltSpeed = beltSpeed;
        this.normalValue = normalValue;
        this.slowValue = slowValue;

    }
    private double driveValue(double joystickValue,Controls.DriveSpeed speed)
    {
      switch (speed) {
        case normal:
          return joystickValue * normalValue;
        case slow:
          return joystickValue * slowValue;
        default :
          return joystickValue;
      }
    }
    public void periodic()
    {
        driveBase.drive(driveValue(controls.getLeftDrive(),controls.leftSpeed()), driveValue(controls.getRightDrive(),controls.rightSpeed()));
        switch(controls.climberDirection())
        {
          case up :
            climber.up();
            break;
          case down :
            climber.down();
            break;
        }

        if(controls.isReverse())
        {
          driveBase.reverseDrive();
        }
        if(controls.isRollerOn())
        {
          ballManipulator.outtake(rollerSpeed);
        }
        else
        {
          ballManipulator.outtake(0);  
        }
        switch(controls.getBeltDirection())
        {
          case off:
            ballManipulator.intake(0);
            break;
          case in:
            ballManipulator.intake(beltSpeed);
            break;
          case out:
            ballManipulator.intake(-beltSpeed);
            break;
        } 
    }

}
