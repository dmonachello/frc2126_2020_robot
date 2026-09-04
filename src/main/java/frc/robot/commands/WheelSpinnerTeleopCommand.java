package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.controls.Controls;
import frc.robot.subsystems.WheelSpinnerSubsystem;

// Stage 2 migration note:
// This command preserves the manual spinner-speed behavior from the original teleop loop.
public class WheelSpinnerTeleopCommand extends RunCommand {
    public WheelSpinnerTeleopCommand(WheelSpinnerSubsystem wheelSpinnerSubsystem, Controls controls) {
        super(
            () -> wheelSpinnerSubsystem.spin(controls.spinnerSpeed()),
            wheelSpinnerSubsystem);
    }
}
