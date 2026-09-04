# Archived 2020 Teleop Test Reference

Date archived: 2026-09-04

## Why This Exists

`TeleopTest.java` originally served as executable reference coverage for the old polling coordinator.

Once `Teleop.java` and `Controls.java` were moved out of the compiled source tree, the test could no longer remain in `src/test/java`. It is archived here so students can still see how the old design was verified.

## What It Covered

The original tests checked three teleop scenarios:

1. Everything off.
2. Intake and roller active.
3. Reverse drive with slow mode and belt outtake.

## Teaching Note

This archive is a good point to explain that tests should move with architecture. The old teleop tests were replaced by:

1. command-based teleop tests
2. `RobotContainer` input-mapping tests

Those new tests cover the active runtime path instead of the retired polling coordinator.
