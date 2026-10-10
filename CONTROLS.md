# Robot controls

For setup and deployment steps, see
[`docs/controller-configuration-user-guide.md`](docs/controller-configuration-user-guide.md).

Select the deployed layout with `Constants.Operator.CONTROL_CONFIGURATION`, then rebuild and
redeploy the robot code. The layout cannot change while the robot is running.

Confirm the controller order in the Driver Station USB tab before enabling the robot.

| Configuration | USB 0 | USB 1 | USB 2 |
| --- | --- | --- | --- |
| `XBOX_CONTROLLERS` | Driver Xbox | Operator Xbox | Unused |
| `JOYSTICK_CONTROLLERS` | Left drive joystick | Right drive joystick | Operator joystick |
| `JOYSTICK_DRIVE_XBOX_OPERATOR` | Left drive joystick | Right drive joystick | Operator Xbox |

## Control mappings

| Robot action | Two Xbox controllers | Three joysticks | Two joysticks and Xbox |
| --- | --- | --- | --- |
| Left drivetrain | Driver left Y axis | Left joystick Y axis | Left joystick Y axis |
| Right drivetrain | Driver right Y axis | Right joystick Y axis | Right joystick Y axis |
| Precision drive | Driver right bumper | Right joystick trigger | Right joystick trigger |
| Belt inward | Operator A | Operator joystick button 1 | Operator A |
| Belt outward | Operator B | Operator joystick button 2 | Operator B |
| Roller | Operator X | Operator joystick button 3 | Operator X |
| Toggle climber | Operator Y | Operator joystick button 4 | Operator Y |

## Drive behavior

Drive input has a deadband, output scaling, and slew-rate limiting to make the top-heavy robot
easier for new drivers to control.

| Setting | Value |
| --- | --- |
| Normal drive scale | 40% output |
| Precision drive scale | 20% output |
| Drive deadband | 0.08 |
| Slew-rate limit | 0.8 output units per second |

## Autonomous and camera

Autonomous routines are currently disabled. Entering autonomous cancels commands, disables the
command scheduler, and stops robot outputs.

USB camera startup is currently commented out in `RobotContainer`.

## Where to change controls

The selected configuration and USB positions are defined in `src/main/java/frc/robot/Constants.java`
under `Constants.Operator`.

Physical controller mappings are defined in `src/main/java/frc/robot/RobotControls.java`.
Action-to-command bindings are defined in `src/main/java/frc/robot/RobotContainer.java` inside
`configureBindings()`.

Before using the joystick layouts on the robot, confirm the Logitech model, forward Y-axis
direction on both drive joysticks, right trigger button number, and operator button locations in
Driver Station diagnostics.
