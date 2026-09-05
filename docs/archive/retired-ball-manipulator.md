# Retired Ball Manipulator Wrapper

## Status

Retired during Stage 2 command-based conversion. This is retained as a teaching
record, not compiled robot code.

## Why It Was Removed

The 2020 `BallManipulator` class combined two independent motors: the belt and
the roller. It also used boolean direction flags to invert their output.

The current command-based design uses `BeltSubsystem` and `RollerSubsystem`
instead. Each owns one motor and can have its own command, so the belt and
roller can run at the same time. Commands send signed speed directly to their
respective motors. There are no hidden direction flags.

`Belts` and `Roller` were also retired because each only forwarded one method
call to WPILib's motor controller. Keeping them would add indirection without
representing a physical subsystem or a useful command-based concept.

## Historical Behavior

`BallManipulator.intake(speed)` sent the requested speed, or its negative, to
the belt according to `isBeltPositive`. `outtake(speed)` did the equivalent for
the roller with `isRollerPositive`. Its unit test covered those boolean-based
inversions; that test was retired with the class.

## Current Replacement

| 2020 implementation | Current command-based implementation |
| --- | --- |
| `BallManipulator.intake(speed)` | `BeltInCommand` calls `BeltSubsystem.run(+speed)` while held. |
| No separate belt-out operation | `BeltOutCommand` calls `BeltSubsystem.run(-speed)` while held. |
| `BallManipulator.outtake(speed)` | `RollerCommand` calls `RollerSubsystem.run(+speed)` while held. |
| Boolean direction flags | Physical wiring and on-robot validation define the correct direction. |

If a motor moves in the wrong direction during physical testing, record and
correct the physical motor orientation. Do not restore a hidden software
inversion flag without documenting why it is needed.
