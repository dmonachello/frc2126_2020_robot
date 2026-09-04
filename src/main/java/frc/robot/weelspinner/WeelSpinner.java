package frc.robot.weelspinner;

import edu.wpi.first.wpilibj.SpeedController;

public class WeelSpinner
{
    private final SpeedController spinner;

    public WeelSpinner (SpeedController spinner)
    {
        this.spinner = spinner;
    }
    public void spin(double speed)
    {
        spinner.set(speed);
    }

}