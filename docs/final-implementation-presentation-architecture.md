# Final implementation presentation architecture

This file defines the required structure for the 2020 robot final-implementation presentation. Future revisions must preserve this structure unless the user explicitly changes it.

## Four-pass structure

Each pass covers the complete robot at a greater level of detail. Do not flatten the presentation into independent topic sections.

### Pass 1: Physical robot

Introduce what the finished robot contains and what the operators can control.

- Major mechanisms and actuators
- roboRIO PWM connections
- roboRIO-to-PCM CAN connection
- Driver and operator Xbox controllers
- High-level input-to-action view

### Pass 2: Software structure

Map the physical robot to the command-based classes.

- `RobotContainer`
- Subsystems and their hardware objects
- Commands as scheduled robot actions
- Controller `Trigger` bindings
- Default commands

Tie this pass to the rule established in the deck: subsystems define how mechanisms operate, and commands define when actions occur.

### Pass 3: Runtime behavior

Explain how WPILib runs the software after construction.

- `TimedRobot` and the 20 ms robot loop
- `robotPeriodic()`
- `CommandScheduler.run()`
- Command lifecycle methods
- Subsystem requirements
- Default-command scheduling and interruption

### Pass 4: Code traces

Trace real actions through the complete system.

- Driver tank drive
- Slow-drive mode
- Belt in and belt out
- Roller operation
- Climber toggle

Each trace follows this path when applicable:

`Xbox input -> Trigger binding -> CommandScheduler -> command -> subsystem method -> WPILib hardware object -> physical output`

## Content rules

- Focus only on the finished implementation. Do not add the porting or migration process.
- Use WPILib terminology exactly. Use `subsystem`, `command`, `Trigger`, `requirement`, `default command`, and `CommandScheduler` as WPILib defines them.
- Keep all four pass labels visible in the slide sequence.
- A revision may add, remove, or reorder slides inside a pass. It must not remove a pass or convert the deck into a single linear topic sequence without explicit approval.
- Speaker notes may add detail, but the visible slides must still communicate the progression between passes.

## Revision check

Before exporting a new deck, confirm all four statements:

1. Pass 1 explains the physical robot and operator controls.
2. Pass 2 maps the robot to command-based classes.
3. Pass 3 explains the runtime and scheduler.
4. Pass 4 traces real actions through the complete implementation.
