# Archived 2020 Teleop Reference

Date archived: 2026-09-04

## Why This Exists

`Teleop.java` was the original 2020 polling coordinator.

By the end of Stage 2, it was no longer part of the active runtime path. Keeping it in `src/main/java` would leave dead runtime classes in the project, so it was moved into documentation instead.

## What It Did

Each robot loop, the original class:

1. Read interpreted operator input from `Controls`.
2. Drove the left and right sides of the drivetrain.
3. Toggled reverse drive when requested.
4. Commanded the climber up or down.
5. Ran the ball manipulator belts in, out, or off.
6. Ran the roller for outtake when requested.

## Archived Source

```java
public class Teleop {
    private final Climber climber;
    private final Controls controls;
    private final DriveBase driveBase;
    private final BallManipulator ballManipulator;
    private final double rollerSpeed;
    private final double beltSpeed;
    private final double normalValue;
    private final double slowValue;

    public void periodic() {
        driveBase.drive(
            driveValue(controls.getLeftDrive(), controls.leftSpeed()),
            driveValue(controls.getRightDrive(), controls.rightSpeed()));

        switch (controls.climberDirection()) {
            case up:
                climber.up();
                break;
            case down:
                climber.down();
                break;
        }

        if (controls.isReverse()) {
            driveBase.reverseDrive();
        }

        if (controls.isRollerOn()) {
            ballManipulator.outtake(rollerSpeed);
        } else {
            ballManipulator.outtake(0);
        }

        switch (controls.getBeltDirection()) {
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
```

## Teaching Note

This is a useful before-example because it shows one class polling everything and making all teleop decisions directly. Stage 2 replaced that structure with `RobotContainer`, subsystem default commands, and bindings.
