# Presentation review log

This file tracks changes for the hand-edited master presentation. Do not apply these changes to an older generated deck. Use slide titles as the primary reference because slide numbers can change.

## Open changes

No open changes are currently recorded.

## Applied in consensus v29

| Area | Slide or location | Applied change | Reason |
|---|---|---|---|
| Presentation scope | Title slide | Changed the subtitle to "A Student's Guide to Robot Hardware and WPILib Command-Based Architecture." | The previous subtitle could imply that the presentation teaches the Java language. |
| Presentation scope | Roadmap slide | Added the visible statement: "We will explain what the robot code does, but this presentation does not teach Java syntax." Added the same expectation to the presenter notes. | Students should understand the presentation's scope before the first code example. |

## Applied in consensus v28

| Area | Slide or location | Applied change | Reason |
|---|---|---|---|
| TimedRobot introduction | Before "Running the Belt Motor in TimedRobot" | Added "TimedRobot runs the robot program." It explains that WPILib supplies the repeating loop and calls the methods that students fill in. | Students now know who calls `teleopPeriodic()` before they see it in the first hardware demonstration. |
| TimedRobot methods | Before "Running the Belt Motor in TimedRobot" | Added "TimedRobot calls methods for the current mode." It introduces `robotInit()`, `robotPeriodic()`, and the four mode-specific periodic methods. The slide points out that the hardware demonstrations use `teleopPeriodic()`. | Students can connect Driver Station mode selection to the method that runs in each demonstration. |
| Presenter notes | Both TimedRobot slides | Added teaching prompts, transitions, the relationship between `TimedRobot` and the later command scheduler, and links to the official WPILib API documentation. | The slide faces stay brief while the instructor has enough detail to explain the loop accurately. |

## Applied in consensus v27

| Area | Slide or location | Applied change | Reason |
|---|---|---|---|
| Source-code accuracy | "Robot.java calls the scheduler every cycle" | Replaced the shortened teaching excerpt with the complete `robotPeriodic()` method exactly as it appears in `Robot.java`, including the method documentation and inline comment. Changed the slide to a full-width code panel so both comment lines remain on one line. | Code slides should use the real project source unless the slide clearly labels an excerpt. |

## Applied in consensus v26

| Area | Slide or location | Applied change | Reason |
|---|---|---|---|
| Scheduler entry point | After "BeltInCommand runs from button press to release" | Added "Robot.java calls the scheduler every cycle." The slide shows the project's `robotPeriodic()` method calling `CommandScheduler.getInstance().run()` and explains the result in beginner-level language. Detailed presenter notes connect the earlier direct `TimedRobot` demos to the finished command-based program. | Students now see where each scheduler pass begins before learning what the scheduler does during that pass. |
| Sequence | Command-based section | Placed the new `Robot.java` slide immediately before "The scheduler checks the robot every 20 ms." | The code that calls the scheduler now appears before the explanation of the scheduler's work. |

## Applied in consensus v25

| Area | Slide or location | Applied change | Reason |
|---|---|---|---|
| Flow and subsystem example | After "Subsystems and commands have different jobs" | Moved and retitled the existing subsystem slide as "`BeltSubsystem` provides methods for controlling the belt." Expanded its presenter notes to explain the private motor controller, constructor, `run(speed)`, `stop()`, and the meaning of API in concrete terms. | Students now see the subsystem methods before seeing a command call them. |
| Flow and command example | After "The scheduler calls four methods as a command runs" | Moved and retitled the existing command slide as "`BeltInCommand` runs from button press to release." Expanded its presenter notes to connect the constructor, `execute()`, `isFinished()`, and `end()` to the button-held behavior. | Students now apply the command-method lesson to real project code before learning the detailed scheduler loop. |
| Sequence | Command-based section and project-source section | Reordered the section as: subsystem and command roles, `BeltSubsystem`, button bindings, command methods, `BeltInCommand`, 20 ms scheduler, command conflict, conflict solution, and button-release behavior. Removed the two code slides from their later project-source positions by moving rather than duplicating them. | Each code example follows the concept it demonstrates, and the repository section no longer repeats those examples. |
| Wording | Command roles and repository overview | Replaced the remaining student-facing uses of "lifecycle" with plain descriptions of the methods the scheduler calls. | The official term is accurate, but the simpler wording better fits this introductory class. |

## Applied in consensus v24

| Area | Slide or location | Applied change | Reason |
|---|---|---|---|
| Wording | Physical Hardware Inventory & Actuator Channel Map | Replaced the pulse-width description with: "Motor control: The roboRIO tells each motor controller how fast and which direction to run." | Pulse widths, open-loop control, and commanded voltage were unnecessary details at this early point in the introductory class. |
| Terminology and hardware context | Hardware inventory, hardware connections, presenter notes, and Constants.java excerpt | Removed the unused `PDB` constant and the unverified CAN ID 1 claim. Renamed the physical component Power Distribution Panel or PDP. Added the PDP CAN termination explanation to the hardware-connections notes. | The PDP's electrical CAN-bus role is separate from software configuration in this project. |
| Flow and wording | How Button A starts the belt | Kept the slide before the command-based introduction and rewrote its steps and notes in generic terms. Command-based class names now begin on the following slide. | Students can follow the familiar controller-to-motor behavior before learning commands, subsystems, bindings, and the scheduler. |
| Sequence | Command-based section | Moved the two belt-conflict slides after the slides that explain subsystem and command roles, button bindings, the command lifecycle, and the scheduler. | Students now learn each required idea before applying it to command conflicts and `addRequirements`. |
| Code layout | Constants.java keeps settings in one place | Removed the blank line left by the deleted `PDB` constant so the final Java statement no longer touches the panel border. | The code remains readable without shrinking the font or wrapping a Java statement. |

## Applied in consensus v23

| Area | Slide or location | Needed change | Reason | Source or reference |
|---|---|---|---|---|
| Visual | Driver Station USB Setup & Live Telemetry Diagnostics | Restore the Driver Station USB-tab screenshot. Keep enough surrounding interface visible for students to recognize the screen and its controller indicators. | The screenshot disappeared during the NotebookLM-to-consensus rebuild. The slide needs the real interface because students must find and use it on the Driver Station. | The image remains embedded on slide 12 of `output/presentations/2020-FRC-Robot-Code-Architecture-and-Implementation-Guide-classroom-v3.pptx`. |
| Flow | After the title slide | Add a short student-facing roadmap that previews the progression: identify the hardware, control it directly, read controller input, organize the behavior with commands and subsystems, trace the repository, and debug common mistakes. | Students need to know how the early hardware demonstrations connect to the later architecture material. | Presentation flow review, October 7, 2026. |
| Flow | Two Commands Request the Belt through Subsystem Requirements & Hardware Exclusivity | Introduce the subsystem and command roles before explaining `addRequirements(beltSubsystem)`. Recommended order: conflict, subsystem and command roles, requirements, trigger bindings, lifecycle, scheduler. | Students need definitions for `command` and `subsystem` before learning how the scheduler reserves a subsystem. | Presentation flow review, October 7, 2026. |
| Context | Button A reaches PWM port 5 | Add a brief cue: "Follow the signal from the button to the motor for now. We will explain each software layer next." | This trace uses `RobotContainer`, `BeltInCommand`, and `BeltSubsystem` before the deck defines them. | Presentation flow review, October 7, 2026. |
| Framing | Electrical Wiring & Pneumatic Routing Schematic | Present the schematic as a summary of how the direct hardware demonstrations connect to the robot. | Its current position follows the demonstrations, so it works better as consolidation than as prerequisite material. | Presentation flow review, October 7, 2026. |
| Speaker notes | Transition from direct tests to controller and command-based code | State the relationship plainly: direct output proves the hardware, controller polling makes the mechanism usable, and competing actions create the coordination problem that command-based code solves. | The deck needs an explicit bridge between the direct `TimedRobot` examples and the architecture section. | Presentation flow review, October 7, 2026. |
| Wording | Reading Controller Inputs in TimedRobot | Replace "Polling Scalability Bottleneck" with "More buttons make this code complicated quickly." Rewrite the supporting sentence in the same beginner-friendly language. | New students do not yet know `polling`, `scalability`, or `bottleneck` as programming terms. The current heading hides a simple idea behind jargon. | Classroom audience review, October 7, 2026. |
| Content and wording | Control Flow Example | Replace the low-level signal trace with a beginner-level five-step explanation. Suggested title: "How Button A Starts the Belt." Suggested steps: 1. The operator presses Button A. 2. Driver Station sends the button state to the robot. 3. `RobotContainer` connects Button A to `BeltInCommand`. 4. `BeltInCommand` tells `BeltSubsystem` to run the belt. 5. `BeltSubsystem` sends an output through PWM port 5 and the belt motor turns. Remove HID reports, UDP broadcasts, HAL, FPGA, NetComm, daemon, pulse widths, and voltage from the slide and notes. | The slide's purpose is to establish the path from a button press to a motor action. Low-level communication and FPGA details obscure that path and introduce terms the class does not need. The final voltage statement also oversimplifies motor-controller output. | Classroom audience review, October 7, 2026. |
| Flow and context | After slide 12 and before the command-conflict slides | Add the introductory slide "Why use command-based robot code?" Briefly explain how subsystems, commands, button bindings, the scheduler, and conflict handling give each part of the program a clear job. | The deck currently changes from direct hardware control to command-based code without explaining why the project uses this structure. Students need the overall purpose before the deck teaches the individual pieces. | Presentation flow review, October 7, 2026. |

## Review priorities

For each new version, review these items in order:

1. Speaker notes give the instructor the missing explanation without repeating the slide.
2. Slide titles name the subject or state the supported takeaway.
3. Each slide defines a concept before later slides depend on it.
4. Code colors distinguish Java keywords, types, methods, constants, strings, and comments consistently.
5. Screenshots and diagrams remain present after content merges or rebuilds.

## Approved design decisions

- Treat unwanted wrapping of Java statements as a layout defect. Keep each statement on one line unless the source code intentionally splits it.
- Side-by-side code and explanation panels may overlap slightly. Give the code panel enough width to preserve indentation, complete statements, and syntax coloring.
- Place the explanation panel above the edge of the code panel when the overlap helps connect the two areas visually. Do not cover code, line callouts, or the code panel's readable margin.
- Prefer a small overlap to shrinking the code font or wrapping Java statements. If more room is still needed, shorten the surrounding explanation or move it into the speaker notes before reducing code readability.

## Audience and wording rules

- Write slide text for students who are new to programming and WPILib.
- Avoid unnecessarily complex wording throughout this introductory class. Use the simplest accurate words and sentence structure.
- State the concrete effect before introducing its technical name. For example, use "More buttons make this code complicated quickly" before teaching terms such as polling or scalability.
- Use a technical term only when the lesson explains that term or students need it to read the code and documentation.
- Prefer words students would use when describing what they see the robot or program do.
- Do not add low-level implementation details merely because they are technically related. Include a detail only when it helps students understand or change the project code.

## Draft sequence: the belt-command conflict and its solution

Split this concept across two slides. Students first predict the problem. Reveal the scheduler solution on the next slide.

### Slide 1: What if two commands control the belt?

Keep the purpose of the existing problem slide, but remove its solution panel and simplify the wording.

**Slide face**

- Button A starts `BeltInCommand` and requests inward motion.
- Button B starts `BeltOutCommand` and requests outward motion.
- Both commands send instructions to the same belt motor.

**Question:** What happens if the operator presses both buttons?

Show both commands pointing to the same `BeltSubsystem` or belt motor. Leave the question unanswered on the slide.

**Speaker notes**

Ask: "What should happen if the operator presses A for belt in and B for belt out at the same time?"

Give students time to answer. They may say that the motor stops, one button wins, or the robot breaks. Explain that Java does not automatically know these actions control the same motor.

Without subsystem requirements, both commands could run during the same scheduler cycle. `BeltInCommand` could request a positive belt speed, and `BeltOutCommand` could request a negative belt speed. The motor controller keeps the last value written during that cycle. The result depends on which command writes last. The robot may appear to work, but a code-order change can reverse the result.

End with: "Command-based programming gives us a way to declare this conflict before the commands run."

### Slide 2: Only one command controls the belt at a time

**Slide face**

```java
addRequirements(beltSubsystem);
```

1. Both commands say they need `BeltSubsystem`.
2. The scheduler detects that the subsystem is already in use.
3. In this project, the new command interrupts the command that is running.
4. The interrupted command stops the belt before the new command starts.

**Takeaway:** The scheduler allows only one command to use `BeltSubsystem` at a time.

Place the scheduler between the two commands and `BeltSubsystem`. Highlight `addRequirements(beltSubsystem);` as the line that gives the scheduler the information it needs.

**Speaker notes**

Point to this line in both command constructors:

```java
addRequirements(beltSubsystem);
```

Explain it in plain language: "This command tells the scheduler that it needs control of the belt subsystem while it runs."

Walk through the real project behavior:

1. The operator presses A. The `whileTrue` binding schedules `BeltInCommand`.
2. The scheduler starts `BeltInCommand` and records that it is using `BeltSubsystem`.
3. The operator presses B while A is still held. The B binding asks the scheduler to start `BeltOutCommand`.
4. The scheduler sees that both commands require `BeltSubsystem`. It does not run them together.
5. These commands use WPILib's default interruption behavior, so the scheduler interrupts `BeltInCommand`.
6. The scheduler calls `BeltInCommand.end(true)`. This project's `end` method calls `beltSubsystem.stop()`.
7. After the first command ends, the scheduler starts `BeltOutCommand`, which runs the belt in the opposite direction.

Stress the benefit: the scheduler does not guess which motor direction is correct. It enforces the rule that only one command can control the belt subsystem at a time. The command code still defines what each action does.

Connect the idea to the rest of the robot. The belt and roller can run together because they are different subsystems. `BeltInCommand` and `BeltOutCommand` cannot run together because both require `BeltSubsystem`.

Ask: "Could the belt command and roller command run at the same time? Why?" The expected answer is yes, because they require different subsystems.

Instructor note: if you demonstrate the conflict by holding A and then pressing B, release both buttons before repeating the demonstration. A `whileTrue` binding does not restart an interrupted command while its button remains held. The operator must release and press the button again.

## Draft slide: Why use command-based robot code?

Insert this slide after slide 12 and before the two-slide belt-command conflict sequence.

### Slide face

**Title:** Why use command-based robot code?

**Opening statement:** A complete robot has many mechanisms and controls. Command-based code gives each part of the program a clear job.

**We organize the robot code:**

- **Subsystems** contain the code that controls one mechanism, such as the belt.
- **Commands** describe actions, such as running the belt inward.
- **Button bindings** connect controller buttons to commands.

**WPILib manages the commands:**

- **The scheduler** starts, runs, and stops commands.
- The scheduler also handles conflicts so that two commands do not control the same subsystem at once.

**Takeaway:** Instead of one large block of robot code, each responsibility has a clear place.

Use one simple visual that groups the five ideas around the robot or shows how they work together. Do not show Java code, lifecycle method names, scheduler timing, or detailed class relationships on this introductory slide. Later slides teach those details.

### Speaker notes

Transition from the hardware section: "So far, we have controlled each device directly. That is a good way to prove that the wiring, ports, and basic code work. Our finished robot has a drivetrain, a belt, a roller, a climber, and two controllers. Putting all of that behavior into one large `teleopPeriodic()` method would become difficult to read and change."

Explain that command-based programming is WPILib's way of organizing a robot program. It does not change how a motor or controller works. It gives each part of the program a clear job.

Introduce the five ideas briefly:

1. A subsystem contains the code for one mechanism. `BeltSubsystem` controls the belt motor.
2. A command describes one action. `BeltInCommand` means "run the belt inward."
3. A button binding connects a controller input to an action. In this project, the operator's A button starts `BeltInCommand`.
4. The scheduler runs the commands. It starts them, calls them while they are active, and stops them when they end.
5. The scheduler also handles conflicts. If two commands need the same subsystem, it does not let both control that subsystem at once.

Use the belt as a quick complete example: "The A button binding starts `BeltInCommand`. That command asks `BeltSubsystem` to run the motor. The scheduler manages when that command runs."

Tell students that this slide is only the big picture. The next slides will examine each part using the belt code.

Ask: "Why might we want the scheduler to know that two commands both use the belt?" Accept answers about preventing opposite instructions or keeping the motor under one command's control.

End with: "Now we will look at a specific problem this organization solves: two commands asking to control the belt at the same time."

## Approved revision: How Button A starts the belt

Keep this slide in its current position before the command-based introduction.

### Slide face

**Title:** How Button A starts the belt

1. Button A is pressed.
2. Driver Station sends the button state.
3. The robot program sees that A is pressed.
4. The program runs the code that moves the belt inward.
5. That code sends output to the belt motor on PWM port 5.

Use these short labels across the top:

1. Button A pressed
2. Driver Station sends input
3. Robot code sees input
4. Belt code runs
5. Belt motor turns

End the slide with: "Next, we will see how command-based programming organizes these jobs."

Do not introduce `RobotContainer`, `BeltInCommand`, `BeltSubsystem`, button bindings, or the scheduler on this slide. The following slide introduces those terms.

### Speaker notes

This is the same basic process we saw in the `TimedRobot` demonstration. The operator presses Button A, and Driver Station sends that information to the robot. The robot program sees that the button is pressed and runs the code that moves the belt inward. That code sends an output through PWM port 5 to the belt motor controller.

A larger robot needs to repeat this process for many buttons and mechanisms. If we put every button check and motor instruction inside `teleopPeriodic()`, the method will become difficult to manage. The next slide introduces the way command-based programming divides these jobs into smaller, organized parts.

## Working agreement

- `output/presentations/2020-FRC-Robot-Code-Architecture-and-Implementation-Guide-consensus-v30.pptx` is the current master.
- `output/presentations/2020-FRC-Robot-Code-Architecture-and-Implementation-Guide-consensus-v29.pptx` remains the checkpoint from before slide 6 clarified that TimedRobot callback overrides are optional.
- `output/presentations/2020-FRC-Robot-Code-Architecture-and-Implementation-Guide-consensus-v28.pptx` remains the hand-edited checkpoint from before the Java-language scope statement was added.
- `output/presentations/2020-FRC-Robot-Code-Architecture-and-Implementation-Guide-consensus-v27.pptx` remains the checkpoint from before the two TimedRobot introduction slides were added.
- `output/presentations/2020-FRC-Robot-Code-Architecture-and-Implementation-Guide-consensus-v26.pptx` remains the checkpoint containing the shortened `robotPeriodic()` excerpt.
- `output/presentations/2020-FRC-Robot-Code-Architecture-and-Implementation-Guide-consensus-v25.pptx` remains the checkpoint from before the `robotPeriodic()` scheduler-entry slide was added.
- `output/presentations/2020-FRC-Robot-Code-Architecture-and-Implementation-Guide-consensus-v24.pptx` remains the checkpoint from before the subsystem and command code slides were moved into the command-based teaching sequence.
- `output/presentations/2020-FRC-Robot-Code-Architecture-and-Implementation-Guide-consensus-v23_drm.pptx` remains the hand-edited source checkpoint used to create v24.
- `output/presentations/2020-FRC-Robot-Code-Architecture-and-Implementation-Guide-consensus-v23.pptx` remains the generated checkpoint from before the v23 hand edits.
- The hand-edited v22 presentation remains the earlier checkpoint.
- The presenter notes are part of the master content. Before reviewing or editing a new version, inspect the notes on every slide and preserve the user's revisions.
- Review each slide face together with its presenter notes. The notes may supply context intentionally omitted from the student-facing slide.
- Codex records findings here and does not edit the `.pptx` unless requested.
- When the hand edits are complete, review that exact file and apply only the remaining open items.

## Applied in consensus v30

- Revised slide 6 to state that teams override only the `TimedRobot` methods their robot needs.
- Kept the student-facing distinction between whole-robot methods and the periodic method for the currently selected Driver Station mode.
- Corrected the earlier wording that implied every mode-specific periodic method runs only while enabled; `disabledPeriodic()` runs while disabled.
- Preserved the Driver Station mode image and added a direct transition to the upcoming `teleopPeriodic()` hardware demonstrations.
- Expanded the presenter notes with the distinction between optional callbacks and the main loop supplied by `TimedRobot`, the exact purpose of the methods used in this project's `Robot.java`, and two classroom check questions.
