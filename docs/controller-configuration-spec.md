# Controller configuration specification

## Status

This document is a proposal for review. The `XBOX_CTRL_ONLY` Git tag marks the code before these changes.

## Goal

Support three control-station configurations without changing commands or subsystems:

- Two Xbox controllers.
- Three Logitech joysticks.
- Two Logitech drive joysticks and one operator Xbox controller.

The selected configuration is a constant set before the robot code is built and deployed. The robot does not switch configurations while it is running.

## Configuration selection

Add a `ControlConfiguration` enum:

```java
public enum ControlConfiguration {
    XBOX_CONTROLLERS,
    JOYSTICK_CONTROLLERS,
    JOYSTICK_DRIVE_XBOX_OPERATOR
}
```

Select one configuration in `Constants.Operator`:

```java
public static final ControlConfiguration CONTROL_CONFIGURATION =
    ControlConfiguration.XBOX_CONTROLLERS;
```

Changing this constant requires a rebuild and redeploy.

## Driver Station USB layout

| Configuration | USB 0 | USB 1 | USB 2 |
|---|---|---|---|
| `XBOX_CONTROLLERS` | Driver Xbox | Operator Xbox | Unused |
| `JOYSTICK_CONTROLLERS` | Left drive joystick | Right drive joystick | Operator joystick |
| `JOYSTICK_DRIVE_XBOX_OPERATOR` | Left drive joystick | Right drive joystick | Operator Xbox |

Driver Station must assign the controllers to these USB positions before the robot is enabled.

## Control mappings

| Robot action | Two Xbox controllers | Three joysticks | Two joysticks and Xbox |
|---|---|---|---|
| Left drivetrain | Driver left Y axis | Left joystick Y axis | Left joystick Y axis |
| Right drivetrain | Driver right Y axis | Right joystick Y axis | Right joystick Y axis |
| Slow drive | Driver right bumper | Right joystick trigger | Right joystick trigger |
| Belt inward | Operator A | Operator joystick button 1 | Operator A |
| Belt outward | Operator B | Operator joystick button 2 | Operator B |
| Roller | Operator X | Operator joystick button 3 | Operator X |
| Toggle climber | Operator Y | Operator joystick button 4 | Operator Y |

The joystick button numbers are proposed mappings. Confirm them against the physical Logitech model before implementation is complete.

## `RobotControls` responsibility

Add a `RobotControls` class that hides the selected controller hardware from `RobotContainer`.

The public methods describe robot actions:

```java
public double getLeftDriveValue();
public double getRightDriveValue();

public Trigger slowDrive();
public Trigger beltIn();
public Trigger beltOut();
public Trigger runRoller();
public Trigger toggleClimber();
```

The `RobotControls` constructor receives the selected `ControlConfiguration`. It creates the required WPILib controller objects and selects the matching axis suppliers and triggers once. Repeating command execution does not check the configuration.

`RobotControls` applies the drive deadband and normalizes axis direction so that positive values mean forward. `DriveTeleopCommand` continues to apply speed scaling and slew-rate limiting.

## `RobotContainer` changes

`RobotContainer` owns one `RobotControls` object instead of controller-specific driver and operator fields.

The drivetrain default command uses the normalized drive values:

```java
driveSubsystem.setDefaultCommand(
    new DriveTeleopCommand(
        driveSubsystem,
        robotControls::getLeftDriveValue,
        robotControls::getRightDriveValue,
        driveSpeedMode));
```

Bindings use robot actions instead of physical button names:

```java
robotControls.beltIn()
    .whileTrue(new BeltInCommand(beltSubsystem));

robotControls.slowDrive()
    .whileTrue(new SlowDriveCommand(driveSpeedMode));
```

The command scheduler continues to poll the selected triggers. Commands do not know which controller configuration supplies them.

## Code that remains unchanged

The change must not alter the behavior of these classes:

- `DriveTeleopCommand`
- `SlowDriveCommand`
- `DriveSubsystem`
- `BeltInCommand`
- `BeltOutCommand`
- `RollerCommand`
- `ToggleClimberArmsCommand`

## Acceptance criteria

- The project builds and all automated tests pass for each configuration value.
- Xbox mode preserves the current controls and behavior.
- Each drive joystick controls only its assigned side of the drivetrain.
- Centering either drive joystick commands zero input for that side after deadband is applied.
- All configurations use the existing drive scales and slew-rate limits.
- The three-joystick configuration provides every current operator action.
- The mixed configuration uses joysticks for driving and the Xbox controller for operator actions.
- `RobotContainer` contains no Xbox-specific or joystick-specific button and axis mappings.
- No controller-configuration checks occur in `DriveTeleopCommand.execute()`.
- Driver Station USB assignments match the selected configuration before testing.

## Hardware checks

Before the feature is complete, verify these items with the physical controllers and Driver Station diagnostics:

- The exact Logitech joystick model.
- The Y-axis direction for both drive joysticks.
- The right joystick trigger number.
- Operator joystick button numbers 1 through 4 and their physical locations.
