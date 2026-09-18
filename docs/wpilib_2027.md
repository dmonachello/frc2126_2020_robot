# WPILib 2027 command frameworks

Commands v2 and Commands v3, with an emphasis on cooperative multitasking

Audience: FRC software mentors and advanced students
Date: September 17, 2026

## Executive summary

WPILib Commands v2 already uses cooperative scheduling. Commands v3 changes how programmers express that cooperation.

In v2, the scheduler repeatedly calls `execute()`. Each call must return promptly. A command stores any state that must survive between calls in fields, timers, composed commands, or state variables.

V3 remains single-threaded and cooperative, but adds a coroutine style. Command code can yield control and later resume at the same point. Sequential robot behavior can therefore look like procedural code without blocking the scheduler.

The shortest comparison is:

> In v2, return to the scheduler and reconstruct your position on the next call.
>
> In v3, yield to the scheduler and resume where you stopped.

V3 is a separate framework, not a silent rewrite of v2. Existing v2 code remains useful. Teams can test v3 without committing to an immediate rewrite.

## 1. Cooperative multitasking

A cooperative scheduler does not interrupt arbitrary command code. Running code must return or yield control.

An FRC robot appears to do many things at once. It drives, operates mechanisms, processes triggers, updates telemetry, and enforces safety rules. In the command framework, these activities normally share one scheduler thread rather than run as independent preemptive threads.

This loop is dangerous:

```java
while (!atTarget()) {
    motor.set(0.3);
}
```

In v2, placing the loop in `execute()` prevents the method from returning. The scheduler cannot run normally.

Moving the same loop into a v3 coroutine does not fix it. The loop must reach `yield()` or another operation that yields control.

V3 keeps cooperative scheduling. It gives programmers a clearer way to express it.

## 2. Commands v2 and lifecycle callbacks

V2 represents a command through lifecycle callbacks for initialization, repeated execution, completion, and cleanup.

```java
public class MoveArm extends Command {
    private final Arm arm;

    public MoveArm(Arm arm) {
        this.arm = arm;
        addRequirements(arm);
    }

    @Override
    public void initialize() {
        arm.set(0.3);
    }

    @Override
    public void execute() {
        // Do a small amount of work, then return.
    }

    @Override
    public boolean isFinished() {
        return arm.atTarget();
    }

    @Override
    public void end(boolean interrupted) {
        arm.stop();
    }
}
```

`execute()` is one slice of work, not the entire task. It must return promptly.

Experienced FRC programmers learn this pattern, but it can surprise students. The obvious procedural solution often looks like this:

```java
startMotor();

while (!atTarget()) {
    // Wait.
}

stopMotor();
```

That code cannot run directly in a cooperative periodic scheduler because it blocks every other scheduled command.

V2 represents multi-step behavior with command composition, command groups, timers, state variables, and fields that retain state between calls. The model works, but one physical sequence may be spread across several callbacks or command objects.

## 3. Commands v3 and continuations

V3 changes how a command preserves its position in an operation.

Conceptually, a v3 command can look like this:

```java
Command moveArm() {
    return arm.run(coroutine -> {
        arm.set(0.3);

        while (!arm.atTarget()) {
            coroutine.yield();
        }

        arm.stop();
    }).named("Move Arm");
}
```

When the command calls `yield()`, it suspends. The scheduler regains control and runs other work. On a later scheduler pass, the command resumes after the yield instead of re-entering the method from the beginning.

Local variables and the current call stack can survive the suspension. That changes how programmers express progress.

### V2

The command records its progress explicitly:

```text
initialize:
    start operation

execute:
    inspect current state
    perform one step
    return

isFinished:
    inspect current state
```

### V3

The command states the sequence directly:

```text
start operation

while operation is incomplete:
    yield

continue with next operation
```

The scheduler is cooperative in both versions. V3 preserves the continuation so the command does not have to reconstruct its position manually.

## 4. How scheduler execution differs

### Commands v2

```text
scheduler.run()

    command A: execute() -> return
    command B: execute() -> return
    command C: execute() -> return

next robot loop

    command A: execute() -> return
    command B: execute() -> return
    command C: execute() -> return
```

Each `execute()` call enters the method normally. If a command has reached step 3, it must represent that fact in persistent state or through composed commands.

### Commands v3

```text
scheduler.run()

    command A: resume -> work -> yield
    command B: resume -> work -> yield
    command C: resume -> work -> yield

next robot loop

    command A: resume after its previous yield
    command B: resume after its previous yield
    command C: resume after its previous yield
```

The scheduler does not interrupt command A at an arbitrary instruction. Command A reaches a suspension point and gives control back.

A poorly written v3 command can still monopolize the robot thread. Long calculations, blocking input or output, and loops without yields all prevent the scheduler from running other work.

## 5. Awaiting child commands

A v3 coroutine can await another command:

```java
return Command.noRequirements(coroutine -> {
    coroutine.await(elevator.toL4());
    coroutine.await(coral.score());
}).named("Score L4");
```

The sequence reads like the robot procedure:

1. Move the elevator.
2. Wait for that command to finish.
3. Score the coral.

Here, "wait" does not mean block the scheduler. The parent suspends while the command framework manages the child.

The scheduler can still see the child. Mechanism ownership, cancellation, telemetry, and command relationships remain part of the framework instead of disappearing inside hand-written blocking logic.

This resembles `async` and `await`, but it does not create a separate operating-system thread for each robot activity.

## 6. Parent and child commands

When one command awaits another, the scheduler can represent their relationship directly:

```text
ScoreCoral
    |
    +-- RaiseElevator
    |
    +-- RunScoringMechanism
```

That relationship helps in two places. First, telemetry can show which part of a larger operation is active. Second, the framework can define what happens to child commands when their parent ends or is cancelled.

On a robot with complex autonomous or assisted-driving routines, that visibility can shorten debugging sessions.

## 7. Mechanism ownership

In v2, a command declares subsystem requirements. The scheduler uses those requirements to stop conflicting commands from controlling the same hardware.

V3 extends the ownership model for nested commands. A v2 composition may reserve requirements for the full composed operation, including periods when one subsystem is idle. Proxy commands and related patterns can relax that reservation, but students must learn another layer of command behavior.

The v3 parent and child model aims to make dynamic ownership more direct. A parent can await child operations while the scheduler tracks the mechanisms involved in the active work.

Teams should test this behavior before porting complex command structures. A v2 requirement pattern may not map directly to v3.

## 8. Scheduler scopes

V3 adds stronger scoping for commands, trigger bindings, defaults, and related scheduler state.

Large robot programs often register behavior globally because global registration is convenient, even when that behavior belongs to one mode or operation. A scoped binding exists only while its containing scope exists. When the scope ends, the scheduler can remove the associated state.

Scopes make these questions easier to answer:

- Is this trigger active now?
- Which default behavior applies in this mode?
- Did a binding outlive the command that created it?
- Is this behavior global or temporary?

For teams with several teleoperated modes and assisted actions, explicit scope can replace hidden global state.

## 9. Priority, suspension, and interruption

V3 gives the scheduler more ways to handle conflicting commands. Instead of reducing every conflict to cancellation or refusal, it introduces priority and suspension.

Suspension is not cancellation:

```text
Cancellation:

running -> cancelled -> finished

Suspension:

running -> suspended -> running again
```

A low-priority command can temporarily yield to a higher-priority command and later resume. That creates a new risk: the old command may resume with stale intent.

During a suspension, the robot may move, sensor values may change, and the operator may choose another action. A resumed command must verify that its assumptions still hold.

Our team should define rules for resumable commands before using suspension widely.

## 10. Scheduler telemetry

V3 places more scheduler state in view, including:

- running and queued commands
- command identity
- parent and child relationships
- mechanism ownership
- priority
- execution timing

Many apparent mechanism failures are ownership failures. When a motor stops, the first question is often, "What is wrong with the motor code?" The better first question may be, "Which command owns this mechanism, and why?"

Scheduler telemetry can answer that question. This fits our bringup and diagnostic practice: expose system state instead of inferring it from symptoms.

## 11. Commands v2 in 2027

The release of v3 does not make v2 obsolete. Teams need to separate normal v2 maintenance from experiments with the v3 programming model.

Existing v2 programs do not gain coroutine behavior. Their scheduler still expects lifecycle callbacks that finish quickly.

A team with a mature v2 robot has no reason to rewrite working code only because v3 exists. Treat v3 as a framework choice and judge it on:

- student readability
- debugging and scheduler visibility
- vendor compatibility
- documentation and examples
- stability during the competition season
- mentor familiarity
- migration cost

Cleaner syntax is attractive, but a competition robot is a bad place to adopt a framework that the team cannot diagnose under pressure.

## 12. What students need to relearn

The main teaching change is the meaning of a command body.

For v2, we teach:

> Never wait inside `execute()`. Check the condition, do a small amount of work, and return.

That rule remains valid because command code must cooperate with the scheduler.

For v3, we can teach a more precise rule:

> Sequential-looking code is allowed, but every long-running path must reach a scheduler suspension point.

Students must know which operations yield and which do not.

This loop cooperates with the scheduler:

```java
while (!sensor.get()) {
    coroutine.yield();
}
```

This loop does not:

```java
while (!sensor.get()) {
}
```

Nor does a yield repair work that already blocked the scheduler:

```java
performHugeCalculation();
coroutine.yield();
```

If `performHugeCalculation()` takes 500 ms, the scheduler waits 500 ms before it reaches the yield.

Coroutines improve code structure. They do not remove real-time constraints.

## 13. Where v3 can simplify robot code

Consider this autonomous mechanism procedure:

1. Home an elevator.
2. Move to a scoring height.
3. Wait for the mechanism to settle.
4. Run the scorer.
5. Wait until a sensor reports that the game piece is gone.
6. Stop the scorer.
7. Return the elevator.

V2 naturally represents this procedure as a command sequence with commands, decorators, waits, and conditions. That is valid command-based programming, but the source can become more complex than the physical procedure.

In v3, a high-level command can follow the procedure directly:

```text
await home elevator
await move to scoring height
await settle
start scorer
yield until piece is gone
stop scorer
await return elevator
```

The source is closer to the engineering description. Advanced students can spend less effort translating a sequence into composition syntax and more effort on the mechanism behavior.

## 14. Why v2 may remain the right choice

Cleaner syntax does not prove that a framework is better for competition.

V2 has years of team experience, mature documentation, established debugging habits, broad community familiarity, and a large body of working robot code. Those advantages count when a failure must be diagnosed between matches.

The useful question for 2027 is not, "Is v3 better than v2?" Ask instead:

> Does v3 solve enough problems in our codebase to justify teaching and supporting a second command model?

An experiment can answer that question.

## 15. Run a team experiment

Do not start by porting the robot. Implement one small mechanism exercise twice.

### Version A: Commands v2

Use normal v2 command composition for this sequence:

```text
home mechanism
move to position
wait for sensor
run motor until condition
return home
```

### Version B: Commands v3

Implement the same behavior with coroutine-based sequential control.

Compare the two versions on:

- readability and code size
- explicit state variables
- ownership and cancellation behavior
- error handling
- scheduler telemetry
- debugging time
- how quickly an advanced student can explain the code
- how quickly another student can diagnose an inserted failure

The student measurements matter more than line count. A competition framework must remain understandable when the robot breaks in the pit.

## 16. A mentor's mental model

```text
Commands v2
-----------
The scheduler calls us.
We do one slice of work.
We return.
On the next cycle, the scheduler calls us again.

Commands v3
-----------
The scheduler resumes us.
We continue the operation.
We yield when other work needs a turn.
On the next cycle, the scheduler resumes us where we yielded.
```

Awaited commands, nested ownership, scopes, priority, and telemetry all build on the scheduler's ability to preserve suspended command execution.

## 17. Bottom line

V3 does not replace cooperative scheduling. It gives command code explicit suspension points.

V2 cooperates through short lifecycle calls. Programmers store state between calls or express a procedure through command composition.

V3 can suspend and resume the command body itself. Sequential code becomes possible while the framework retains control of scheduling, ownership, cancellation, and telemetry.

For our team, syntax is not the deciding factor. We need to learn whether v3 makes complex robot behavior easier to understand, observe, test, and debug during competition.

If it does, v3 may justify the cost of a second command model. If it only shortens the source, mature v2 code remains the safer choice.

## References

Commands v3 was under active development when this document was written. Check current WPILib documentation and design material before making a season architecture decision.

Consult these primary sources:

- WPILib 2027 documentation and release notes
- WPILib Commands v2 documentation
- WPILib Commands v3 documentation
- Commands v3 design material and implementation in the WPILib repository
