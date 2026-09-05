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
4. Retired teleop coordinator classes are archived outside the active source tree.

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
6. Kept the old `Teleop.java` file in the tree temporarily as a readable reference during the transition.

Why this slice:

1. It introduces the standard command-based structure without forcing a full rewrite in one step.
2. It makes `Robot` thinner immediately.
3. It keeps behavior logic traceable because the command-based teleop cycle still closely mirrors the old `Teleop.periodic()` flow.

Expected follow-up work:

1. Replace the single scheduled teleop cycle with more idiomatic default commands and bindings.
2. Move more responsibility from the transition layer into commands and subsystem methods.
3. Decide when `Teleop.java` can be removed from `src/` and archived after the new structure is proven stable.

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
5. Kept `Controls.java` in the repo temporarily as a documented historical reference.

Verification:

1. Ran `./gradlew build`.
2. The project built successfully.
3. Existing unit tests passed.
4. No build warnings were emitted.

### 2026-09-04 Teleop Coordinator Retirement Slice

Decision:

1. `Teleop.java` is no longer part of the active runtime path.
2. The active teleop path now runs through `RobotContainer`, subsystem default commands, and bindings.
3. Instead of deleting `Teleop.java` immediately, it remained in the repository as an interim archival teaching reference.

Why it was handled this way:

1. Students can still read the original 2020 teleop coordinator beside the new command-based structure.
2. The existing `TeleopTest` suite temporarily preserved an executable reference for the old polling logic.
3. Keeping the file temporarily makes the before/after comparison easier during instruction.

What changed:

1. Added archival comments to `Teleop.java`.
2. Added a matching historical note to `TeleopTest.java`.
3. Documented that the command-based path had fully replaced `Teleop.java` at runtime.

Verification:

1. Ran `./gradlew build`.
2. The project built successfully.
3. Existing unit tests passed.
4. No build warnings were emitted.

### 2026-09-04 Controls Helper Retirement Slice

Decision:

1. `Controls.java` is no longer part of the active teleop path.
2. `RobotContainer` now owns the live controller objects, deadband handling, button mapping, and reverse-drive binding.
3. `Controls.java` remained in the repository temporarily as an archival teaching reference instead of being deleted immediately.

Why it was handled this way:

1. `Controls.java` explains why the 2020 robot used a polling helper instead of command bindings.
2. Students can compare the old input-translation helper directly against the new `RobotContainer` methods.
3. Keeping the file avoids losing the design history that explains how the project evolved.

What changed:

1. Added stronger archival comments to `Controls.java`.
2. Documented that the class is kept for comparison, not for active runtime use.
3. Preserved the file unchanged functionally for a teaching window before final archiving.

Verification:

1. This slice should not change runtime behavior.
2. A clean build is still required after the documentation update.

### 2026-09-04 Command-Based Teleop Test Slice

Decision:

1. Keep `TeleopTest` temporarily as executable coverage for the archived 2020 polling design.
2. Add a separate command-based test suite for the active Stage 2 teleop path.
3. Test each teleop command at the command/subsystem seam instead of trying to integration-test `RobotContainer` hardware construction.

Why it was handled this way:

1. The active runtime path is now split across multiple commands, so the tests should reflect that structure.
2. Direct command tests are easier for students to understand than heavier scheduler-and-joystick integration scaffolding.
3. This preserves the old tests while showing how behavior coverage evolves during a migration.

What changed:

1. Added `TeleopCommandsTest` under `src/test/java/frc/robot/commands`.
2. Added focused tests for drive, climber, and ball manipulator teleop commands.
3. Kept `TeleopTest` in place temporarily as historical-reference coverage for the retired polling coordinator.

Verification:

1. `./gradlew build` must pass.
2. Both the temporary `TeleopTest` suite and the new command-based tests must pass.

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

1. Added regression tests around `RobotContainer` behavior.
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
2. Remaining active and command-based tests must continue to pass.

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
2. Remaining active and command-based tests must continue to pass.

### 2026-09-04 Teleop Archive Closeout Slice

Decision:

1. Remove `Teleop.java`, `Controls.java`, and `TeleopTest.java` from the compiled source tree.
2. Preserve their teaching value under `docs/archive/` instead of keeping dead Java classes in `src/`.
3. Treat this as the Stage 2 cleanup step that ends the hybrid teleop transition.

Why it was handled this way:

1. By this point the active robot no longer used the old polling coordinator or its helper.
2. Leaving dead runtime classes in `src/main/java` would blur the final command-based architecture.
3. Moving them into Markdown keeps the history available without letting old code shape the active project.

What changed:

1. Archived `Teleop.java` into `docs/archive/teleop-2020-reference.md`.
2. Archived `Controls.java` into `docs/archive/controls-2020-reference.md`.
3. Archived `TeleopTest.java` into `docs/archive/teleop-test-2020-reference.md`.
4. Removed the old Java source and test files from the active build.
5. Updated the documentation index and migration spec to point at the archive material.

Verification:

1. `./gradlew build` must pass.
2. The active command-based tests must continue to pass without the old polling classes in the build.

### 2026-09-04 RobotContainer Scheduler Regression Slice

Decision:

1. Add scheduler-level regression tests around `RobotContainer`.
2. Keep the existing command tests and input-mapping tests, then add one higher-level layer above them.
3. Avoid test-only hooks in `RobotContainer` by injecting a hardware bundle instead.

Why it was handled this way:

1. The old `TeleopTest` verified one coordinator end to end, and Stage 2 needed an equivalent confidence layer for the command-based replacement.
2. Command-level tests alone do not prove that `RobotContainer` actually installs the right defaults and bindings.
3. This gives students a clean example of testing command-based wiring without leaving test-only methods in runtime code.

What changed:

1. Added `RobotContainerSchedulerTest` to run `CommandScheduler` against the active teleop wiring.
2. Introduced `RobotHardware` so production hardware construction is separated from `RobotContainer` behavior wiring.
3. Covered both default-command execution and the reverse-drive binding through the scheduler.

### 2026-09-04 Runtime/Test Separation Cleanup Slice

Decision:

1. Remove test-specific seams from `RobotContainer`.
2. Separate concrete WPILib construction into a production `RobotHardware` boundary.
3. Keep fake hardware setup entirely inside test code.

Why it was handled this way:

1. New students should not have to read `for testing` methods in the active robot container.
2. A real construction boundary is a better design than a test-only hook.
3. This keeps regression tests strong without letting tests shape the runtime API unnecessarily.

What changed:

1. Added `RobotHardware` to build the real robot hardware bundle.
2. Changed `RobotContainer` to consume `RobotHardware` instead of constructing everything inline.
3. Updated regression tests to inject fake `RobotHardware` objects from `src/test/java`.

Verification:

1. `./gradlew build` must pass.
2. The scheduler-level and input-mapping regression tests must still pass.

### 2026-09-04 Constants Cleanup Slice

Decision:

1. Replace `PinOut.java` with a root-level `Constants.java`.
2. Group the values by purpose instead of keeping them as mutable instance fields.
3. Use the modern WPILib constants pattern in the active code and tests.

Why it was handled this way:

1. `PinOut.java` was a 2020-style global constant holder that no longer matched the structure we want students to learn.
2. `Constants.java` is the standard WPILib convention and makes the project look more like current examples.
3. This cleanup improves readability without changing robot behavior.

What changed:

1. Added `src/main/java/frc/robot/Constants.java`.
2. Moved hardware IDs, operator mappings, and tuning values into nested constant groups.
3. Updated `RobotContainer`, `RobotHardware`, and regression tests to use `Constants`.
4. Removed `src/main/java/frc/robot/pinout/PinOut.java`.

Verification:

1. `./gradlew build` must pass.
2. No code or tests should reference `PinOut` after the cleanup.

### 2026-09-05 Button Binding Consolidation Slice

Decision:

1. Put every discrete operator-action binding in `RobotContainer.configureBindings()`.
2. Reserve `configureDefaultCommands()` for continuous controls only: the drive-stick axes.
3. Give the independently powered belt and roller separate subsystem ownership so their controls can run simultaneously.

Why it was handled this way:

1. A student should be able to open one method and find every event-style control: press, hold, and release behavior.
2. The climber is an event-style control: hold the button for up, then command down when released.
3. The 2020 `BallManipulator` class controls two independent motors. Modeling it as one subsystem would make WPILib interrupt one command when the other starts, even though the robot can physically run both at once.
4. `BeltSubsystem` and `RollerSubsystem` give those motors separate scheduler ownership. The obsolete combined `BallManipulator` wrapper was retired to `docs/archive/retired-ball-manipulator.md` because it no longer represents the active architecture.
5. The drive slow button remains part of drive-axis value interpretation. It is a scale modifier, not a separate scheduled mechanism action.

What changed:

1. Moved climber hold/release behavior from `ClimberTeleopCommand` into a `Trigger` binding.
2. Added separate bindings for belt-in, belt-out, and roller controls, keeping all original button numbers and the original belt-in priority when both belt buttons are held.
3. Replaced `BallManipulatorSubsystem` with `BeltSubsystem` and `RollerSubsystem` so their commands can run concurrently.
4. Removed the retired climber and ball-manipulator default command classes.
5. Updated scheduler regression coverage to verify active bindings and release behavior through `RobotContainer`.

Verification:

1. `./gradlew build` must pass.
2. Scheduler tests must verify that climber and ball-manipulator bindings run and stop correctly.

### 2026-09-05 Explicit Command Objects Slice

Decision:

1. Teach the command lifecycle with named command classes instead of inline command helpers.
2. Keep `RobotContainer.configureBindings()` as a short mapping from each button to a command object.
3. Make both slow-drive buttons explicit bindings while preserving independent left and right scaling.

Why it was handled this way:

1. Inline `InstantCommand` and `RunCommand` helpers are concise, but they hide the `initialize`, `execute`, `end`, and `isFinished` lifecycle that new students need to learn.
2. A student can now open `BeltInCommand`, `ToggleClimberArmsCommand`, or `SlowDriveCommand` and see the complete behavior of one action.
3. The initial command-based version allowed either drive side to be slowed independently. The current student control map replaces that with one shared slow-drive button.

What changed:

1. Replaced inline command helpers with named `Command` subclasses for driving, reversing, climbing, belt-in, belt-out, roller, and slow drive.
2. Changed bindings to use the plain `JoystickButton` form where one physical button maps directly to one command.
3. Kept `Trigger` only where the original behavior needs a combined condition: either reverse button, either roller button, or belt-out only when belt-in is not held.
4. Added scheduler coverage for normal drive, left slow drive, and both slow buttons together.

2026 library note:

1. Current WPILib uses `Command` as the base class for explicit commands. The older `CommandBase` teaching pattern is not present in the 2026.2.1 library, but the lifecycle methods are the same.

Verification:

1. `./gradlew build` must pass.
2. The scheduler tests must prove that independent mechanism and slow-drive bindings can run concurrently.

### 2026-09-05 Reverse-Drive Deferral Slice

Decision:

1. Remove reverse-drive orientation from the active student control map.
2. Keep it as an optional future exercise rather than a required teleop feature.

Why it was handled this way:

1. Reverse drive is a persistent orientation toggle, not a momentary backward-drive action; that distinction adds unnecessary control complexity for the initial student robot.
2. The two driver joysticks should focus on straightforward driving controls.
3. Students can later decide whether the feature is useful after testing the real robot and then implement it as a contained command-based exercise.

What changed:

1. Removed the active reverse button binding, its command class, its operator constant, and its RobotContainer regression tests.
2. Left the low-level drivetrain reverse capability unchanged, so the later exercise has a known starting point.
3. Preserved the historical archive references because they describe what the 2020 robot actually did.

TBD student exercise:

1. Define the intended driver use case for reverse orientation.
2. Choose one unambiguous button on the driver controller.
3. Implement a named command, add a binding, add scheduler regression coverage, and validate it on the physical robot.

### 2026-09-05 Controller Responsibility Simplification Slice

Decision:

1. Reserve driver joystick ports `0` and `1` for drivetrain controls only.
2. Move roller control from duplicate driver joystick buttons to operator controller port `2`, button `1`.
3. Keep climber and belt controls on the operator controller, giving every active non-driving mechanism action one binding.

Why it was handled this way:

1. New students can identify responsibility immediately: driver controls driving, operator controls mechanisms.
2. Duplicate roller bindings made it harder to explain which operator owns the action.
3. The separate `isRollerRequested()` condition is no longer needed after the reassignment.
4. Belt-out retains an explicit guard because in and out are contradictory commands for one belt motor; belt-in remains the documented priority if both are held.

Active control map:

| Controller port | Active controls |
| --- | --- |
| `0`: left driver joystick | Left drive axis; shared slow-drive button `2` |
| `1`: right driver joystick | Right drive axis |
| `2`: operator controller | Roller `1`; climber `5`; belt in `6`; belt out `8` |

Verification:

1. Scheduler regression tests must prove that operator roller, belt, and climber actions work together.
2. `./gradlew build` must pass with no warnings.

### 2026-09-05 Direct Belt Direction Bindings Slice

Decision:

1. Use direct, symmetric button bindings for both belt-in and belt-out.
2. Do not add code that chooses a direction when both belt buttons are held.

Why it was handled this way:

1. The controls are easier for new students to read when each belt direction follows the same pattern: one button starts one named command.
2. Holding contradictory direction buttons is operator error, not behavior that this introductory control map needs to resolve.

What changed:

1. Replaced the guarded belt-out `Trigger` with a direct `JoystickButton` binding.
2. Removed `isBeltOutRequested()` from `RobotContainer`.
3. Removed the previously documented belt-in priority from the active control behavior.

Operator responsibility:

1. Hold either belt-in or belt-out, not both at once.
2. If both are held, WPILib command scheduling determines which competing belt command remains active; that condition is intentionally unsupported.

### 2026-09-05 Runtime/Test Separation Follow-Up

Decision:

1. Remove input-helper methods that existed only for direct unit tests and are not part of the live control design.
2. Test active controls through `RobotContainerSchedulerTest`, which uses the command scheduler and fake hardware entirely from `src/test/java`.

Why it was handled this way:

1. Students should see only active control code in `RobotContainer`.
2. A scheduler-level regression test exercises the real buttons, bindings, commands, and subsystems without exposing test-only methods in production code.

What changed:

1. Removed the retired `RobotContainerInputTest` and its production input-test seam.
2. Made the two drive-axis suppliers private implementation details again.
3. Kept the focused command test and scheduler-level control-map tests in the test source tree.

### 2026-09-05 Shared Slow-Drive Control Slice

Decision:

1. Use one slow-drive button on left driver joystick port `0`, button `2`.
2. Slow both drivetrain sides together while that button is held.

Why it was handled this way:

1. A driver normally wants precision movement from the whole robot, not independent left/right speed scaling.
2. One button, one state, and one named command are easier for new students to understand and test.

What changed:

1. Replaced `SlowLeftDriveCommand` and `SlowRightDriveCommand` with `SlowDriveCommand`.
2. Simplified `DriveSpeedMode` to one shared state.
3. Updated scheduler coverage to verify normal speed, both-side slow speed, and release back to normal speed.

## Student Notes

### 2026-09-05 Student-First Hardware Construction Slice

Decision:

1. Remove the `RobotHardware` dependency-injection bundle from active production code.
2. Let `RobotContainer` directly create controllers and subsystems.
3. Let each subsystem construct and own its real hardware.
4. Defer a full scheduler-and-hardware simulation test environment as a TBD advanced exercise.

Why it was handled this way:

1. The standard beginner command-based structure is easier to follow when `RobotContainer` visibly creates the controllers and subsystems it wires together.
2. The previous `RobotHardware` class and alternate `RobotContainer` constructor existed only to support an integration-test harness, not robot behavior.
3. Focused tests can validate named command behavior using mocked subsystems entirely under `src/test/java` without putting fake-hardware paths into production classes.

What changed:

1. Removed `RobotHardware` and the `RobotContainer` fake-hardware constructor.
2. Moved concrete motor, sensor, and pneumatic construction into the corresponding subsystem constructors.
3. Removed `RobotContainerSchedulerTest` and replaced its coverage with focused named-command tests.
4. Kept the old mechanism-level regression tests; a full command scheduler simulation is now explicitly deferred.

TBD advanced test exercise:

1. Build a hardware simulation fixture outside the active production path.
2. Run `RobotContainer` through the WPILib scheduler using simulated driver-station inputs.
3. Compare that integration coverage with the smaller focused command tests.

### 2026-09-05 Ultrasonic Sensor Deferral Slice

Decision:

1. Remove ultrasonic distance sensing from the active runtime path.
2. Keep distance sensing as a TBD student extension instead of publishing unused dashboard data.

Why it was handled this way:

1. The code only displayed ultrasonic voltage and calculated distance; no teleop or autonomous behavior used the measurement.
2. Unused sensors add wiring, code, and teaching surface without a current robot decision to support.

What changed:

1. Removed the active ultrasonic class, constants, `RobotContainer` field, and autonomous dashboard update call.
2. Preserved the decision in the Markdown teaching material.

TBD student exercise:

1. Verify whether an ultrasonic sensor is installed and reliable on the physical robot.
2. Define a specific distance-driven behavior before adding it back.
3. Test the sensor conversion and the resulting robot behavior.

### 2026-09-05 Compressor Control Deferral Slice

Decision:

1. Remove explicit `Compressor` construction and `enableDigital()` calls from the active robot code.
2. Rely on the CTRE PCM's default digital closed-loop pressure control for the climber pneumatics.
3. Treat compressor telemetry and manual compressor control as a TBD advanced exercise.

Why it was handled this way:

1. The active robot uses double solenoids for the climber, and the PCM controls compressor pressure independently.
2. Creating a compressor object only to enable its default behavior adds code without changing the intended operation.

What changed:

1. Removed the `Compressor` field and `enableCompressor()` method from `RobotContainer`.
2. Removed compressor-enable calls from `teleopInit()` and `testInit()`.
3. Kept the PCM constant because the climber solenoids still require it.

TBD advanced pneumatics exercise:

1. Add compressor current, pressure-switch, and enabled-state telemetry.
2. Decide whether a real driver or safety use case requires manual compressor control.

### 2026-09-05 Direct Ball-Motor Direction Slice

Decision:

1. Remove the belt and roller software-direction flags.
2. Make positive and negative command values flow directly to the corresponding motor.

Why it was handled this way:

1. Both flags were set to `true`, so they had no active effect.
2. The extra inversion layer obscured the simple teaching rule: positive belt speed is in, negative belt speed is out.
3. If motor direction is physically wrong, correct wiring or the mechanism configuration instead of adding a hidden runtime sign switch.

### 2026-09-05 DriveSubsystem Consolidation Slice

Decision:

1. Remove the legacy `DriveBase` class from the active runtime.
2. Put the installed robot's drivetrain behavior directly in `DriveSubsystem`.
3. Remove the deferred reverse-drive implementation, not just its button binding.

Why it was handled this way:

1. `DriveSubsystem` already owns the drive motors and limit switches, so a separate forwarding class did not help beginners follow the code.
2. The old `DriveBase` accepted unused encoders and a configurable drive orientation even though this robot uses one installed orientation.
3. The active control map intentionally has no reverse-drive feature. Leaving its implementation in runtime code would make the student project harder to read.

What changed:

1. `DriveSubsystem.drive(leftSpeed, rightSpeed)` now reads both limit switches, blocks positive speed into an active switch, and commands the two motor pairs.
2. The right motor pair is explicitly inverted once during construction to match the physical drivetrain orientation.
3. Replaced the old generic `DriveBaseTest` suite with focused `DriveSubsystemTest` coverage for the installed forward-limit safety rule.
4. The optional reverse-drive idea remains a documented TBD exercise; it is not compiled runtime behavior.

Student reading path:

```text
DriveTeleopCommand -> DriveSubsystem.drive -> limit-switch safety -> motor pairs
```

### 2026-09-05 Drivetrain Limit-Switch Deferral Slice

Decision:

1. Remove the left and right drivetrain limit switches from the active runtime.
2. Treat drivetrain position sensing and travel protection as a TBD safety exercise.

Why it was handled this way:

1. The class needs a clear first command-based drivetrain example: two requested tank-drive speeds go directly to two motor pairs.
2. The team has not yet confirmed that these switches are installed, correctly wired, and needed on the physical robot.
3. Untested safety code can create a false impression that the robot is protected.

What changed:

1. Removed drivetrain `DigitalInput` construction and DIO constants.
2. Removed the forward-motion guard and its focused tests because the feature is no longer active behavior.
3. `DriveSubsystem.drive(leftSpeed, rightSpeed)` now directly commands the left and right motor pairs.

TBD safety exercise:

1. Inspect the physical drivetrain for left and right limit switches and document their purpose.
2. Verify wiring, active values, and which motion each switch must block.
3. Add the safety rule back only with a specific physical requirement and regression tests.

### 2026-09-05 Climber Subsystem Consolidation Slice

Decision:

1. Keep all active climber hardware code in `ClimberSubsystem`.
2. Remove the legacy `climber/Climber.java` and `climber/Piston.java` wrappers.

Why it was handled this way:

1. The active robot has one climber mechanism with two solenoids; separate wrapper classes made the control path longer without adding a student-facing concept.
2. The command-based ownership model is clearer when `ToggleClimberArmsCommand` requires `ClimberSubsystem`, and that subsystem visibly owns and controls both solenoids.

What changed:

1. `ClimberSubsystem` now creates named left and right `DoubleSolenoid` objects directly.
2. `up()` sets both configured out values; `down()` sets both configured in values.
3. Removed the legacy wrapper tests. The focused `ToggleClimberArmsCommand` test remains as the active command behavior regression test.

Student reading path:

```text
ToggleClimberArmsCommand -> ClimberSubsystem.toggleArms() -> left and right solenoids
```

### 2026-09-05 Climber Arm-Motion Naming Slice

Decision:

1. Name climber code for the physical arm action that is always true.
2. Retain the existing button behavior: hold to extend the arms; release to retract them.

Why it was handled this way:

1. Retracting the arms raises the robot only when they are hooked on the bar; otherwise it simply brings the arms back in.
2. Robot-level names such as `lowerRobot()` and `raiseRobot()` would therefore be misleading in some valid operating situations.

What changed:

1. Renamed the climber command to `ExtendClimberArmsCommand`.
2. Renamed the subsystem methods to `extendArms()` and `retractArms()`.
3. Updated the focused command regression test to state the actual robot behavior.

Student reading path:

```text
The next slice replaces this hold/release prototype with the final press-to-toggle binding.
```

### 2026-09-05 One-Button Climber Toggle Slice

Decision:

1. Use one climber button as a press-to-toggle control.
2. Do not command pneumatic motion when that button is released.

Why it was handled this way:

1. The operator needs one button to extend arms to hook the bar, retract them to raise the robot, and extend them again to lower the robot.
2. Releasing a button must not unexpectedly spend air or move the arms.

What changed:

1. Replaced `ExtendClimberArmsCommand` with `ToggleClimberArmsCommand`.
2. Changed the button binding from `whileTrue(...)` to `onTrue(...)`, which schedules the command only when the button is pressed.
3. `ClimberSubsystem.toggleArms()` alternates between `extendArms()` and `retractArms()`.

Physical-starting-position requirement:

1. The software begins by assuming the arms are retracted.
2. Before enabling the robot, verify that the arms are physically retracted; otherwise the first press will select the wrong next action.

Student operating sequence:

```text
First press:  extend arms and hook the bar
Second press: retract arms and raise the robot
Third press:  extend arms and lower the robot
Button release: no pneumatic action
```

This is the main architectural teaching stage. It should explain how the existing `TimedRobot` and `Teleop` flow maps into subsystems, default commands, and button bindings.
