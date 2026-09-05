package frc.robot.subsystems;

import edu.wpi.first.wpilibj.motorcontrol.Talon;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

/**
 * NAME
 *     BeltSubsystem - owns the ball-transport belt motor.
 *
 * DESCRIPTION
 *     Sends the requested signed output directly to the belt motor.
 */
public class BeltSubsystem extends SubsystemBase {
    private final Talon beltMotor;

    /**
     * NAME
     *     BeltSubsystem - creates the belt motor using the configured PWM channel.
     */
    public BeltSubsystem() {
        beltMotor = new Talon(Constants.Hardware.PWM_BELT);
    }

    /**
     * NAME
     *     run - commands belt motion.
     *
     * PARAMETERS
     *     speed - signed requested output; positive is belt in and negative is belt out.
     */
    public void run(double speed) {
        beltMotor.set(speed);
    }

    /**
     * NAME
     *     stop - commands zero belt output.
     */
    public void stop() {
        run(0);
    }
}
