# Switch and use controller configurations

Use this guide to select a controller layout, deploy it, and check the controls before enabling the
robot. The robot reads the selection when the program starts. You cannot change layouts while the
program is running.

## Select the layout

1. Open `src/main/java/frc/robot/Constants.java`.
2. Find `Constants.Operator.CONTROL_CONFIGURATION`.
3. Set the constant to one of these values:

   ```java
   ControlConfiguration.XBOX_CONTROLLERS
   ControlConfiguration.JOYSTICK_CONTROLLERS
   ControlConfiguration.JOYSTICK_DRIVE_XBOX_OPERATOR
   ```

For example, select three joysticks with this code:

```java
public static final ControlConfiguration CONTROL_CONFIGURATION =
    ControlConfiguration.JOYSTICK_CONTROLLERS;
```

4. Save `Constants.java`.

## Assign the Driver Station USB positions

Connect the controllers to the Driver Station computer. In the Driver Station USB tab, put each
controller in the position shown for the selected layout.

| Selected layout | USB 0 | USB 1 | USB 2 |
| --- | --- | --- | --- |
| `XBOX_CONTROLLERS` | Driver Xbox | Operator Xbox | Unused |
| `JOYSTICK_CONTROLLERS` | Left drive joystick | Right drive joystick | Operator joystick |
| `JOYSTICK_DRIVE_XBOX_OPERATOR` | Left drive joystick | Right drive joystick | Operator Xbox |

The code cannot detect and correct swapped USB positions.

## Test and deploy the selection

Disable the robot before you deploy code.

1. Open PowerShell in the project directory.
2. Run the automated tests:

   ```powershell
   .\gradlew.bat clean test
   ```

3. Confirm that Gradle reports `BUILD SUCCESSFUL`.
4. Connect the development computer to the robot network.
5. Deploy the code:

   ```powershell
   .\gradlew.bat deploy
   ```

6. Confirm the USB positions again before you enable the robot.

Changing `CONTROL_CONFIGURATION` without rebuilding and redeploying does not change the program on
the robot.

## Use the selected layout

### Two Xbox controllers

Select `ControlConfiguration.XBOX_CONTROLLERS`.

| Controller | Control | Robot action |
| --- | --- | --- |
| Driver Xbox, USB 0 | Left stick Y | Left drivetrain |
| Driver Xbox, USB 0 | Right stick Y | Right drivetrain |
| Driver Xbox, USB 0 | Hold right bumper | Precision drive |
| Operator Xbox, USB 1 | Hold A | Run the belt inward |
| Operator Xbox, USB 1 | Hold B | Run the belt outward |
| Operator Xbox, USB 1 | Hold X | Run the roller |
| Operator Xbox, USB 1 | Press Y | Toggle the climber arms |

### Three joysticks

Select `ControlConfiguration.JOYSTICK_CONTROLLERS`.

| Controller | Control | Robot action |
| --- | --- | --- |
| Left drive joystick, USB 0 | Y axis | Left drivetrain |
| Right drive joystick, USB 1 | Y axis | Right drivetrain |
| Right drive joystick, USB 1 | Hold trigger | Precision drive |
| Operator joystick, USB 2 | Hold button 1 | Run the belt inward |
| Operator joystick, USB 2 | Hold button 2 | Run the belt outward |
| Operator joystick, USB 2 | Hold button 3 | Run the roller |
| Operator joystick, USB 2 | Press button 4 | Toggle the climber arms |

### Drive joysticks with an operator Xbox controller

Select `ControlConfiguration.JOYSTICK_DRIVE_XBOX_OPERATOR`.

| Controller | Control | Robot action |
| --- | --- | --- |
| Left drive joystick, USB 0 | Y axis | Left drivetrain |
| Right drive joystick, USB 1 | Y axis | Right drivetrain |
| Right drive joystick, USB 1 | Hold trigger | Precision drive |
| Operator Xbox, USB 2 | Hold A | Run the belt inward |
| Operator Xbox, USB 2 | Hold B | Run the belt outward |
| Operator Xbox, USB 2 | Hold X | Run the roller |
| Operator Xbox, USB 2 | Press Y | Toggle the climber arms |

## Check the controls before enabling the robot

Keep the robot disabled while you complete these checks.

1. Confirm that Driver Station shows each controller in the required USB position.
2. Move the left drive control. Confirm that only the left drive input changes.
3. Center the left drive control. Confirm that the input returns to zero.
4. Move the right drive control. Confirm that only the right drive input changes.
5. Center the right drive control. Confirm that the input returns to zero.
6. Hold the precision-drive control. Confirm that Driver Station detects it.
7. Check each operator control one at a time.
8. Move the robot to a safe test area.
9. Enable the robot and confirm that forward input drives both sides forward.

## Validate the Logitech joysticks

Complete this check with the physical controllers before match use. Record the results in the team
hardware notes or match checklist.

| Check | Expected software mapping | Verified result |
| --- | --- | --- |
| Exact Logitech model | Team-selected model | Not yet recorded |
| Left drive Y direction | Forward produces a positive normalized input | Not yet recorded |
| Right drive Y direction | Forward produces a positive normalized input | Not yet recorded |
| Right drive trigger | Button 1 | Not yet recorded |
| Operator belt inward | Button 1 | Not yet recorded |
| Operator belt outward | Button 2 | Not yet recorded |
| Operator roller | Button 3 | Not yet recorded |
| Operator climber toggle | Button 4 | Not yet recorded |

If a physical control does not match the table, keep the robot disabled. Confirm the model and the
Driver Station readings before you change `RobotControls.java`.

## Fix common setup problems

### One drive side uses the wrong joystick

Open the Driver Station USB tab. Put the left drive joystick in USB 0 and the right drive joystick
in USB 1.

### The operator controls do not respond

Check USB 2. `JOYSTICK_CONTROLLERS` requires an operator joystick in USB 2.
`JOYSTICK_DRIVE_XBOX_OPERATOR` requires an operator Xbox controller in USB 2.

### The deployed controls do not match the source code

Rebuild and redeploy the robot code. The robot does not read the source file from the development
computer.

### A joystick drives backward

Disable the robot. Inspect the joystick Y axis in Driver Station diagnostics. Change the direction
mapping in `RobotControls.java` only after the team confirms the physical behavior.

### A joystick button performs the wrong action

Disable the robot. Inspect the button numbers in Driver Station diagnostics. Confirm the right
trigger and operator button numbers against the validation table.
