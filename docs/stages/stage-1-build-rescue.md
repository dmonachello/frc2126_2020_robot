# Stage 1: 2026 Build Rescue

Date: 2026-09-04

## Objective

Upgrade the project to current 2026 libraries and tooling without intentionally changing robot behavior.

## Scope

1. Upgrade GradleRIO and WPILib.
2. Upgrade or replace vendor dependencies only when a valid 2026 path exists.
3. Keep behavior unchanged as closely as possible.
4. Treat all warnings as failures.

## Exit Criteria

1. Clean build on 2026 tooling.
2. No compiler warnings.
3. No Gradle warnings.
4. No vendor/tooling warnings.

## Expected Work Areas

1. `build.gradle`
2. `gradle/wrapper/`
3. `vendordeps/`
4. Java API compatibility fixes required by 2026 libraries

## Notes

No migration work recorded yet.

## Student Notes

This stage is the likely teaching entry point because it creates a working 2026 project before the architectural conversion begins.
