# Archived 2020 Controls Reference

Date archived: 2026-09-04

## Why This Exists

`Controls.java` was the original 2020 input helper.

By the end of Stage 2, active input ownership had moved into `RobotContainer`, so keeping `Controls.java` in the main source tree would preserve dead structure instead of just preserving history.

## What It Did

The class translated raw joystick and gamepad state into robot-facing decisions:

1. Left and right drive values.
2. Normal or slow drive mode.
3. Reverse-drive button detection.
4. Belt in, out, or off.
5. Roller on or off.
6. Climber up or down.

## Archived Source

```java
public class Controls {
    public enum BeltDirection { off, in, out }
    public enum ClimberState { up, down }
    public enum DriveSpeed { normal, slow }

    public double getLeftDrive() {
        return -joystickLeft.getRawAxis(leftDriveAxis);
    }

    public double getRightDrive() {
        return -joystickRight.getRawAxis(rightDriveAxis);
    }

    public DriveSpeed leftSpeed() {
        return joystickLeft.getRawButton(slowModeButton)
            ? DriveSpeed.slow
            : DriveSpeed.normal;
    }

    public DriveSpeed rightSpeed() {
        return joystickRight.getRawButton(slowModeButton)
            ? DriveSpeed.slow
            : DriveSpeed.normal;
    }

    public boolean isReverse() {
        return joystickLeft.getRawButtonPressed(reverseButton)
            || joystickRight.getRawButtonPressed(reverseButton);
    }

    public BeltDirection getBeltDirection() {
        if (gamepad.getRawButton(beltInButton)) {
            return BeltDirection.in;
        }
        if (gamepad.getRawButton(beltOutButton)) {
            return BeltDirection.out;
        }
        return BeltDirection.off;
    }

    public boolean isRollerOn() {
        return joystickLeft.getRawButton(rollerButton)
            || joystickRight.getRawButton(rollerButton);
    }

    public ClimberState climberDirection() {
        return gamepad.getRawButton(climberButton)
            ? ClimberState.up
            : ClimberState.down;
    }
}
```

## Teaching Note

`Controls.java` was a reasonable helper in a `TimedRobot` polling design. Stage 2 replaced it not because it was wrong for 2020, but because `RobotContainer` is the clearer ownership point in standard command-based WPILib structure.
