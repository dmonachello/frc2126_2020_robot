package frc.robot;

import frc.robot.ballmanipulator.BallManipulator;
import frc.robot.climber.Climber;
import frc.robot.controls.Controls;
import frc.robot.drivebase.DriveBase;
import frc.robot.weelspinner.WeelSpinner;
import frc.robot.gimbal.*;
import frc.robot.pinout.PinOut;

public class Teleop
{
  private final Climber climber;
  private final Controls controls;
  private final DriveBase driveBase;
  private final WeelSpinner weelSpinner;
  private final BallManipulator ballManipulator;
  private final double rollerSpeed;
  private final double beltSpeed;
  private final double normalValue;
  private final double slowValue;
  private final Gimbal gimbal;
 

    public Teleop(
        Climber climber,
        Controls controls,
     DriveBase driveBase,
     WeelSpinner weelSpinner,
     BallManipulator ballManipulator,
     Gimbal gimbal,
     double rollerSpeed,
     double beltSpeed,
     double normalValue,
     double slowValue
     )
    {
        this.climber = climber;
        this.controls = controls;
        this.driveBase = driveBase;
        this.weelSpinner = weelSpinner;
        this.ballManipulator = ballManipulator;
        this.rollerSpeed = rollerSpeed;
        this.beltSpeed = beltSpeed;
        this.normalValue = normalValue;
        this.slowValue = slowValue;
        this.gimbal = gimbal;

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
        gimbal.gimbalHorizontalRelative(controls.gimbalXValue() * 0.01);
        gimbal.gimbalVerticalRelative(controls.gimbalYValue() * 0.01);
        
        weelSpinner.spin(controls.spinnerSpeed() * 1);

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