package frc.robot.pinout;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class PinOut
{
    public   int PWMbackLeftDrive = 1;
    public   int PWMfrontLeftDrive = 0;
    public   int PWMbackRightDrive = 2;   
    public   int PWMfrontRightDrive = 3;
    
    
    public   int PWMBelt = 5;
    public   int PWMRoller = 4;
    public   int CAMpcm = 0;
    public   int CAMpdb = 1;
    public   int ChannelSolenoidLeftForward = 0;
    public   int ChannelSolenoidLeftReverse = 1;
    public   int ChannelSolenoidRightForward = 2;
    public   int ChannelSolenoidRightReverse = 3;

    public   int ultrasonicRight = 0;
    public   int ultrasonicLeft = 1;
    
    public   int leftJoystickNum = 0;
    public   int rightJoystickNum = 1;

    public   int gamepadNum = 2;
    public   int leftDriveAxis = 1;
    public   int rightDriveAxis = 1;
    public   int climberButton = 5;
    public   int reverseButton = 3;
    public   int beltInButton = 6;
    public   int beltOutButton = 8;
    public   int rollerButton = 1;
    public   int driveSlowButton = 2;

    
    
    public   boolean rightDriveForward = false;
    public   boolean isRollerPositive = true;
    public   boolean isBeltPositive = true;

    public boolean leftLimitSwitchTrippedValue = false;
    public boolean rightLimitSwitchTrippedValue = false;

    public   DoubleSolenoid.Value solenoidLeftOut = Value.kForward;
    public   DoubleSolenoid.Value solenoidLeftIn = Value.kReverse;
    public   DoubleSolenoid.Value solenoidRightOut = Value.kForward;
    public   DoubleSolenoid.Value solenoidRightIn = Value.kReverse;

    public   int DIOleftLimitSwitch = 0;
    public   int DIOrightLimitSwitch = 1;

    public   double rollerSpeed = 1;
    public   double beltSpeed = 1;

    public   double normalValue = .8;
    public   double slowValue = 0.4;
    
}   
