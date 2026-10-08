package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

/**
 * NAME
 *     ClimberSubsystem - owns the climber's pneumatic control valve.
 *
 * DESCRIPTION
 *     Controls one double-solenoid valve whose two air outputs are split between the left and
 *     right climber cylinders. Both arms therefore receive the same extend or retract command.
 */
public class ClimberSubsystem extends SubsystemBase {
    private final DoubleSolenoid climberSolenoid;

    /**
     * NAME
     *     areArmsCommandedExtended - checks whether the climber valve is set to extend.
     *
     * DESCRIPTION
     *     Compares the valve's current commanded value with its configured outward value.
     *     This reports the pneumatic command, not the physical position of the climber arms.
     *
     * RETURNS
     *     true when the valve is commanded outward; false otherwise.
     */
    public boolean areArmsCommandedExtended() {
        return climberSolenoid.get() == Constants.Hardware.CLIMBER_ARMS_OUT;
    }

    /**
     * NAME
     *     ClimberSubsystem - creates the valve that controls both climber arms.
     */
    public ClimberSubsystem() {
        climberSolenoid = new DoubleSolenoid(
            Constants.Hardware.PCM,
            PneumaticsModuleType.CTREPCM,
            Constants.Hardware.CLIMBER_SOLENOID_FORWARD,
            Constants.Hardware.CLIMBER_SOLENOID_REVERSE);
    }

    /** NAME
     *     extendArms - extends both climber arms.
     */
    public void extendArms() {
        climberSolenoid.set(Constants.Hardware.CLIMBER_ARMS_OUT);
    }

    /** NAME
     *     retractArms - retracts both climber arms.
     */
    public void retractArms() {
        climberSolenoid.set(Constants.Hardware.CLIMBER_ARMS_IN);
    }

    /**
     * NAME
     *     toggleArms - changes the arms from retracted to extended, or from extended to retracted.
     */
    public void toggleArms() {
        if (areArmsCommandedExtended()) {
            retractArms();
        } else {
            extendArms();
        }
    }
}
