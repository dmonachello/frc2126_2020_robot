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

1. Existing mechanism classes such as `DriveBase`, `Climber`, and `BallManipulator` start as the hardware/control layer.
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
| `Controls` | Controller bindings/input translation | Replace gradually | Medium | Likely absorbed into `RobotContainer` bindings. |

### 2026-09-04 First Command-Based Slice

Actions taken:

1. Added the 2026 `WPILibNewCommands` vendordep so command-based classes are available to the project.
2. Added `RobotContainer` as the new wiring point for teleop hardware and operator controls.
3. Added initial subsystem wrappers around the existing mechanism classes:
   `DriveSubsystem`, `ClimberSubsystem`, and `BallManipulatorSubsystem`.
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

### 2026-09-04 Teleop Coordinator Retirement Slice

Decision:

1. `Teleop.java` is no longer part of the active runtime path.
2. The active teleop path now runs through `RobotContainer`, subsystem default commands, and bindings.
3. Instead of deleting `Teleop.java` immediately, it remains in the repository as an archival teaching reference.

Why it was handled this way:

1. Students can still read the original 2020 teleop coordinator beside the new command-based structure.
2. The existing `TeleopTest` suite still provides an executable reference for the old polling logic.
3. Keeping the file temporarily makes the before/after comparison easier during instruction.

What changed:

1. Added archival comments to `Teleop.java`.
2. Added a matching historical note to `TeleopTest.java`.
3. Documented that the command-based path has fully replaced `Teleop.java` at runtime.

Verification:

1. Ran `./gradlew build`.
2. The project built successfully.
3. Existing unit tests passed.
4. No build warnings were emitted.

### 2026-09-04 Controls Helper Retirement Slice

Decision:

1. `Controls.java` is no longer part of the active teleop path.
2. `RobotContainer` now owns the live controller objects, deadband handling, button mapping, and reverse-drive binding.
3. `Controls.java` remains in the repository as an archival teaching reference instead of being deleted immediately.

Why it was handled this way:

1. `Controls.java` explains why the 2020 robot used a polling helper instead of command bindings.
2. Students can compare the old input-translation helper directly against the new `RobotContainer` methods.
3. Keeping the file avoids losing the design history that explains how the project evolved.

What changed:

1. Added stronger archival comments to `Controls.java`.
2. Documented that the class is kept for comparison, not for active runtime use.
3. Preserved the file unchanged functionally so the old design remains readable for instruction.

Verification:

1. This slice should not change runtime behavior.
2. A clean build is still required after the documentation update.

### 2026-09-04 Command-Based Teleop Test Slice

Decision:

1. Keep `TeleopTest` as executable coverage for the archived 2020 polling design.
2. Add a separate command-based test suite for the active Stage 2 teleop path.
3. Test each teleop command at the command/subsystem seam instead of trying to integration-test `RobotContainer` hardware construction.

Why it was handled this way:

1. The active runtime path is now split across multiple commands, so the tests should reflect that structure.
2. Direct command tests are easier for students to understand than heavier scheduler-and-joystick integration scaffolding.
3. This preserves the old tests while showing how behavior coverage evolves during a migration.

What changed:

1. Added `TeleopCommandsTest` under `src/test/java/frc/robot/commands`.
2. Added focused tests for drive, climber, and ball manipulator teleop commands.
3. Kept `TeleopTest` in place as historical-reference coverage for the retired polling coordinator.

Verification:

1. `./gradlew build` must pass.
2. Both the archived `TeleopTest` suite and the new command-based tests must pass.

### 2026-09-04 DriveBase Helper Cleanup Slice

Decision:

1. Replace the mutable `DrivePair` helper inside `DriveBase` with an immutable value carrier.
2. Keep the reverse, orientation, and safety transform order exactly the same.
3. Treat this as a readability cleanup, not a behavior change.

Why it was handled this way:

1. `DrivePair` existed only to move two numbers through a few helper methods.
2. The old mutable inner class worked, but it was harder to explain than a small immutable value object.
3. This is a good teaching example of a safe cleanup that improves code clarity without changing robot behavior.

What changed:

1. Replaced `DrivePair` with an internal `DriveValues` record in `DriveBase`.
2. Updated `reverse`, `orient`, `safety`, and `drive` to use the new immutable value carrier.
3. Added comments explaining why the helper changed during the migration.

Verification:

1. `./gradlew build` must pass.
2. Existing `DriveBaseTest` coverage must continue to pass unchanged.

### 2026-09-04 Repository Line Ending Policy Slice

Decision:

1. Add a repository-level `.gitattributes` file.
2. Store normal text files with `LF` line endings in Git.
3. Keep Windows launcher scripts such as `.bat` and `.cmd` as `CRLF`.

Why it was handled this way:

1. The project is being taught on Windows systems where `core.autocrlf=true` is common.
2. Without a repo policy, identical edits can generate distracting line-ending warnings and noisy diffs.
3. This is a good classroom example of making collaboration rules explicit in version control.

What changed:

1. Added `.gitattributes` at the repository root.
2. Set a default text normalization policy.
3. Added explicit Windows exceptions for command launcher files.

Verification:

1. The repository should stop depending on each local machine's Git defaults for text files.
2. The build should continue to pass unchanged.

### 2026-09-04 RobotContainer Input Test Slice

Decision:

1. Add unit tests for the input-mapping logic that moved from `Controls.java` into `RobotContainer`.
2. Add a small package-private test seam in `RobotContainer` instead of using reflection.
3. Keep the production constructor unchanged for the real robot path.

Why it was handled this way:

1. The migrated input logic is now one of the most important behavior-preservation points in Stage 2.
2. A test seam is easier for students to read and discuss than reflection-based test code.
3. This shows that command-based migration still needs direct tests around control interpretation, not just command execution tests.

What changed:

1. Added a package-private `RobotContainer` constructor for tests that injects joysticks and subsystems without creating hardware.
2. Relaxed selected input-helper methods from `private` to package-private so they can be verified directly by tests in `frc.robot`.
3. Added `RobotContainerInputTest` to cover drive scaling, climber mapping, reverse detection, belt commands, and roller command behavior.

Verification:

1. `./gradlew build` must pass.
2. The new `RobotContainerInputTest` suite must pass alongside the archived and command-based teleop tests.

### 2026-09-04 Wheel Spinner Removal Slice

Decision:

1. Remove the wheel spinner from both the active runtime path and the archived reference path.
2. Treat the wheel spinner the same way as the color sensor: it was not part of the final robot hardware.
3. Update the Stage 2 notes so students can see the difference between architecture cleanup and hardware-truth cleanup.

Why it was handled this way:

1. A wheel spinner without the color-sensor-based game mechanism is not part of the real final robot configuration.
2. Keeping dead game-specific hardware in the active path would teach the wrong robot history.
3. The archived `Teleop.java` reference is still useful, but it should reflect the final robot hardware rather than preserve removed mechanisms forever.

What changed:

1. Removed `WheelSpinnerTeleopCommand`, `WheelSpinnerSubsystem`, `WheelSpinner`, and `WeelSpinner`.
2. Removed spinner construction and scheduling from `RobotContainer`.
3. Removed spinner-related logic from `Teleop`, `Controls`, `PinOut`, and the related tests.

Verification:

1. `./gradlew build` must pass.
2. Remaining archived and command-based tests must continue to pass.

### 2026-09-04 Gimbal Removal Slice

Decision:

1. Remove the gimbal from both the active runtime path and the archived reference path.
2. Treat the gimbal as hardware that does not appear to have been part of the final robot configuration.
3. Keep the documentation explicit that this was a hardware-truth correction, not a command-based architecture requirement.

Why it was handled this way:

1. The code suggests the gimbal was a manual two-servo pan/tilt mechanism, likely for a camera, but the final robot hardware history does not support keeping it.
2. Carrying uncertain or abandoned hardware forward into the 2026 command-based robot would teach the wrong system boundary.
3. Removing unused mechanisms makes the remaining migration work easier to explain and verify.

What changed:

1. Removed `GimbalTeleopCommand`, `GimbalSubsystem`, and `Gimbal`.
2. Removed gimbal construction and scheduling from `RobotContainer`.
3. Removed gimbal-related logic from `Teleop`, `Controls`, `PinOut`, and the related tests.

Verification:

1. `./gradlew build` must pass.
2. Remaining archived and command-based tests must continue to pass.

## Student Notes

This is the main architectural teaching stage. It should explain how the existing `TimedRobot` and `Teleop` flow maps into subsystems, default commands, and button bindings.
