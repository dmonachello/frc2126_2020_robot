# Current robot implementation presentation outline V2

Audience: a mixed group ranging from first-time programmers to experienced WPILib students  
Format: a shared core, parallel experience-based tracks, and a shared rejoin  
Suggested length: 30 to 35 minutes together, 25 to 35 minutes in tracks, and 10 minutes together at the end  
Primary goal: students can trace one operator input through a binding, command, subsystem, hardware API, and physical mechanism  
Follow-up goal: students can make one guided change to a similar command-based robot program

## Teaching approach

- Begin with physical robot behavior before naming software parts.
- Use the belt as the first example and repeat it until students can trace it without help.
- Introduce only the Java syntax needed to read the next excerpt.
- Keep excerpts short and copy them directly from the repository.
- Read each excerpt aloud in plain language.
- Use consistent colors in the finished deck: one for bindings, one for commands, one for subsystems, and one for hardware calls.
- Give beginners a recognition or tracing question every three or four slides.
- Give experienced students a prediction, design, or failure-mode question on the same slides.
- Treat Slides 13 through 23 as parallel tracks rather than one required linear sequence.

# Shared core: Understanding the code flow

## Slide 1: The robot's four mechanisms

- The drivetrain moves and steers the robot.
- The belt transports game pieces.
- The roller ejects game pieces.
- The pneumatic climber changes the arm position.

Suggested visual: one current robot photo with the four mechanisms labeled.

Learning target: connect each visible mechanism to one software subsystem by the end of the session.

## Slide 2: Current operator controls

| Controller | Input | Physical result |
| --- | --- | --- |
| Driver, USB 0 | Left and right Y axes | Drive and steer |
| Driver, USB 0 | Right bumper, held | Reduce drive speed |
| Operator, USB 1 | A, held | Move the belt inward |
| Operator, USB 1 | B, held | Move the belt outward |
| Operator, USB 1 | X, held | Run the roller |
| Operator, USB 1 | Y, pressed | Change climber position |

Suggested visual: driver and operator Xbox controllers with only the active controls labeled.

Teaching point: every software path begins with a physical action the team wants the robot to perform.

## Slide 3: One button press

Begin without software terms:

1. The operator holds A.
2. The robot starts the belt.
3. The belt continues while A remains held.
4. Releasing A stops the belt.

First code sighting from `RobotContainer.java`:

```java
operatorController.a()
    .whileTrue(new BeltInCommand(beltSubsystem));
```

Read it aloud: while the operator holds A, run the belt-in action using the belt mechanism.

Do not explain every Java symbol yet. Students only need to connect the two lines to the physical behavior.

Beginner check: which controller button starts this behavior?

Experienced prompt: why does `whileTrue` fit this action better than `onTrue`?

## Slide 4: Reading one line of Java

Actual line from `BeltInCommand.java`:

```java
beltSubsystem.run(Constants.Tuning.BELT_SPEED);
```

Break the line into four parts:

| Code | Plain meaning |
| --- | --- |
| `beltSubsystem` | The object representing the belt mechanism |
| `run` | The action requested from that object |
| `Constants.Tuning.BELT_SPEED` | The speed supplied to the action |
| `;` | The end of the Java statement |

Read it aloud: ask the belt subsystem to run at the configured belt speed.

Teaching point: students do not need to memorize Java syntax before they can start recognizing its structure.

## Slide 5: The command scheduler

Actual scheduler call from `Robot.java`:

```java
@Override
public void robotPeriodic() {
  CommandScheduler.getInstance().run();
}
```

- WPILib calls `robotPeriodic()` about every 20 ms.
- The scheduler checks controller triggers.
- It starts, runs, interrupts, and ends commands.
- Commands do not run themselves. The scheduler calls their lifecycle methods.

Plain-language model: the scheduler repeatedly checks what should be running, then gives each active command time to run.

Beginner check: what would happen if the robot never called `CommandScheduler.run()`?

Experienced prompt: what scheduler responsibility prevents two commands from controlling the same mechanism?

## Slide 6: The subsystem hardware layer

Actual excerpt from `BeltSubsystem.java`:

```java
public BeltSubsystem() {
    beltMotor = new Talon(Constants.Hardware.PWM_BELT);
}

public void run(double speed) {
    beltMotor.set(speed);
}
```

- The subsystem creates and owns the belt motor-controller object.
- `run()` calls the WPILib hardware API that changes motor output.
- Hardware access for the belt stays inside `BeltSubsystem`.

Teaching point: the subsystem knows how to operate its hardware.

Beginner check: which line actually changes the motor output?

Experienced prompt: what maintenance problem would appear if commands created their own motor-controller objects?

## Slide 7: The command action layer

Actual excerpt from `BeltInCommand.java`:

```java
@Override
public void execute() {
    beltSubsystem.run(Constants.Tuning.BELT_SPEED);
}

@Override
public void end(boolean interrupted) {
    beltSubsystem.stop();
}
```

- `execute()` requests belt motion while the command runs.
- `end()` requests a safe stop after release or interruption.
- The command does not know which PWM port controls the motor.

Teaching point: the command decides what action to request and when. The subsystem performs the hardware operation.

## Slide 8: Subsystem requirements

Actual constructor from `BeltInCommand.java`:

```java
public BeltInCommand(BeltSubsystem beltSubsystem) {
    this.beltSubsystem = beltSubsystem;
    addRequirements(beltSubsystem);
}
```

- The constructor receives the belt subsystem that the command will use.
- `addRequirements(beltSubsystem)` declares exclusive control of the belt while the command runs.
- The scheduler will not run belt-in and belt-out commands at the same time.
- A command may have no requirement when it does not control a subsystem.

Teaching point: requirements tell the scheduler which mechanism a command controls.

Beginner check: which subsystem does this command control?

Experienced prompt: predict what the scheduler does when `BeltOutCommand` starts while `BeltInCommand` is active.

## Slide 9: Controller bindings

Actual belt bindings from `RobotContainer.java`:

```java
operatorController.a()
    .whileTrue(new BeltInCommand(beltSubsystem));
operatorController.b()
    .whileTrue(new BeltOutCommand(beltSubsystem));
```

- A binding connects a controller input to a command.
- `whileTrue` keeps the command scheduled while the button remains held.
- Releasing the button cancels the command, causing `end()` to stop the motor.

Teaching point: bindings decide when commands start. Commands decide what to request. Subsystems operate the hardware.

## Slide 10: The complete belt path

Trace the action in numbered stages:

1. Operator A activates the `whileTrue` binding.
2. The scheduler starts `BeltInCommand`.
3. `execute()` calls `beltSubsystem.run(...)`.
4. `BeltSubsystem.run()` calls `beltMotor.set(speed)`.
5. The PWM motor controller runs the physical belt.
6. Button release cancels the command and `end()` calls `stop()`.

Use the same colors assigned to bindings, commands, subsystems, and hardware calls.

Student activity: provide the six stages out of order and ask students to arrange them.

Experienced extension: identify which stages are framework behavior and which are team-written code.

## Slide 11: Subsystem boundaries and cooperation

Actual subsystem construction from `RobotContainer.java`:

```java
driveSubsystem = new DriveSubsystem();
climberSubsystem = new ClimberSubsystem();
beltSubsystem = new BeltSubsystem();
rollerSubsystem = new RollerSubsystem();
```

- Each independently controlled mechanism has one subsystem.
- The drivetrain is one subsystem because its four motors create one coordinated motion.
- The belt and roller are separate because either mechanism can run by itself.
- Commands with different requirements can run together.
- A coordinated action can use a command or command group that requires every participating subsystem.

Teaching point: subsystems own mechanisms. Commands coordinate how mechanisms work alone or together.

Beginner check: why are the belt and roller separate subsystems?

Experienced prompt: describe a scoring command that would coordinate both mechanisms and name its requirements.

## Slide 12: Shared-core checkpoint

Ask students to answer without looking back:

1. Which class directly calls `beltMotor.set()`?
2. Which class decides that the belt should run inward?
3. What connects the A button to that action?
4. What repeatedly runs the command lifecycle?
5. Why can the drivetrain and belt run at the same time?

Beginner success criterion: explain the belt path using binding, scheduler, command, subsystem, and hardware.

Experienced success criterion: predict interruption and concurrency from a command's requirements.

# Beginner track: Reading and modifying existing code

## Slide 13: Project map and startup

Actual project structure:

```text
frc.robot
  Robot.java
  RobotContainer.java
  Constants.java
  commands/
  subsystems/
```

Actual startup excerpt from `Robot.java`:

```java
@Override
public void robotInit() {
  robotContainer = new RobotContainer();
  autonomousMode = new AutonomousMode(robotContainer);
}
```

- `Robot` owns lifecycle and scheduler execution.
- `RobotContainer` creates subsystems, commands, and bindings.
- The two folders separate hardware-facing code from robot actions.

## Slide 14: Hardware IDs in context

Actual constant from `Constants.java`:

```java
public static final int PWM_FRONT_LEFT_DRIVE = 0;
```

Actual use in `DriveSubsystem.java`:

```java
leftDriveLeader = new Talon(Constants.Hardware.PWM_FRONT_LEFT_DRIVE);
```

Two other kinds of constants used by the robot:

```java
public static final int DRIVER_CONTROLLER = 0;
public static final double DRIVE_DEADBAND = 0.08;
```

- The number identifies a physical roboRIO PWM output.
- The name records which motor uses that connection.
- The subsystem uses the name and does not repeat the number.
- `Constants` groups hardware connections, operator ports, and tuning values.

Teaching point: introduce a named hardware connection when the subsystem first needs it.

## Slide 15: Drivetrain hardware construction

Actual excerpt from `DriveSubsystem.java`:

```java
leftDriveLeader = new Talon(Constants.Hardware.PWM_FRONT_LEFT_DRIVE);
Talon leftDriveFollower = new Talon(Constants.Hardware.PWM_BACK_LEFT_DRIVE);
rightDriveLeader = new Talon(Constants.Hardware.PWM_FRONT_RIGHT_DRIVE);
Talon rightDriveFollower = new Talon(Constants.Hardware.PWM_BACK_RIGHT_DRIVE);

leftDriveLeader.addFollower(leftDriveFollower);
rightDriveLeader.addFollower(rightDriveFollower);
rightDriveLeader.setInverted(true);
```

- `DriveSubsystem` owns all four drivetrain motor controllers.
- Each rear motor follows the front motor on the same side.
- The right leader handles the physical orientation of the right side.

## Slide 16: Drivetrain action method

Actual excerpt from `DriveSubsystem.java`:

```java
public void drive(double leftSpeed, double rightSpeed) {
    leftDriveLeader.set(leftSpeed);
    rightDriveLeader.set(rightSpeed);
}
```

- The method describes the mechanism action in drivetrain language.
- Commands send two requested speeds without knowing individual motor ports.
- WPILib forwards each leader's value to its follower.

Student prediction: which class should call `drive()` during teleoperated control?

## Slide 17: The default drive command

Actual setup from `RobotContainer.java`:

```java
driveSubsystem.setDefaultCommand(
    new DriveTeleopCommand(
        driveSubsystem,
        this::getLeftDriveValue,
        this::getRightDriveValue,
        driveSpeedMode));
```

- The scheduler uses the default command whenever the drivetrain is free.
- Suppliers allow the command to read current stick values on every scheduler cycle.
- Another command requiring the drivetrain would interrupt this default command.

Teaching point: a default command defines the normal behavior of an otherwise unused subsystem.

## Slide 18: Drive input and execution

Actual input method from `RobotContainer.java`:

```java
private double getLeftDriveValue() {
    return -MathUtil.applyDeadband(
        driverController.getLeftY(),
        Constants.Tuning.DRIVE_DEADBAND);
}
```

Actual execution from `DriveTeleopCommand.java`:

```java
driveSubsystem.drive(
    scale(leftDriveSupplier.getAsDouble()),
    scale(rightDriveSupplier.getAsDouble()));
```

- `RobotContainer` interprets the controller input.
- The command applies the selected speed scale.
- The subsystem sends the results to the hardware.

Guided beginner activity:

1. Find the matching right-stick method.
2. Circle the controller input, deadband constant, command call, and subsystem call.
3. Change the normal drive scale with an instructor and predict the physical effect before running the code.

# Experienced track: Design, coordination, and failure modes

## Slide 19: Drive tuning and slow mode

Actual tuning values from `Constants.java`:

```java
public static final double NORMAL_DRIVE_SCALE = 0.8;
public static final double SLOW_DRIVE_SCALE = 0.4;
public static final double DRIVE_DEADBAND = 0.08;
```

Actual state changes from `SlowDriveCommand.java`:

```java
public void initialize() {
    driveSpeedMode.setSlow(true);
}

public void end(boolean interrupted) {
    driveSpeedMode.setSlow(false);
}
```

- Holding the bumper enables slow mode without interrupting the default drive command.
- Releasing it restores normal scaling.
- `SlowDriveCommand` has no subsystem requirement because it changes shared drive state rather than hardware output.

Experienced design review:

1. Explain why adding `DriveSubsystem` as a requirement would stop slow driving from working.
2. Compare the shared `DriveSpeedMode` object with reading the bumper directly in the drive command.
3. Decide which design you would teach and defend the tradeoff.

## Slide 20: Reusing the motor-subsystem pattern

Actual excerpt from `RollerSubsystem.java`:

```java
public RollerSubsystem() {
    rollerMotor = new Talon(Constants.Hardware.PWM_ROLLER);
}

public void run(double speed) {
    rollerMotor.set(speed);
}

public void stop() {
    run(0);
}
```

- The roller uses the same ownership and action pattern as the belt.
- Separate subsystems allow the belt and roller to operate simultaneously.
- A simple mechanism may need only construction, motion, and stop behavior.

Experienced challenge: determine which drive, belt, and roller commands can run at the same time from their requirements alone.

## Slide 21: Pneumatic subsystem pattern

Actual action from `ClimberSubsystem.java`:

```java
public void extendArms() {
    leftClimberSolenoid.set(Constants.Hardware.SOLENOID_LEFT_OUT);
    rightClimberSolenoid.set(Constants.Hardware.SOLENOID_RIGHT_OUT);
    armsExtended = true;
}
```

Actual command action from `ToggleClimberArmsCommand.java`:

```java
public void initialize() {
    climberSubsystem.toggleArms();
}
```

- The subsystem owns two hardware objects because both belong to one climber mechanism.
- The command requests one position change and then finishes.
- The software assumes the arms start retracted, so the team must verify the physical starting position.

Experienced safety review: identify how the software could learn the real arm position instead of assuming it.

## Slide 22: Coordinating multiple subsystems

Actual independent bindings from `RobotContainer.java`:

```java
operatorController.a()
    .whileTrue(new BeltInCommand(beltSubsystem));
operatorController.x()
    .whileTrue(new RollerCommand(rollerSubsystem));
```

- The current controls let the operator run the belt and roller independently.
- Their commands can run together because they require different subsystems.
- A single scoring action could coordinate both through one command or command group.
- That coordinated action must require both `BeltSubsystem` and `RollerSubsystem`.

Experienced design task: define the lifecycle and interruption behavior of a combined scoring action before writing code.

## Slide 23: Tests and design verification

Actual test excerpt from `TeleopCommandsTest.java`:

```java
command.execute();
command.end(false);

Mockito.verify(beltSubsystem).run(Constants.Tuning.BELT_SPEED);
Mockito.verify(beltSubsystem).stop();
```

- The test checks what the command asks its subsystem to do.
- The mocked subsystem avoids connecting real robot hardware.
- The test covers both motion and safe stop behavior.

Experienced exercise:

1. Write a test for the proposed combined scoring action.
2. Verify that both mechanisms start.
3. Verify that both mechanisms stop after normal completion or interruption.
4. Explain which existing commands the new requirements would interrupt.

# Shared rejoin

## Slide 24: Team recap and similar-robot recipe

1. List the physical mechanisms and operator actions.
2. Give each independently controlled mechanism one subsystem.
3. Put direct motor, solenoid, and sensor calls inside that subsystem.
4. Add short mechanism methods such as `run()`, `stop()`, or `drive()`.
5. Create commands that request those actions.
6. Declare every subsystem requirement.
7. Connect controller inputs to commands in `RobotContainer`.
8. Run the scheduler from `robotPeriodic()`.
9. Add named hardware IDs and tuning values to `Constants`.
10. Test command requests before connecting hardware.
11. Verify motor direction, stop behavior, and physical starting positions on the robot.

Rejoin activity:

- A beginner traces one existing action through the five software roles.
- An experienced student explains one scheduler or design decision without assuming prior Java knowledge.
- Each track demonstrates or describes one change it made.

## Presenter preparation

- Use current mechanism photos beside the related subsystem slides.
- Reveal code in small steps instead of showing a complete class at once.
- Read every new Java construct aloud the first time it appears.
- Keep the four role colors consistent across the shared core and both tracks.
- Print a one-page vocabulary sheet for binding, scheduler, command, requirement, subsystem, and hardware API.
- Give students the repository before the breakout so they can follow the named files.
