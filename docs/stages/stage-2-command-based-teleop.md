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

### 2026-09-04 Stage 2 Kickoff

Starting point:

1. Stage 1 has completed successfully.
2. The project builds successfully on the 2026 WPILib toolchain.
3. Existing unit tests pass.
4. The robot code is still structured as a `TimedRobot` project with a hand-written `Teleop` coordinator.

Initial Stage 2 goal:

Introduce the standard command-based scaffolding without changing teleop behavior yet.

Planned first actions:

1. Add `RobotContainer`.
2. Add initial `subsystems` and `commands` package structure.
3. Create a class-mapping table from the current structure into the command-based structure.
4. Keep the existing teleop flow working while the new structure is introduced alongside it.

## Class Mapping Table

| Current class | Planned command-based role | Migration decision | Behavior risk | Notes |
| --- | --- | --- | --- | --- |
| `Robot` | Thin lifecycle shell | Shrink | Medium | Initialization and binding logic should move out over time. |
| `Teleop` | Split into bindings and commands | Replace gradually | High | This is the main Stage 2 teaching target. |
| `DriveBase` | Drive subsystem hardware/control layer | Wrap first | Medium | Good early candidate for `DriveSubsystem`. |
| `Climber` | Climber subsystem hardware/control layer | Wrap first | Low | Behavior is simple and should migrate cleanly. |
| `BallManipulator` | Ball manipulator subsystem hardware/control layer | Wrap first | Medium | Likely splits into intake/outtake commands. |
| `Gimbal` | Gimbal subsystem hardware/control layer | Wrap first | Medium | Axis control may become default or manual commands. |
| `WeelSpinner` | Wheel spinner subsystem hardware/control layer | Wrap first | Low | Small mechanism, likely straightforward. |
| `Controls` | Controller bindings/input translation | Replace gradually | Medium | Likely absorbed into `RobotContainer` bindings. |

## Student Notes

This is the main architectural teaching stage. It should explain how the existing `TimedRobot` and `Teleop` flow maps into subsystems, default commands, and button bindings.
