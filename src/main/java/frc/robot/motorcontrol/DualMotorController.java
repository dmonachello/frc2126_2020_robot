package frc.robot.motorcontrol;

import edu.wpi.first.wpilibj.motorcontrol.MotorController;

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
