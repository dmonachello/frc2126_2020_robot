package frc.robot.weelspinner;

import edu.wpi.first.wpilibj.motorcontrol.MotorController;

public class WeelSpinner
{
    private final MotorController spinner;

    public WeelSpinner (MotorController spinner)
    {
        this.spinner = spinner;
    }
    public void spin(double speed)
    {
        spinner.set(speed);
    }

}
