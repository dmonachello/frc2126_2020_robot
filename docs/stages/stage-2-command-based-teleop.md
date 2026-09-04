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

### 2026-09-04 First Command-Based Slice

Actions taken:

1. Added the 2026 `WPILibNewCommands` vendordep so command-based classes are available to the project.
2. Added `RobotContainer` as the new wiring point for teleop hardware and operator controls.
3. Added initial subsystem wrappers around the existing mechanism classes:
   `DriveSubsystem`, `ClimberSubsystem`, `BallManipulatorSubsystem`, `GimbalSubsystem`, and `WheelSpinnerSubsystem`.
4. Moved hardware construction for teleop-owned mechanisms from `Robot` into `RobotContainer`.
5. Introduced a scheduled teleop command path using `RunCommand` while keeping the existing low-level mechanism classes intact.
6. Kept the old `Teleop.java` file in the tree as a readable reference during the transition.

Why this slice:

1. It introduces the standard command-based structure without forcing a full rewrite in one step.
2. It makes `Robot` thinner immediately.
3. It keeps behavior logic traceable because the command-based teleop cycle still closely mirrors the old `Teleop.periodic()` flow.

Expected follow-up work:

1. Replace the single scheduled teleop cycle with more idiomatic default commands and bindings.
2. Move more responsibility from the transition layer into commands and subsystem methods.
3. Decide when `Teleop.java` can be removed after the new structure is proven stable.

Verification:

1. Ran `./gradlew build`.
2. The project built successfully.
3. Existing unit tests passed.
4. A first pass used `teleopCommand.schedule()` and produced a deprecation warning.
5. Replaced that call with `CommandScheduler.getInstance().schedule(teleopCommand)`.
6. Re-ran `./gradlew build` successfully with no build warnings.

### 2026-09-04 Default Commands and Bindings Slice

Actions taken:

1. Added explicit teleop command classes under `src/main/java/frc/robot/commands`.
2. Replaced the single scheduled teleop loop with subsystem default commands.
3. Added a command-based reverse-drive binding using `Trigger` and `InstantCommand`.
4. Removed the temporary `teleopCommand` scheduling path from `Robot`.
5. Kept `Controls` as the input translation layer for now so behavior mapping stays readable.

Why this slice:

1. This is the first step that looks like a real command-based robot instead of a scheduled compatibility loop.
2. Students can now see the distinction between subsystem ownership, default commands, and button-triggered actions.
3. The existing behavior is still recognizable because each new command closely mirrors a piece of the old `Teleop.periodic()` logic.

Verification:

1. Ran `./gradlew build`.
2. The project built successfully.
3. Existing unit tests passed.
4. No build warnings were emitted.

### 2026-09-04 Direct RobotContainer Input Slice

Why `Controls` existed:

1. In the original 2020 design, `Teleop.java` needed one polling helper that translated raw joystick axes and button numbers into robot actions.
2. `Controls.java` centralized deadband handling, slow-mode scaling, reverse-drive detection, and button-to-action mapping.
3. That design fit a `TimedRobot` teleop loop, where one class repeatedly polled everything each cycle.

Why it changed:

1. In a standard command-based robot, `RobotContainer` is the normal place to own controllers, bindings, and input interpretation.
2. Keeping `Controls` in the active path would preserve old structure that students ultimately need to move beyond.
3. Moving input handling into `RobotContainer` makes the code closer to modern WPILib examples while keeping the behavior readable.

Actions taken:

1. Removed `Controls` from the active Stage 2 teleop path.
2. Moved joystick ownership directly into `RobotContainer`.
3. Changed the teleop command classes to accept suppliers rather than the old `Controls` helper.
4. Recreated the old axis scaling, deadband, and button mapping logic inside `RobotContainer`.
5. Kept `Controls.java` in the repo as a documented historical reference.

Verification:

1. Ran `./gradlew build`.
2. The project built successfully.
3. Existing unit tests passed.
4. No build warnings were emitted.

## Student Notes

This is the main architectural teaching stage. It should explain how the existing `TimedRobot` and `Teleop` flow maps into subsystems, default commands, and button bindings.
