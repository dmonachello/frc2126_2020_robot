package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.subsystems.BallManipulatorSubsystem;

// Stage 2 migration note:
// This command preserves the intake/outtake section of the original Teleop.periodic() logic.
public class BallManipulatorTeleopCommand extends RunCommand {
    public BallManipulatorTeleopCommand(
        BallManipulatorSubsystem ballManipulatorSubsystem,
        DoubleSupplier intakeSpeedSupplier,
        DoubleSupplier outtakeSpeedSupplier) {
        super(
            () -> {
                ballManipulatorSubsystem.outtake(outtakeSpeedSupplier.getAsDouble());
                ballManipulatorSubsystem.intake(intakeSpeedSupplier.getAsDouble());
            },
            ballManipulatorSubsystem);
    }
}
