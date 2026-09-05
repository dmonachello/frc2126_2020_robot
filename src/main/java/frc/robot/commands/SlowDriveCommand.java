package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.DriveSpeedMode;

/**
 * NAME
 *     SlowDriveCommand - enables precision drive while its button is held.
 *
 * DESCRIPTION
 *     Changes shared speed-mode state read by the default drive command.
 */
public class SlowDriveCommand extends Command {
    private final DriveSpeedMode driveSpeedMode;

    /**
     * NAME
     *     SlowDriveCommand - creates a precision-drive command.
     *
     * PARAMETERS
     *     driveSpeedMode - shared state read by the drive command.
     */
    public SlowDriveCommand(DriveSpeedMode driveSpeedMode) {
        this.driveSpeedMode = driveSpeedMode;
    }

    /** NAME
     *     initialize - enables slow drive when the button is pressed.
     */
    @Override
    public void initialize() {
        driveSpeedMode.setSlow(true);
    }

    /**
     * NAME
     *     end - restores normal drive when the button is released or interrupted.
     *
     * PARAMETERS
     *     interrupted - true when another command interrupts this command.
     */
    @Override
    public void end(boolean interrupted) {
        driveSpeedMode.setSlow(false);
    }

    /**
     * NAME
     *     isFinished - keeps the command scheduled while its button is held.
     *
     * RETURNS
     *     false.
     */
    @Override
    public boolean isFinished() {
        return false;
    }
}
