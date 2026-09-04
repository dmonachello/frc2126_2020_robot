package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.drivebase.DriveBase;

public class DriveSubsystem extends SubsystemBase {
    private final DriveBase driveBase;

    public DriveSubsystem(DriveBase driveBase) {
        this.driveBase = driveBase;
    }

    public void drive(double left, double right) {
        driveBase.drive(left, right);
    }

    public void reverseDrive() {
        driveBase.reverseDrive();
    }
}
