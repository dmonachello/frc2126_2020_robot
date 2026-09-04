package frc.robot.ballmanipulator;

import edu.wpi.first.wpilibj.SpeedController;

public class Roller
{
    private final SpeedController speedController;

    public Roller (SpeedController speedController)
    {
        this.speedController = speedController;
    }
    public void spin(double speed)
    {
        speedController.set(speed);
    }
}