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
    private boolean armsExtended;

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
        armsExtended = true;
    }

    /** NAME
     *     retractArms - retracts both climber arms.
     */
    public void retractArms() {
        leftClimberSolenoid.set(Constants.Hardware.SOLENOID_LEFT_IN);
        rightClimberSolenoid.set(Constants.Hardware.SOLENOID_RIGHT_IN);
        armsExtended = false;
    }

    /**
     * NAME
     *     toggleArms - changes the arms from retracted to extended, or from extended to retracted.
     *
     * DESCRIPTION
     *     The active code assumes the arms are retracted when the robot program starts. Verify
     *     that physical starting position before enabling this control on the robot.
     */
    public void toggleArms() {
        if (armsExtended) {
            retractArms();
        } else {
            extendArms();
        }
    }
}
