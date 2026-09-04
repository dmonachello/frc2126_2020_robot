package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.controls.Controls;
import frc.robot.subsystems.BallManipulatorSubsystem;

// Stage 2 migration note:
// This command preserves the intake/outtake section of the original Teleop.periodic() logic.
public class BallManipulatorTeleopCommand extends RunCommand {
    public BallManipulatorTeleopCommand(
        BallManipulatorSubsystem ballManipulatorSubsystem,
        Controls controls,
        double beltSpeed,
        double rollerSpeed) {
        super(
            () -> {
                // Roller behavior is still driven by the same operator check as the 2020 code.
                if (controls.isRollerOn()) {
                    ballManipulatorSubsystem.outtake(rollerSpeed);
                } else {
                    ballManipulatorSubsystem.outtake(0);
                }

                // Belt direction still maps directly from the legacy Controls helper.
                switch (controls.getBeltDirection()) {
                    case off:
                        ballManipulatorSubsystem.intake(0);
                        break;
                    case in:
                        ballManipulatorSubsystem.intake(beltSpeed);
                        break;
                    case out:
                        ballManipulatorSubsystem.intake(-beltSpeed);
                        break;
                    default:
                        break;
                }
            },
            ballManipulatorSubsystem);
    }
}
