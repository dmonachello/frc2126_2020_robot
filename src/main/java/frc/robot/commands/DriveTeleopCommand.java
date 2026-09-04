package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.controls.Controls;
import frc.robot.subsystems.DriveSubsystem;

// Stage 2 migration note:
// This command is the command-based replacement for the drive portion of Teleop.periodic().
public class DriveTeleopCommand extends RunCommand {
    public DriveTeleopCommand(
        DriveSubsystem driveSubsystem,
        Controls controls,
        double normalValue,
        double slowValue) {
        super(
            // Preserve the original left/right tank drive calculation from the 2020 teleop loop.
            () -> driveSubsystem.drive(
                driveValue(controls.getLeftDrive(), controls.leftSpeed(), normalValue, slowValue),
                driveValue(controls.getRightDrive(), controls.rightSpeed(), normalValue, slowValue)),
            driveSubsystem);
    }

    private static double driveValue(
        double joystickValue,
        Controls.DriveSpeed speed,
        double normalValue,
        double slowValue) {
        switch (speed) {
            case normal:
                return joystickValue * normalValue;
            case slow:
                return joystickValue * slowValue;
            default:
                return joystickValue;
        }
    }
}
