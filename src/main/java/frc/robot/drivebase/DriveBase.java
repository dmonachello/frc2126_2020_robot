package frc.robot.drivebase;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;

public class DriveBase
{
    // Historical cleanup note:
    // The original 2020 source used a mutable inner helper to carry left/right values through
    // the reverse, orientation, and safety transforms. During Stage 2 on September 4, 2026,
    // that helper was replaced with an immutable record so the code stays easier to read
    // without changing any drive behavior.
    private record DriveValues(double left, double right) { }

    private MotorController speedControllerLeft;
    private MotorController speedControllerRight;
    private Encoder encoderLeft;
    private Encoder encoderRight;
    private boolean reverse;
    private DigitalInput rightLimitSwitch;
    private DigitalInput leftLimitSwitch;
    private boolean rightDriveForward;

    private final boolean leftLimitSwitchTrippedValue;
    private final boolean rightLimitSwitchTrippedValue;
    public DriveBase (MotorController speedControllerLeft,
    MotorController speedControllerRight,
     Encoder encoderLeft,
     Encoder encoderRight,
      boolean rightDriveForward,
      DigitalInput leftLimitSwitch,
      DigitalInput rightLimitSwitch,
       boolean leftLimitSwitchTrippedValue,
       boolean rightLimitSwitchTrippedValue )
    {
        this.encoderLeft = encoderLeft;
        this.encoderRight = encoderRight;
        this.speedControllerLeft  = speedControllerLeft;
        this.speedControllerRight = speedControllerRight;
        this.rightDriveForward = rightDriveForward;
        this.leftLimitSwitch = leftLimitSwitch;
        this.rightLimitSwitch = rightLimitSwitch;
        this.leftLimitSwitchTrippedValue = leftLimitSwitchTrippedValue;
        this.rightLimitSwitchTrippedValue = rightLimitSwitchTrippedValue; 
    }
    public void reverseDrive()
    {
        reverse = !reverse;
    }
    private DriveValues orient(DriveValues pair)
    {
        if(rightDriveForward)
        {
            return new DriveValues(-pair.left(), pair.right());
        }

        return new DriveValues(pair.left(), -pair.right());
    }

    private DriveValues reverse(DriveValues pair)
    {
        if(reverse)
        {
            return new DriveValues(-pair.right(), -pair.left());
        }

        return pair;
    }

    private DriveValues safety(DriveValues pair)
    {
        double rightOutput;
        if(rightLimitSwitch.get() == rightLimitSwitchTrippedValue)
        {
            if(rightDriveForward)
            {
                rightOutput = Math.min(pair.right(), 0);
            }
            else
            {
                rightOutput = Math.max(pair.right(), 0);
            }
        }
        else
        {
            rightOutput = pair.right();
        }

        double leftOutput;
        if(leftLimitSwitch.get() == leftLimitSwitchTrippedValue)
        {
            if(!rightDriveForward)
            {
                leftOutput = Math.min(pair.left(), 0);
            }
            else
            {
                leftOutput = Math.max(pair.left(), 0);
            }
        }
        else
        {
            leftOutput = pair.left();
        }

        return new DriveValues(leftOutput, rightOutput);
    }

    public void drive(double left, double right)
    {
        DriveValues raw = new DriveValues(left, right);
        DriveValues processed = safety(orient(reverse(raw)));
    
        speedControllerLeft.set(processed.left());
        speedControllerRight.set(processed.right());
    }
}
