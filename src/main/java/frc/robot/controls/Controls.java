package frc.robot.controls;

import javax.print.attribute.standard.JobHoldUntil;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController.Button;
import frc.robot.pinout.PinOut.*;

public class Controls
{
    public static enum BeltDirection {off,in,out}
    public static enum ClimberState {up,down}
    public static enum DriveSpeed {normal,slow}
    private final Joystick joystickLeft;
    private final Joystick joystickRight;
    private final Joystick gamepad;
    private final int leftDriveAxis;
    private final int rightDriveAxis;
    private final int gimbalXAxis;
    private final int gimbalYAxis;
    private final int climberButton;
    private final int reverseButton;
    private final int beltInButton;
    private final int beltOutButton;
    private final int rollerButton;

    private final int slowModeButton;



    public Controls (
            Joystick joystickLeft,
            Joystick joystickRight,
            Joystick gamepad, 
            int leftDriveAxis, 
            int rightDriveAxis,
            int gimbalXAxis,
            int gimbalYAxis, 
            int climberButton, 
            int reverseButton,
            int beltInButton,
            int beltOutButton,
            int rollerButton,
            int slowModeButton)
    {
        this.joystickLeft = joystickLeft;
        this.joystickRight = joystickRight;
        this.gamepad = gamepad;
        this.leftDriveAxis = leftDriveAxis;
        this.rightDriveAxis = rightDriveAxis;
        this.gimbalXAxis = gimbalXAxis;
        this.gimbalYAxis = gimbalYAxis;
        this.climberButton = climberButton;
        this.reverseButton = reverseButton;
        this.beltInButton = beltInButton;
        this.beltOutButton = beltOutButton;
        this.rollerButton = rollerButton;
        this.slowModeButton = slowModeButton;
    }
    public double getLeftDrive()
    {
        // negative because forwards is negative
        return -joystickLeft.getRawAxis(leftDriveAxis);
    }
    public double getRightDrive()
    {
        return -joystickRight.getRawAxis(rightDriveAxis);
    }
    public DriveSpeed leftSpeed()
    {
        if(joystickLeft.getRawButton(slowModeButton))
        {
            return DriveSpeed.slow;
        }
        else
        {
            return DriveSpeed.normal;
        }
    }

    public DriveSpeed rightSpeed()
    {
        if(joystickRight.getRawButton(slowModeButton))
        {
            return DriveSpeed.slow;
        }
        else
        {
            return DriveSpeed.normal;
        }
    }

    public boolean isReverse()
    {
        return joystickLeft.getRawButtonPressed(reverseButton) || joystickRight.getRawButtonPressed(reverseButton);
    }
    public BeltDirection getBeltDirection()
    {
        if(gamepad.getRawButton(beltInButton))
        {
            return BeltDirection.in;
        }
        else if (gamepad.getRawButton(beltOutButton))
        {
            return BeltDirection.out;
        }
        else
        {
            return BeltDirection.off;
        }
    }
    public boolean isRollerOn()
    {
        return joystickLeft.getRawButton(rollerButton) || joystickRight.getRawButton(rollerButton);
    }
    public ClimberState climberDirection()
    {
        if(gamepad.getRawButton(climberButton))
        {
          return ClimberState.up;
        }
        
        else
        {
            return ClimberState.down;
        } 
    }

    public double spinnerSpeed()
    {
        return deadZone(gamepad.getRawAxis(0), 0.25);
    }

    public double gimbalXValue()
    {
        return deadZone(gamepad.getRawAxis(gimbalXAxis), 0.25);
    }
    public double gimbalYValue()
    {
        return deadZone(-gamepad.getRawAxis(gimbalYAxis), 0.25);
    }
    private static double deadZone(double joystickValue, double range)
    {
        if(Math.abs(joystickValue) <= range)
        {
            return 0.0;
        }
        else
        {
            return joystickValue;
        }
    }
}