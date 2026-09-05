package frc.robot.motorcontrol;

import edu.wpi.first.wpilibj.motorcontrol.MotorController;

/**
 * NAME
 *     DualMotorController - sends one motor-controller command to a paired motor group.
 *
 * DESCRIPTION
 *     Stage 1 compatibility class. It replaces the 2020 motor-group API with a small
 *     implementation that preserves the two-motor-per-drive-side behavior.
 */
public class DualMotorController implements MotorController {
    private final MotorController primary;
    private final MotorController secondary;
    private boolean inverted;

    /**
     * NAME
     *     DualMotorController - creates a paired motor group.
     *
     * PARAMETERS
     *     primary - first motor in the group.
     *     secondary - second motor in the group.
     */
    public DualMotorController(MotorController primary, MotorController secondary) {
        this.primary = primary;
        this.secondary = secondary;
    }

    /**
     * NAME
     *     set - sends one possibly inverted output to both motors.
     *
     * PARAMETERS
     *     speed - requested motor output.
     */
    @Override
    public void set(double speed) {
        double output = inverted ? -speed : speed;
        primary.set(output);
        secondary.set(output);
    }

    /** NAME
     *     get - reads the output reported by the primary motor.
     *
     * RETURNS
     *     primary motor output.
     */
    @Override
    public double get() {
        return primary.get();
    }

    /**
     * NAME
     *     setInverted - changes group inversion.
     *
     * PARAMETERS
     *     isInverted - requested inversion state.
     */
    @Override
    public void setInverted(boolean isInverted) {
        inverted = isInverted;
    }

    /** NAME
     *     getInverted - reports group inversion.
     *
     * RETURNS
     *     current inversion state.
     */
    @Override
    public boolean getInverted() {
        return inverted;
    }

    /** NAME
     *     disable - disables both grouped motor controllers.
     */
    @Override
    public void disable() {
        primary.disable();
        secondary.disable();
    }

    /** NAME
     *     stopMotor - stops both grouped motor controllers.
     */
    @Override
    public void stopMotor() {
        primary.stopMotor();
        secondary.stopMotor();
    }
}
