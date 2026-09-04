package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.subsystems.GimbalSubsystem;

// Stage 2 migration note:
// The gimbal is still controlled as relative servo motion each cycle, matching the 2020 code.
public class GimbalTeleopCommand extends RunCommand {
    public GimbalTeleopCommand(
        GimbalSubsystem gimbalSubsystem,
        DoubleSupplier gimbalXValueSupplier,
        DoubleSupplier gimbalYValueSupplier) {
        super(
            () -> {
                gimbalSubsystem.gimbalHorizontalRelative(gimbalXValueSupplier.getAsDouble() * 0.01);
                gimbalSubsystem.gimbalVerticalRelative(gimbalYValueSupplier.getAsDouble() * 0.01);
            },
            gimbalSubsystem);
    }
}
