package frc.robot.drivebase;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;

public class DriveBase
{
    private class DrivePair
    {
        public double left;
        public double right;
    }
    private SpeedController speedControllerLeft;
    private SpeedController speedControllerRight;
    private Encoder encoderLeft;
    private Encoder encoderRight;
    private boolean reverse;
    private DigitalInput rightLimitSwitch;
    private DigitalInput leftLimitSwitch;
    private boolean rightDriveForward;

    private final boolean leftLimitSwitchTrippedValue;
    private final boolean rightLimitSwitchTrippedValue;
    public DriveBase (SpeedController speedControllerLeft,
    SpeedController speedControllerRight,
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
    private DrivePair orient(DrivePair pair)
    {
        DrivePair output = new DrivePair();
        if(rightDriveForward)
        {
            output.left = -pair.left;
            output.right = pair.right;
        }
        else
        {
            output.right = -pair.right;
            output.left = pair.left;
        }
        return output;
    }
    private DrivePair reverse(DrivePair pair)
    {
        DrivePair output = new DrivePair();
        if(reverse)
        {
            output.left = -pair.right;
            output.right = -pair.left;
        }
        else{
            output.left = pair.left;
            output.right = pair.right;
        }
        return output;
    }
    private DrivePair safety(DrivePair pair)
    {
        DrivePair output = new DrivePair();
        if(rightLimitSwitch.get() == rightLimitSwitchTrippedValue)
        {
            if(rightDriveForward)
            {
                output.right = Math.min(pair.right, 0);
            }
            else
            {
                output.right = Math.max(pair.right, 0);
            }
        }
        else
        {
            output.right = pair.right;
        }

        if(leftLimitSwitch.get() == leftLimitSwitchTrippedValue)
        {
            if(!rightDriveForward)
            {
                output.left = Math.min(pair.left, 0);
            }
            else
            {
                output.left = Math.max(pair.left, 0);
            }
        }
        else
        {
            output.left = pair.left;
        }
        return output;
    }
    public void drive(double left, double right)
    {
        DrivePair raw = new DrivePair();
        raw.left = left;
        raw.right = right;

        DrivePair processed = safety(orient(reverse(raw)));
    
        speedControllerLeft.set(processed.left);
        speedControllerRight.set(processed.right);
    }
}