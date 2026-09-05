package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.DriveSpeedMode;
import frc.robot.subsystems.DriveSubsystem;

/** NAME: DriveTeleopCommand - continuously drives the robot from two joystick-axis suppliers.
 * DESCRIPTION: installed as the drivetrain default command. */
public class DriveTeleopCommand extends Command {
    private final DriveSubsystem driveSubsystem;
    private final DoubleSupplier leftDriveSupplier;
    private final DoubleSupplier rightDriveSupplier;
    private final DriveSpeedMode driveSpeedMode;

    /** PARAMETERS: driveSubsystem - drivetrain owner; leftDriveSupplier - left input; rightDriveSupplier - right input; driveSpeedMode - normal or slow scale. */
    public DriveTeleopCommand(
        DriveSubsystem driveSubsystem,
        DoubleSupplier leftDriveSupplier,
        DoubleSupplier rightDriveSupplier,
        DriveSpeedMode driveSpeedMode) {
        this.driveSubsystem = driveSubsystem;
        this.leftDriveSupplier = leftDriveSupplier;
        this.rightDriveSupplier = rightDriveSupplier;
        this.driveSpeedMode = driveSpeedMode;
        addRequirements(driveSubsystem);
    }

    /** NAME: execute - reads, scales, and sends both tank-drive outputs. */
    @Override
    public void execute() {
        driveSubsystem.drive(
            scale(leftDriveSupplier.getAsDouble()),
            scale(rightDriveSupplier.getAsDouble()));
    }

    /** NAME: isFinished - keeps this default command scheduled. RETURNS: always false. */
    @Override
    public boolean isFinished() {
        return false;
    }

    /** NAME: scale - applies the selected drive scale. PARAMETERS: joystickValue - raw axis value. RETURNS: scaled output. */
    private double scale(double joystickValue) {
        double driveScale;

        if (driveSpeedMode.isSlow()) {
            driveScale = Constants.Tuning.SLOW_DRIVE_SCALE;
        } else {
            driveScale = Constants.Tuning.NORMAL_DRIVE_SCALE;
        }

        return joystickValue * driveScale;
    }
}
