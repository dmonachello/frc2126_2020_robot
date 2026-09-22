# Final implementation presentation architecture

This file defines the required structure for the 2020 robot final-implementation presentation. Future revisions must preserve these four questions unless the user explicitly changes them.

## Four-question structure

The presentation explains the finished robot by answering four questions in order.

### 1. What does the robot do?

Begin with the robot's physical jobs and the way the two operators control them.

- Drive and steer with tank drive
- Move game pieces forward or backward with the ball belt
- Score game pieces with the roller
- Raise or lower the climber arms
- Separate driver and operator Xbox controller responsibilities

### 2. What are the major subsystems?

Divide the robot by physical mechanism and use WPILib's term `subsystem`.

- `DriveSubsystem`
- `BeltSubsystem`
- `RollerSubsystem`
- `ClimberSubsystem`

Each subsystem owns the hardware objects and methods for one mechanism.

### 3. How does the hardware work?

Explain how commands from the roboRIO reach the physical actuators. Begin with
the general idea that a hardware ID identifies a control point. Use the 2020
wiring as the working example without turning the section into a comparison
between old and current hardware.

- The driver and operator Xbox controllers connect through USB 0 and USB 1.
- On this 2020 robot, the motor-controller IDs are roboRIO PWM port numbers.
  Mention that fact, but emphasize what the IDs do rather than the age of the
  interface.
- The roboRIO communicates with CTRE PCM 0 over CAN; the PCM controls the
  pneumatic valves. The individual solenoids are not CAN devices.
- PCM solenoid channel pairs 0/1 and 2/3 control the climber.
- At the end of the presentation, explain that the team's current motor
  controllers and pneumatics module use CAN rather than PWM. Do not imply that
  every device on a modern robot uses CAN.
- Use one closing example to compare a PWM port ID with a CAN device ID. The
  identifier still selects a control point, and the subsystem still owns the
  resulting device object.

### 4. How does the software separate, identify, and talk to each piece?

Explain the command-based ownership and runtime path.

- `RobotContainer` creates controllers and subsystems, then configures default commands and `Trigger` bindings.
- Subsystems own the WPILib hardware objects and expose mechanism methods.
- Commands define robot actions by calling subsystem methods.
- `Trigger` bindings identify which controller input schedules each command.
- `CommandScheduler` polls triggers, schedules commands, enforces subsystem requirements, and runs command lifecycle methods.
- `robotPeriodic()` calls `CommandScheduler.run()` every 20 ms, about 50 times per second.
- End-to-end traces connect Xbox input to a command, subsystem, WPILib hardware object, and physical output.

## Content rules

- Focus only on the finished implementation. Do not add the porting or migration process.
- Use WPILib terminology exactly. Use `subsystem`, `command`, `Trigger`, `requirement`, `default command`, and `CommandScheduler` as WPILib defines them.
- Use the durable path `controller input -> command -> subsystem method -> device
  API -> mechanism`. End primary runtime traces at the device API or mechanism.
  Keep the PWM-to-CAN comparison out of those traces and place it in the closing
  hardware example.
- Describe object creation precisely: `Robot` creates `RobotContainer`;
  `RobotContainer` creates subsystem objects; subsystem constructors create and
  own device objects; `RobotContainer` creates command instances, passes the
  required subsystem references, and configures `Trigger` bindings and default
  commands.
- Keep the four questions visible in the slide sequence.
- Do not replace these questions with separate sections for software structure, runtime behavior, or code traces. Those details belong under question four.
- Speaker notes may add detail, but the visible slides must still show how each detail answers its section's question.

## Revision check

Before exporting a new deck, confirm all five statements:

1. The opening explains what the robot does.
2. The next section names and defines the four major subsystems.
3. The hardware section teaches IDs as control-point identifiers, mentions that
   this robot's motor IDs are roboRIO PWM ports, and explains the PCM module and
   channel IDs accurately.
4. The software section explains ownership, object construction, bindings, scheduling, lifecycle, requirements, and complete control paths.
5. A closing example shows that a CAN device ID replaces the PWM port ID on
   modern motor controllers without changing the subsystem and command pattern.
