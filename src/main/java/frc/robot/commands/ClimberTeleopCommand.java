package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.controls.Controls;
import frc.robot.subsystems.ClimberSubsystem;

// Stage 2 migration note:
// This command preserves the simple "up while button held, otherwise down" climber behavior
// from the original teleop coordinator.
public class ClimberTeleopCommand extends RunCommand {
    public ClimberTeleopCommand(ClimberSubsystem climberSubsystem, Controls controls) {
        super(
            () -> {
                switch (controls.climberDirection()) {
                    case up:
                        climberSubsystem.up();
                        break;
                    case down:
                        climberSubsystem.down();
                        break;
                    default:
                        break;
                }
            },
            climberSubsystem);
    }
}
