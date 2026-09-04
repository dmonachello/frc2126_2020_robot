package frc.robot.commands;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.subsystems.ClimberSubsystem;

// Stage 2 migration note:
// This command preserves the simple "up while button held, otherwise down" climber behavior
// from the original teleop coordinator.
public class ClimberTeleopCommand extends RunCommand {
    public ClimberTeleopCommand(ClimberSubsystem climberSubsystem, BooleanSupplier climberUpSupplier) {
        super(
            () -> {
                if (climberUpSupplier.getAsBoolean()) {
                    climberSubsystem.up();
                } else {
                    climberSubsystem.down();
                }
            },
            climberSubsystem);
    }
}
