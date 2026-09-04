package frc.robot.motorcontrol;

import edu.wpi.first.wpilibj.motorcontrol.MotorController;

// Stage 1 compatibility shim:
// The 2020 code grouped PWM motor controllers with WPILib's older grouping class.
// This wrapper keeps the same "send one output to two motors" behavior while avoiding
// deprecated APIs during the 2026 migration.
public class DualMotorController implements MotorController {
    private final MotorController primary;
    private final MotorController secondary;
    private boolean inverted;

    public DualMotorController(MotorController primary, MotorController secondary) {
        this.primary = primary;
        this.secondary = secondary;
    }

    @Override
    public void set(double speed) {
        // Both motors always receive the same commanded output, just like the 2020 drive groups.
        double output = inverted ? -speed : speed;
        primary.set(output);
        secondary.set(output);
    }

    @Override
    public double get() {
        return primary.get();
    }

    @Override
    public void setInverted(boolean isInverted) {
        inverted = isInverted;
    }

    @Override
    public boolean getInverted() {
        return inverted;
    }

    @Override
    public void disable() {
        primary.disable();
        secondary.disable();
    }

    @Override
    public void stopMotor() {
        primary.stopMotor();
        secondary.stopMotor();
    }
}
