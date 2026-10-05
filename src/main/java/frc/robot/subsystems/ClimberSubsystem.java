package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

/**
 * NAME
 *     ClimberSubsystem - owns the two climber pneumatic solenoids.
 */
public class ClimberSubsystem extends SubsystemBase {
    private final DoubleSolenoid leftClimberSolenoid;
    private final DoubleSolenoid rightClimberSolenoid;

    /**
     * NAME
     *     areArmsCommandedExtended - checks whether both climber solenoids are set to extend.
     *
     * DESCRIPTION
     *     Compares each solenoid's current commanded value with its configured outward value.
     *     This reports the pneumatic command, not the physical position of the climber arms.
     *
     * RETURNS
     *     true when both solenoids are commanded outward; false otherwise.
     */
    public boolean areArmsCommandedExtended() {
        return leftClimberSolenoid.get() == Constants.Hardware.SOLENOID_LEFT_OUT
            && rightClimberSolenoid.get() == Constants.Hardware.SOLENOID_RIGHT_OUT;
    }

    /**
     * NAME
     *     ClimberSubsystem - creates the two climber solenoids.
     */
    public ClimberSubsystem() {
        leftClimberSolenoid = new DoubleSolenoid(
            Constants.Hardware.PCM,
            PneumaticsModuleType.CTREPCM,
            Constants.Hardware.SOLENOID_LEFT_FORWARD,
            Constants.Hardware.SOLENOID_LEFT_REVERSE);
        rightClimberSolenoid = new DoubleSolenoid(
            Constants.Hardware.PCM,
            PneumaticsModuleType.CTREPCM,
            Constants.Hardware.SOLENOID_RIGHT_FORWARD,
            Constants.Hardware.SOLENOID_RIGHT_REVERSE);
    }

    /** NAME
     *     extendArms - extends both climber arms.
     */
    public void extendArms() {
        leftClimberSolenoid.set(Constants.Hardware.SOLENOID_LEFT_OUT);
        rightClimberSolenoid.set(Constants.Hardware.SOLENOID_RIGHT_OUT);
    }

    /** NAME
     *     retractArms - retracts both climber arms.
     */
    public void retractArms() {
        leftClimberSolenoid.set(Constants.Hardware.SOLENOID_LEFT_IN);
        rightClimberSolenoid.set(Constants.Hardware.SOLENOID_RIGHT_IN);
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
