package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.subsystems.DriveSubsystem;

// Stage 2 migration note:
// This command is the command-based replacement for the drive portion of Teleop.periodic().
public class DriveTeleopCommand extends RunCommand {
    public DriveTeleopCommand(
        DriveSubsystem driveSubsystem,
        DoubleSupplier leftDriveSupplier,
        DoubleSupplier rightDriveSupplier) {
        super(
            // Stage 2 change: RobotContainer now owns joystick interpretation directly instead
            // of routing it through the old Controls helper.
            () -> driveSubsystem.drive(leftDriveSupplier.getAsDouble(), rightDriveSupplier.getAsDouble()),
            driveSubsystem);
    }
}
