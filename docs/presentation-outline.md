# Presentation Outline

Date: 2026-09-04

This Markdown file is the running source for the future presentation deck.

## Slide 1: Project Goal

Title:
From 2020 TimedRobot to 2026 Command-Based Robot

Key points:

1. Migrate a 2020 FRC Java robot project to the 2026 WPILib toolchain.
2. Preserve robot behavior during the initial migration.
3. Convert teleop to standard WPILib command-based structure in teachable steps.
4. Keep a written record of decisions, blockers, and code changes.

## Slide 2: Starting Point

Title:
What the 2020 Project Looked Like

Key points:

1. The project was built around `TimedRobot`.
2. Hardware was constructed directly inside `Robot.java`.
3. Teleop behavior was coordinated by a hand-written `Teleop.java`.
4. The project depended on 2020-era GradleRIO, WPILib, and vendordeps.
5. Existing unit tests were already present in the repository.

## Slide 3: Why the Old Project Did Not Open Cleanly

Title:
First Failure: Toolchain Age

Key points:

1. The original project used Gradle `6.0.1`.
2. The original project used `edu.wpi.first.GradleRIO` version `2020.3.2`.
3. The current machine runtime was Java `17.0.12`.
4. The old build failed before project evaluation with a Groovy initialization error.
5. The first blocker was the build stack, not the robot logic.

Teaching note:

Students should see that old code often fails first because of tools and dependencies, not because the business or robot logic is wrong.

## Slide 4: Stage 0

Title:
Preserve the Baseline Before Migrating

Key points:

1. The original repository state was committed locally first.
2. A GitHub repository was created before migration work continued.
3. The baseline was tagged as `stage-0-baseline`.
4. This created a rollback point and a clean historical record.

Why it matters:

1. Migration work is safer when every step is reversible.
2. Students can inspect both the final result and the path used to get there.

## Slide 5: Stage 1 Objective

Title:
Build Rescue Without Behavior Changes

Key points:

1. Upgrade to 2026 WPILib and GradleRIO.
2. Preserve robot behavior as closely as possible.
3. Treat warnings as failures.
4. Pause on broken dependencies and document the decision process.

## Slide 6: Stage 1 Blockers

Title:
What Broke During the 2026 Upgrade

Key points:

1. `WPILibOldCommands.json` was invalid for 2026.
2. `ColorSensorV3.json` was invalid for 2026.
3. The repo was missing `.wpilib/wpilib_preferences.json`.
4. 2026 WPILib removed or moved several 2020 APIs such as:
   `SpeedController`, `SpeedControllerGroup`, older camera usage, and older compressor calls.

Teaching note:

A migration is partly an investigation. Some dependencies are still real, and some are leftovers that should be removed.

## Slide 7: Stage 1 Decisions

Title:
How Stage 1 Was Resolved

Key points:

1. Upgraded the wrapper, GradleRIO, `settings.gradle`, and Java target to the 2026 template values.
2. Removed `WPILibOldCommands.json` after confirming no source usage.
3. Removed the old REV color sensor vendordep and dead `ColorManager` code.
4. Added `.wpilib/wpilib_preferences.json` with team number `2126`.
5. Replaced removed motor-control APIs with current equivalents.
6. Added a local `DualMotorController` wrapper to preserve behavior without deprecated WPILib grouping APIs.

## Slide 8: Stage 1 Outcome

Title:
Stage 1 Result

Key points:

1. `./gradlew build` succeeds on the 2026 toolchain.
2. Existing unit tests pass.
3. No build warnings remain.
4. The project is now a stable 2026 baseline for architecture work.

Teaching note:

This is a good classroom entry point because students can start from a working 2026 build instead of starting in tooling failure.

## Slide 9: Stage 2 Strategy

Title:
How to Convert Structure Without Breaking Everything

Key points:

1. Do not rewrite the whole robot at once.
2. Introduce command-based structure alongside the old code.
3. Keep the old mechanism classes working underneath the new structure.
4. Move responsibilities gradually from `Teleop.java` into subsystems, commands, and bindings.

## Slide 10: Stage 2 First Slice

Title:
Introduce RobotContainer and Subsystems

Key points:

1. Added `RobotContainer`.
2. Added subsystem wrappers for drive, climber, and ball manipulator.
3. Moved teleop-owned hardware construction into `RobotContainer`.
4. Kept the old low-level mechanism classes as the hardware/control layer.

Why this step:

1. It makes `Robot.java` thinner.
2. It introduces standard WPILib structure early.
3. It keeps the migration readable for students.

## Slide 11: Stage 2 Second Slice

Title:
Move Teleop Into Default Commands and Bindings

Key points:

1. Added command classes for drive, climber, and ball manipulator.
2. Replaced a temporary scheduled teleop loop with subsystem default commands.
3. Added a `Trigger` plus `InstantCommand` binding for reverse drive.
4. Kept `Controls` temporarily so students can compare old polling logic with new command-based structure.

## Slide 12: Why Controls Existed

Title:
Why the 2020 Project Had a Controls Helper

Key points:

1. `Controls.java` translated raw joystick input into meaningful robot actions.
2. It centralized deadband logic, slow-mode scaling, and button mappings.
3. That made sense in the original `TimedRobot` polling model.
4. It reduced duplication inside the old `Teleop.java` coordinator.

Teaching note:

This is a good example of a reasonable design that fit the old architecture, even though it is not the best long-term shape for a command-based robot.

## Slide 13: Stage 2 Third Slice

Title:
Move Input Handling Into RobotContainer

Key points:

1. Removed `Controls` from the active teleop path.
2. Moved joystick ownership directly into `RobotContainer`.
3. Updated teleop command classes to use suppliers instead of the legacy helper.
4. Recreated the old input behavior directly in the command-based wiring layer.
5. Kept `Controls.java` in the repo as a historical reference for students.

## Slide 14: Retiring Controls.java From Runtime Use

Title:
Keep the Input Helper as Evidence, Not Active Code

Key points:

1. `Controls.java` no longer participates in the active teleop runtime path.
2. `RobotContainer` now owns the live joystick objects, input interpretation, and bindings.
3. The file stays in the repository so students can inspect the original polling-helper design.
4. This lets the class compare an old but reasonable `TimedRobot` pattern with the command-based replacement.

## Slide 15: Removed 2020 Game-Specific Hardware

Title:
Remove What Was Not On the Final Robot

Key points:

1. The color sensor was removed in Stage 1 because it never made the final 2020 robot.
2. The wheel spinner was later removed for the same reason.
3. This was a hardware-truth correction, not just a code cleanup.
4. Students should learn to separate architectural migration from confirming what the real robot actually had.

## Slide 16: Remove Unclear Experimental Hardware

Title:
Do Not Carry Forward Hardware You Cannot Justify

Key points:

1. The gimbal code described a two-servo pan/tilt mechanism, likely intended to move a camera.
2. The code did not prove that the final robot actually used that mechanism.
3. Once the hardware purpose could not be justified, the gimbal was removed.
4. Students should learn that old code can contain experiments that should not be preserved blindly.

## Slide 17: Current Stage 2 State

Title:
Where the Project Stands Now

Key points:

1. The project builds successfully.
2. Tests still pass.
3. Teleop now uses command-based defaults and bindings.
4. `RobotContainer` now owns active input handling.
5. The project is still a hybrid transition codebase.
6. `Teleop.java` and `Controls.java` remain in the tree as teaching references.

## Slide 18: What Changed in the Code Structure

Title:
Old vs New Ownership

Before:

1. `Robot.java` created hardware and ran teleop behavior directly or through `Teleop.java`.
2. Mechanism logic lived in plain helper classes.

After current Stage 2 checkpoint:

1. `Robot.java` mainly owns lifecycle and autonomous mode flow.
2. `RobotContainer` owns hardware wiring and command setup.
3. `subsystems/` own mechanism-facing command-based access.
4. `commands/` own teleop behavior slices.

## Slide 19: Lessons Learned

Title:
Migration Lessons

Key points:

1. Preserve history before touching the build.
2. Expect tooling failures before source failures.
3. Remove stale dependencies only after confirming they are unused.
4. Small compatibility shims can reduce migration risk.
5. A hybrid intermediate structure is easier to teach than a big-bang rewrite.

## Slide 20: Suggested Student Exercises

Title:
Ways Students Can Explore the Project

Key points:

1. Trace one behavior from the old `Teleop.java` to the new command-based command.
2. Compare `Robot.java` before and after Stage 2.
3. Explain why `RobotContainer` is useful.
4. Identify which classes are still transitional and which are already idiomatic command-based code.
5. Propose the next slice to continue the migration.

## Slide 21: Retiring Teleop.java

Title:
Retire the Old Coordinator Without Losing the Lesson

Key points:

1. `Teleop.java` is no longer used by the active robot runtime.
2. `RobotContainer`, default commands, and bindings now own teleop behavior.
3. `Teleop.java` was kept as an archival teaching reference instead of being deleted immediately.
4. `TeleopTest` now acts as a behavior reference for the old polling model.

Teaching note:

Removing a legacy design from runtime use does not mean throwing away the evidence. Sometimes the old code is most valuable as a comparison artifact.

## Next Presentation Updates

Add future slides or revise these when:

1. Stage 1 receives hardware-validation notes.
2. Stage 2 removes more of the old teleop structure.
3. Stage 3 begins autonomous command conversion.
