# Robot Controls

The robot uses two Xbox controllers. Confirm their order in the Driver Station USB tab before
enabling the robot.

| Driver Station port | Controller role | Input | Robot action |
| --- | --- | --- | --- |
| 0 | Driver Xbox controller | Left stick Y | Left drivetrain speed |
| 0 | Driver Xbox controller | Right stick Y | Right drivetrain speed |
| 0 | Driver Xbox controller | Right bumper | Precision drive while held |
| 1 | Operator Xbox controller | A button | Run belt inward while held |
| 1 | Operator Xbox controller | B button | Run belt outward while held |
| 1 | Operator Xbox controller | X button | Run scoring roller while held |
| 1 | Operator Xbox controller | Y button | Toggle climber arms |

## Drive Behavior

Drive input has a deadband, output scaling, and slew-rate limiting to make the top-heavy robot
easier for new drivers to control.

| Setting | Value |
| --- | --- |
| Normal drive scale | 40% output |
| Precision drive scale | 20% output |
| Drive deadband | 0.08 |
| Slew-rate limit | 0.8 output units per second |

## Autonomous And Camera

Autonomous routines are currently disabled. Entering autonomous cancels commands, disables the
command scheduler, and stops robot outputs.

USB camera startup is currently commented out in `RobotContainer`.

## Where To Change Controls

Controller ports are defined in `src/main/java/frc/robot/Constants.java` under
`Constants.Operator`.

Button-to-command bindings are defined in `src/main/java/frc/robot/RobotContainer.java` inside
`configureBindings()`.
