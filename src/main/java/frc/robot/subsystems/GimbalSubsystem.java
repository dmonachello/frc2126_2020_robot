package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.gimbal.Gimbal;

public class GimbalSubsystem extends SubsystemBase {
    private final Gimbal gimbal;

    public GimbalSubsystem(Gimbal gimbal) {
        this.gimbal = gimbal;
    }

    public void gimbalHorizontalRelative(double positionDelta) {
        gimbal.gimbalHorizontalRelative(positionDelta);
    }

    public void gimbalVerticalRelative(double positionDelta) {
        gimbal.gimbalVerticalRelative(positionDelta);
    }
}
