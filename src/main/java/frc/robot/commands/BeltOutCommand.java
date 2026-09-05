package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.BeltSubsystem;

/**
 * NAME
 *     BeltOutCommand - runs the ball-transport belt away from the intake while held.
 *
 * DESCRIPTION
 *     Requires the belt subsystem and stops its motor when the command ends.
 */
public class BeltOutCommand extends Command {
    private final BeltSubsystem beltSubsystem;

    /**
     * NAME
     *     BeltOutCommand - creates a belt-out command.
     *
     * PARAMETERS
     *     beltSubsystem - subsystem that owns the belt motor.
     */
    public BeltOutCommand(BeltSubsystem beltSubsystem) {
        this.beltSubsystem = beltSubsystem;
        addRequirements(beltSubsystem);
    }

    /**
     * NAME
     *     execute - runs the belt opposite the configured intake direction.
     */
    @Override
    public void execute() {
        beltSubsystem.run(-Constants.Tuning.BELT_SPEED);
    }

    /**
     * NAME
     *     end - stops the belt motor.
     *
     * PARAMETERS
     *     interrupted - true when another command interrupts this command.
     */
    @Override
    public void end(boolean interrupted) {
        beltSubsystem.stop();
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
