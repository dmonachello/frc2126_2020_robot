package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.weelspinner.WeelSpinner;

public class WheelSpinnerSubsystem extends SubsystemBase {
    private final WeelSpinner wheelSpinner;

    public WheelSpinnerSubsystem(WeelSpinner wheelSpinner) {
        this.wheelSpinner = wheelSpinner;
    }

    public void spin(double speed) {
        wheelSpinner.spin(speed);
    }
}
