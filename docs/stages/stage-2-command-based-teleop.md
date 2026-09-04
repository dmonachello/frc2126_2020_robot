# Stage 2: Command-Based Teleop

Date: 2026-09-04

## Objective

Convert teleop logic to the standard WPILib command-based structure.

## Scope

1. Introduce `RobotContainer`.
2. Create subsystems from the current mechanism classes.
3. Move teleop behavior from the manual coordinator into commands and bindings.
4. Preserve behavior established in Stage 1 as closely as possible.

## Directory and Package Migration Approach

The directory structure should be changed incrementally rather than all at once.

1. Add the new command-based structure alongside the old packages first.
2. Keep the current mechanism classes usable during the transition.
3. Use early subsystem classes to wrap or absorb existing mechanism classes.
4. Move teleop decision logic out of `Teleop.java` in small steps into default commands and button bindings.
5. Remove or reduce old coordinator code only after the replacement behavior is stable.

## Expected Mapping Pattern

1. Existing mechanism classes such as `DriveBase`, `Climber`, `BallManipulator`, `Gimbal`, and `WeelSpinner` start as the hardware/control layer.
2. New subsystem classes become the command-based ownership layer.
3. `Teleop.java` is expected to split into `RobotContainer` bindings and commands.
4. `Robot.java` should become thinner as initialization and control wiring move into standard command-based locations.

## Documentation Requirement

Maintain a mapping table during Stage 2 that records:

1. Old file or class
2. New home
3. Keep, wrap, replace, or remove decision
4. Behavior risk
5. Migration notes for students

## Exit Criteria

1. Teleop runs through command-based structure.
2. Build remains clean with no warnings.
3. Subsystem ownership and command boundaries are documented.

## Notes

No migration work recorded yet.

## Student Notes

This is the main architectural teaching stage. It should explain how the existing `TimedRobot` and `Teleop` flow maps into subsystems, default commands, and button bindings.
