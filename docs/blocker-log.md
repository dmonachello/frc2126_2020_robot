# Blocker Log

Date: 2026-09-04

Use this file to record migration blockers that pause work.

## Entry Template

### Blocker ID

Status:

Stage:

Date:

Problem:

Impact:

Options Considered:

Decision:

Student Notes:

## Active Blockers

### B001

Status: Resolved

Stage: Stage 1

Date: 2026-09-04

Problem:

The untouched 2020 build stack does not start successfully under the current Java 17 runtime on this machine.

Observed evidence:

1. The project uses Gradle `6.0.1` from `gradle/wrapper/gradle-wrapper.properties`.
2. The project uses `edu.wpi.first.GradleRIO` version `2020.3.2` from `build.gradle`.
3. Running `./gradlew build` fails before project evaluation with Groovy initialization errors:
   `NoClassDefFoundError: Could not initialize class org.codehaus.groovy.vmplugin.v7.Java7`
4. The machine Java runtime is `OpenJDK 17.0.12`.

Impact:

Stage 1 cannot proceed to code compilation, dependency resolution, or warning cleanup until the build tooling is updated enough to run under the current environment.

Options Considered:

1. Run the old 2020 project under an older JDK temporarily.
2. Begin the controlled upgrade to the 2026 GradleRIO and WPILib toolchain.

Decision:

Use the locally installed 2026 WPILib project templates as the primary reference and upgrade the build stack intentionally as part of Stage 1.

Student Notes:

This is the first example of a migration blocker caused by tooling age rather than robot logic. The code has not failed yet; the build system has failed first.

### B002

Status: Resolved

Stage: Stage 1

Date: 2026-09-04

Problem:

After upgrading the wrapper and GradleRIO configuration toward the local 2026 template, GradleRIO now rejects `vendordeps/WPILibOldCommands.json` during plugin initialization.

Observed evidence:

1. Running `./gradlew build` now reaches GradleRIO plugin application.
2. The build fails with:
   `Vendor Dependency WPILib-Old-Commands has invalid year null. Expected to be 2026.`
3. The local 2026 WPILib install includes `WPILibNewCommands.json` rather than `WPILibOldCommands.json`.
4. Source scanning found no usage of old-command classes in `src/main/java` or `src/test/java`.

Impact:

The build cannot proceed to Java compilation until the stale vendordep is removed or replaced with a valid 2026-supported alternative.

Options Considered:

1. Remove `WPILibOldCommands.json` because the current source tree does not appear to use it.
2. Replace it with `WPILibNewCommands.json` if a dependency entry is still needed for later command-based work.
3. Keep it in place and block all further progress.

Decision:

Pause here for review. The most likely next step is to remove the stale vendordep because it appears unused, but that should be called out explicitly as a migration decision.

Student Notes:

This is a good example of a dependency that can block a modern build even when the robot code does not actually reference it. Part of migration work is distinguishing real dependencies from leftover configuration.

Resolution:

`vendordeps/WPILibOldCommands.json` was removed after confirming no source usage of old-command classes.

### B003

Status: Resolved

Stage: Stage 1

Date: 2026-09-04

Problem:

After removing the old-command vendordep, GradleRIO rejected `vendordeps/ColorSensorV3.json` as invalid for 2026.

Observed evidence:

1. Running `./gradlew build` failed with:
   `Vendor Dependency REVColorSensorV3 has invalid year null. Expected to be 2026.`
2. Source scanning found `com.revrobotics.ColorSensorV3` only in `src/main/java/frc/robot/weelspinner/ColorManager.java`.
3. `ColorManager` is not constructed or called by the active robot code.
4. `Robot.java` only had an unused import and a commented-out field for `ColorManager`.

Impact:

The build could not proceed until the stale REV Color Sensor vendordep was either upgraded or removed.

Options Considered:

1. Find a 2026-compatible REV Color Sensor vendordep and keep the dead code in place.
2. Remove the unused vendordep and the isolated dead code that required it.
3. Pause for a broader feature review.

Decision:

Remove the unused color-sensor vendordep and the dead `ColorManager` code because it is not part of active robot behavior.

Student Notes:

This is a useful cleanup case: not every library listed in an old project is still part of the robot. Migration work should confirm whether code is active before carrying a dependency forward.

User confirmation:

The color sensor did not make it into the final 2020 robot, so removing the related vendordep and dead code is consistent with the known robot behavior.

### B004

Status: Resolved

Stage: Stage 1

Date: 2026-09-04

Problem:

After the stale vendordeps were removed, the build failed because the project did not contain WPILib team/project metadata.

Observed evidence:

1. Running `./gradlew build` failed with:
   `Could not find team number.`
2. The repository did not contain a `.wpilib/wpilib_preferences.json` file.
3. The local 2026 WPILib templates include `.wpilib/wpilib_preferences.json` as standard project metadata.

Impact:

The build could not continue through GradleRIO configuration without a team number source.

Options Considered:

1. Change the build to use `getTeamOrDefault(...)`.
2. Add the missing `.wpilib/wpilib_preferences.json` file.
3. Pass the team number only on the command line.

Decision:

Add `.wpilib/wpilib_preferences.json` so the project matches normal WPILib structure. Team number `2126` is used as the working value based on the repository name.

Student Notes:

This is a project-metadata issue, not a robot-code issue. Modern WPILib projects expect a small amount of project configuration outside the Java source tree.

Resolution:

Adding the missing `.wpilib` metadata allowed GradleRIO configuration to complete and exposed the first real source-level compatibility issues.
