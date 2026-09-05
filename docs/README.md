# Documentation Index

Date: 2026-09-04

This directory holds the migration and teaching notes for converting the 2020 robot project into a 2026 WPILib command-based robot.

## Core Documents

1. [2026 Migration Spec](./2026-migration-spec.md)
2. [Blocker Log](./blocker-log.md)
3. [Presentation Outline](./presentation-outline.md)

## Archive References

1. [Archived 2020 Teleop Reference](./archive/teleop-2020-reference.md)
2. [Archived 2020 Controls Reference](./archive/controls-2020-reference.md)
3. [Archived 2020 Teleop Test Reference](./archive/teleop-test-2020-reference.md)

## Stage Notes

1. [Stage 0: Archival Baseline](./stages/stage-0-archival-baseline.md)
2. [Stage 1: 2026 Build Rescue](./stages/stage-1-build-rescue.md)
3. [Stage 2: Command-Based Teleop](./stages/stage-2-command-based-teleop.md)
4. [Stage 3: Command-Based Autonomous](./stages/stage-3-command-based-autonomous.md)

## Usage

Use the stage files to record:

1. What changed
2. Why it changed
3. What students should learn
4. Any blockers, tradeoffs, or follow-up work

## Current TBD

Reverse-drive orientation is intentionally not part of the active student control map. It remains a later optional command-based exercise and is documented in the Stage 2 notes.

The full hardware/scheduler simulation test environment is also deferred. The active test suite uses focused command and mechanism tests; a realistic integration test environment is a later teaching exercise.

Ultrasonic distance sensing is not part of the active robot path. Students may later decide whether a verified distance sensor supports a useful autonomous or driver-assist feature.

Explicit compressor control and compressor telemetry are deferred. The CTRE PCM's default closed-loop pressure control remains active for the climber pneumatics.
