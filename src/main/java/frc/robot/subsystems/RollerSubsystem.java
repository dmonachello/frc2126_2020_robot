package frc.robot.subsystems;

import edu.wpi.first.wpilibj.motorcontrol.Talon;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

/**
 * NAME
 *     RollerSubsystem - owns the ball-ejection roller motor.
 *
 * DESCRIPTION
 *     Sends the requested signed output directly to the roller motor.
 */
public class RollerSubsystem extends SubsystemBase {
    private final Talon rollerMotor;

    /**
     * NAME
     *     RollerSubsystem - creates the roller motor using the configured PWM channel.
     */
    public RollerSubsystem() {
        rollerMotor = new Talon(Constants.Hardware.PWM_ROLLER);
    }

    /**
     * NAME
     *     run - commands roller motion.
     *
     * PARAMETERS
     *     speed - signed requested output.
     */
    public void run(double speed) {
        rollerMotor.set(speed);
    }

    /**
     * NAME
     *     stop - commands zero roller output.
     */
    public void stop() {
        run(0);
    }
}
