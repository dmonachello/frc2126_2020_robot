package frc.robot.ballmanipulator;

import edu.wpi.first.wpilibj.motorcontrol.MotorController;

public class Belts
{
    private final MotorController speedController;
    public Belts(MotorController speedController)
    {
        this.speedController = speedController;
    }
    public void spin(double speed)
    {
        speedController.set(speed);
    }
}
