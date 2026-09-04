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

### 2026-09-04 Inventory Baseline

Current build configuration:

1. `build.gradle` uses `edu.wpi.first.GradleRIO` version `2020.3.2`.
2. `gradle/wrapper/gradle-wrapper.properties` pins Gradle `6.0.1`.
3. `settings.gradle` is wired to `C:\\Users\\Public\\wpilib\\2020\\maven`.
4. The project Java level is set to `JavaVersion.VERSION_11`.

Current machine/tooling context:

1. The system Java runtime is `OpenJDK 17.0.12`.
2. WPILib 2026 is installed locally at `C:\\Users\\Public\\wpilib\\2026`.
3. A 2026 Java Gradle template is available locally under the WPILib VS Code extension install.

Observed build result:

1. `./gradlew build` fails before project evaluation.
2. The first failure is a Groovy initialization error under Java 17:
   `NoClassDefFoundError: Could not initialize class org.codehaus.groovy.vmplugin.v7.Java7`
3. This confirms the current blocker is the 2020 build stack itself, not yet the robot source code.

Vendor dependency inventory:

1. `vendordeps/ColorSensorV3.json`
   Uses REV Color Sensor V3 version `1.0.1`.
2. `vendordeps/WPILibOldCommands.json`
   Provides the old command framework compatibility package.

Code usage inventory:

1. `ColorManager.java` imports `com.revrobotics.ColorSensorV3`.
2. No source usage of WPILib old command classes was found in `src/main/java` or `src/test/java`.
3. The current robot structure is a custom `TimedRobot` plus a hand-written `Teleop` coordinator, not a command-based robot.

2026 reference inventory:

1. The local 2026 WPILib Java template targets Java 17.
2. The local 2026 template uses the newer `wpi.java.*` dependency layout.
3. The local 2026 `settings.gradle` points to `C:\\Users\\Public\\wpilib\\2026\\maven`.
4. The local 2026 template uses JUnit 5 rather than JUnit 4.

Initial assessment:

1. The first Stage 1 task is build-system modernization, not source-level API cleanup.
2. `WPILibOldCommands.json` may be removable because the source tree does not appear to use old command classes.
3. REV Color Sensor support may still matter if `ColorManager` remains in scope, but actual runtime use must be confirmed during migration.
4. Tests will likely need separate attention after the project can evaluate and resolve dependencies.

Planned next actions:

1. Update the build files toward the local 2026 WPILib template structure.
2. Re-run the build and capture the next blocker after the wrapper/toolchain changes.
3. Only after the build stack runs, begin resolving 2026 API and dependency issues.

### 2026-09-04 Build-System Upgrade Attempt

Build-file changes made:

1. Updated `build.gradle` to use `edu.wpi.first.GradleRIO` version `2026.2.1`.
2. Updated the project Java target from 11 to 17.
3. Reworked dependencies to the `wpi.java.*` layout used by the local 2026 template.
4. Updated `settings.gradle` to point at `C:\\Users\\Public\\wpilib\\2026\\maven`.
5. Updated the Gradle wrapper properties from `6.0.1` to `8.11`.
6. Replaced the wrapper launcher files with the local 2026 WPILib template versions.

Observed result after build-file upgrade:

1. `./gradlew build` now starts successfully under Java 17.
2. The previous Groovy/Gradle startup failure is no longer the active blocker.
3. The next failure happens during GradleRIO plugin initialization.

New blocker discovered:

1. `vendordeps/WPILibOldCommands.json` is rejected as invalid for 2026.
2. The exact error is:
   `Vendor Dependency WPILib-Old-Commands has invalid year null. Expected to be 2026.`
3. This means the project has progressed past the wrapper/toolchain failure and is now blocked by stale vendordep metadata.

Assessment of the blocker:

1. The current source tree does not appear to use WPILib old-command classes.
2. The local 2026 WPILib installation includes `WPILibNewCommands.json`, not the old-command vendordep.
3. The most likely clean path is to remove the stale vendordep and continue, but this should be recorded as an intentional migration decision.

### 2026-09-04 Vendordep Cleanup Pass

Step 1:

1. Removed `vendordeps/WPILibOldCommands.json`.
2. Re-ran `./gradlew build`.
3. Result: the build advanced past the old-command vendordep and failed next on the REV Color Sensor vendordep.

Step 2 findings:

1. The new error was:
   `Vendor Dependency REVColorSensorV3 has invalid year null. Expected to be 2026.`
2. Code scanning showed the REV color sensor type only appears in `weelspinner/ColorManager.java`.
3. `ColorManager` is not used by the active robot flow.
4. `Robot.java` only had an unused import and a commented field related to `ColorManager`.

Assessment:

1. Carrying the REV Color Sensor vendordep forward would preserve unused code, not robot behavior.
2. Removing the unused vendordep and dead code is consistent with the Stage 1 goal of preserving behavior while eliminating unsupported configuration.

Planned next actions:

1. Remove the unused REV Color Sensor vendordep.
2. Remove the isolated dead code that requires the REV class.
3. Re-run the build and capture the next blocker.

### 2026-09-04 Project Metadata Recovery

Build result after vendordep cleanup:

1. `./gradlew build` advanced past vendordep validation.
2. The next failure was:
   `Could not find team number. Make sure either one is passed in, or the team number is set in the wpilib_preferences.json file.`

Findings:

1. This repository did not contain a `.wpilib/` directory.
2. The local 2026 WPILib templates include `.wpilib/wpilib_preferences.json` as part of normal project structure.
3. This blocker is about missing project metadata, not source compatibility.

Action taken:

1. Added `.wpilib/wpilib_preferences.json`.
2. Set `projectYear` to `2026`.
3. Set `currentLanguage` to `java`.
4. Set `teamNumber` to `2126` based on the repository name.

Assessment:

1. Adding the missing metadata does not change robot behavior.
2. This brings the project closer to a normal 2026 WPILib project layout.

Planned next action:

Re-run the build and capture the first source-level or dependency-resolution compatibility failure after project metadata is restored.

### 2026-09-04 Source Compatibility Pass

First source-level failures:

1. The 2026 WPILib Java API no longer provides `edu.wpi.first.wpilibj.SpeedController`.
2. The 2026 WPILib Java API no longer provides `edu.wpi.first.wpilibj.SpeedControllerGroup`.
3. The 2026 motor controller classes are now in `edu.wpi.first.wpilibj.motorcontrol`.
4. Pneumatics constructors now require `PneumaticsModuleType`.
5. `CameraServer.getInstance()` is no longer the correct usage pattern for this project.
6. `Compressor.start()` is no longer the correct API for the migrated code path.

Actions taken:

1. Replaced `SpeedController` references with `MotorController`.
2. Replaced direct WPILib `MotorControllerGroup` usage with a small local `DualMotorController` compatibility wrapper to avoid deprecation warnings.
3. Updated `Talon` imports to `edu.wpi.first.wpilibj.motorcontrol.Talon`.
4. Updated `DoubleSolenoid` and `Compressor` construction to use `PneumaticsModuleType.CTREPCM`.
5. Replaced compressor activation calls with `compressor.enableDigital()`.
6. Replaced `CameraServer.getInstance().startAutomaticCapture(0)` with `CameraServer.startAutomaticCapture(0)`.
7. Applied the matching `MotorController` test updates in the test code.

Behavior note:

These changes were limited to API compatibility and support code. No intentional teleop or autonomous behavior changes were introduced.

### 2026-09-04 Stage 1 Build Outcome

Final verification command:

1. `./gradlew build`

Observed result:

1. Main code compiled successfully.
2. Test code compiled successfully.
3. All existing tests passed.
4. The earlier JVM class-sharing warning during tests was eliminated by adding `-Xshare:off` to the test JVM arguments.
5. The build completed successfully with no build-time errors or warnings.

Stage 1 outcome:

Stage 1 is complete. The project now builds successfully against the 2026 WPILib toolchain without intentional robot behavior changes.

## Student Notes

This stage is the likely teaching entry point because it creates a working 2026 project before the architectural conversion begins.
