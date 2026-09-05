package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.RollerSubsystem;

/**
 * NAME
 *     RollerCommand - runs the ball-ejection roller while held.
 *
 * DESCRIPTION
 *     Requires the roller subsystem and stops its motor when the command ends.
 */
public class RollerCommand extends Command {
    private final RollerSubsystem rollerSubsystem;

    /**
     * NAME
     *     RollerCommand - creates a roller command.
     *
     * PARAMETERS
     *     rollerSubsystem - subsystem that owns the roller motor.
     */
    public RollerCommand(RollerSubsystem rollerSubsystem) {
        this.rollerSubsystem = rollerSubsystem;
        addRequirements(rollerSubsystem);
    }

    /**
     * NAME
     *     execute - runs the roller at the configured speed.
     */
    @Override
    public void execute() {
        rollerSubsystem.run(Constants.Tuning.ROLLER_SPEED);
    }

    /**
     * NAME
     *     end - stops the roller motor.
     *
     * PARAMETERS
     *     interrupted - true when another command interrupts this command.
     */
    @Override
    public void end(boolean interrupted) {
        rollerSubsystem.stop();
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
