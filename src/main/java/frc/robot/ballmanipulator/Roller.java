package frc.robot.ballmanipulator;

import edu.wpi.first.wpilibj.motorcontrol.MotorController;

public class Roller
{
    private final MotorController speedController;

    public Roller (MotorController speedController)
    {
        this.speedController = speedController;
    }
    public void spin(double speed)
    {
        speedController.set(speed);
    }
}
