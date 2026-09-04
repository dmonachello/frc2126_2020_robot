package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.subsystems.WheelSpinnerSubsystem;

// Stage 2 migration note:
// This command preserves the manual spinner-speed behavior from the original teleop loop.
public class WheelSpinnerTeleopCommand extends RunCommand {
    public WheelSpinnerTeleopCommand(
        WheelSpinnerSubsystem wheelSpinnerSubsystem,
        DoubleSupplier spinnerSpeedSupplier) {
        super(
            () -> wheelSpinnerSubsystem.spin(spinnerSpeedSupplier.getAsDouble()),
            wheelSpinnerSubsystem);
    }
}
