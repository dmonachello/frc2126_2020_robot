package frc.robot.weelspinner;

import edu.wpi.first.wpilibj.motorcontrol.MotorController;

public class Spinner
{
    private final MotorController speedController;

    public Spinner (MotorController speedController)
    {
        this.speedController = speedController;
    }
}
