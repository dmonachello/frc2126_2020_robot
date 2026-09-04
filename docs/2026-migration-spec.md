# 2020 Robot to 2026 Command-Based Migration Spec

Date: 2026-09-04

## Purpose

This project will migrate the 2020 robot codebase to a 2026 WPILib Java command-based robot in a way that is technically safe, reversible, and teachable for robotics students.

The migration must preserve the original project history, expose the engineering decisions made during the upgrade, and produce documentation that can be used both during instruction and afterward as a retrospective.

## Project Goals

1. Preserve the exact original 2020 project state before making migration changes.
2. Upgrade the project to current 2026 WPILib and vendor libraries.
3. Reach a clean 2026 build with no errors or warnings.
4. Convert the robot from its current timed-robot structure to the standard WPILib command-based architecture.
5. Convert teleop before autonomous.
6. Produce student-facing notes throughout the process.
7. Build presentation material from the Markdown notes after the migration is complete.

## Non-Goals

1. Change robot behavior during the initial 2026 build-rescue stage.
2. Hide migration problems or silently replace blocked dependencies.
3. Skip documentation in favor of code-only changes.
4. Start with slides before the written record exists.

## Current Starting Point

The current project is a 2020-era Java FRC robot project built around `TimedRobot` with direct hardware construction in `Robot.java` and a hand-written `Teleop` coordinator.

The project includes generated directories and files that are part of the original baseline and will be preserved in the initial archival commit.

## Operating Principles

1. Every major step must be reversible through git.
2. Small branches are preferred over long-lived multi-purpose branches.
3. Migration work must be documented as it happens.
4. If a dependency or API change blocks a stage, stop and document the blocker before choosing an alternative.
5. Students should be able to inspect both the final result and the path used to get there.

## Execution Order

The actual migration should be performed in this order.

### Stage 0: Archival Baseline

Objective:
Create the historical baseline before any migration work.

Requirements:

1. Keep the source tree totally unmodified.
2. Include generated directories and files such as `build/` and `bin/` if they are present in the working tree.
3. Create the initial local git history from the untouched project.
4. Create a new GitHub repository for this project.
5. Push the untouched baseline to GitHub before starting any migration branch.

Exit Criteria:

1. The repository exists locally and on GitHub.
2. The default branch contains the untouched original project state.
3. No migration edits are included in the baseline commit.

### Stage 1: 2026 Build Rescue

Objective:
Upgrade the project from 2020 libraries to 2026 libraries while preserving robot behavior.

Requirements:

1. Upgrade WPILib and GradleRIO to the 2026 toolchain.
2. Upgrade or replace vendor dependencies only when a valid 2026-supported path exists.
3. Do not intentionally change robot behavior in this stage.
4. Treat warnings as real failures.
5. Keep the code structure as close to the original as practical unless a change is required for 2026 compatibility.

Blocked Dependency Rule:

If any 2020 vendor dependency cannot be migrated cleanly to a 2026-compatible solution, this stage pauses. The team documents the blocker, evaluates alternatives, and only then proceeds.

Exit Criteria:

1. The project builds cleanly against 2026 libraries.
2. The build has no errors.
3. The build has no warnings, including compiler, Gradle, and dependency/tooling warnings.
4. Any exceptions or compromises are explicitly documented.

### Stage 2: Command-Based Teleop Conversion

Objective:
Convert teleop behavior from the current timed structure to the standard WPILib command-based architecture.

Target Architecture:

1. `Robot`
2. `RobotContainer`
3. `subsystems`
4. `commands`

Requirements:

1. Convert teleop first.
2. Preserve the working behavior established in Stage 1 as closely as possible.
3. Move hardware ownership and control responsibilities into standard command-based roles.
4. Make the structure teachable to students who are learning modern WPILib patterns.

Exit Criteria:

1. Teleop uses standard command-based structure.
2. The project still builds cleanly with no warnings.
3. Command ownership and subsystem boundaries are documented.

### Stage 3: Command-Based Autonomous Conversion

Objective:
Convert autonomous logic after teleop has been stabilized in command-based form.

Requirements:

1. Replace timed-state autonomous logic with commands and command groups.
2. Preserve or intentionally document any autonomous behavior changes.
3. Keep the design aligned with standard WPILib command-based practices.

Exit Criteria:

1. Autonomous routines use commands or command groups.
2. The project still builds cleanly with no warnings.
3. Autonomous structure is documented clearly enough for students to follow.

## Teaching Order

The teaching order may differ from the execution order.

Preferred teaching order:

1. Stage 1
2. Stage 2
3. Stage 3
4. Stage 0 and deeper Stage 1 details as retrospective material for interested students

Rationale:

Students may learn more effectively by starting from a working 2026 codebase, then studying the command-based transformation, and only later reviewing the archival and migration mechanics that made the work reversible.

## Branching Strategy

Migration work should use small, focused branches. Suggested pattern:

1. One branch for Stage 0 repository/bootstrap work if needed before push
2. One or more branches for Stage 1 build-rescue tasks
3. Small branches for Stage 2 command-based teleop slices
4. Small branches for Stage 3 autonomous conversion slices
5. Separate documentation branches only if the notes become large enough to justify them

Each branch should have one clear purpose and should leave a readable history for students.

## Documentation Requirements

All project notes should begin as Markdown files stored in the repository.

Documentation goals:

1. Record what changed.
2. Explain why it changed.
3. Capture blockers and alternatives considered.
4. Preserve student-facing explanations of concepts and tradeoffs.
5. Provide a clean source for later slide creation.

Suggested documentation set:

1. A top-level migration overview
2. Stage notes for each migration stage
3. A blocker log for dependency and API issues
4. Student-facing concept notes
5. A presentation outline that can later become PowerPoint slides

## Student-Facing Expectations

For each major stage, the documentation should answer:

1. What changed?
2. Why did it change?
3. What should students learn from this stage?
4. What tradeoffs or blockers appeared?

## Success Criteria

The project is successful when all of the following are true:

1. The untouched original project state is preserved in git and GitHub.
2. The project builds against 2026 libraries with no errors or warnings.
3. Teleop is converted to standard WPILib command-based structure.
4. Autonomous is converted afterward using commands and command groups.
5. The migration path is documented well enough to teach from.
6. The Markdown notes are sufficient to turn into presentation slides after completion.

## Risks

1. 2020 vendor dependencies may not have direct 2026 replacements.
2. API breakage between 2020 and 2026 may require compatibility refactors that appear small but have broad effects.
3. Test code may fail independently of production code during the build-rescue phase.
4. Generated artifacts in the baseline commit may complicate later repo hygiene, but they are intentionally preserved for historical accuracy.

## Open Implementation Questions

These questions do not block the spec, but they should be resolved as the work begins:

1. Exact GitHub repository name and ownership
2. Exact Markdown document layout under `docs/`
3. Exact branch naming convention
4. Exact definition of the clean-build command set used to validate each stage

## Immediate Next Step

Create the Stage 0 archival baseline in git and GitHub with the project totally unmodified, then add the initial Markdown documentation structure that will track the later migration stages.
