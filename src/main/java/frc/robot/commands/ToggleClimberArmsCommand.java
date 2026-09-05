package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ClimberSubsystem;

/**
 * NAME
 *     ToggleClimberArmsCommand - changes the climber arms on one button press.
 *
 * DESCRIPTION
 *     Ends immediately after toggling. Releasing the button does not command pneumatic motion.
 */
public class ToggleClimberArmsCommand extends Command {
    private final ClimberSubsystem climberSubsystem;

    /**
     * NAME
     *     ToggleClimberArmsCommand - creates a climber-arm toggle command.
     *
     * PARAMETERS
     *     climberSubsystem - subsystem that owns the climber solenoids.
     */
    public ToggleClimberArmsCommand(ClimberSubsystem climberSubsystem) {
        this.climberSubsystem = climberSubsystem;
        addRequirements(climberSubsystem);
    }

    /** NAME
     *     initialize - changes the arm position once when the button is pressed.
     */
    @Override
    public void initialize() {
        climberSubsystem.toggleArms();
    }

    /**
     * NAME
     *     isFinished - ends after the one position change.
     *
     * RETURNS
     *     true.
     */
    @Override
    public boolean isFinished() {
        return true;
    }
}
