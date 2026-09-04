package frc.robot.gimbal;

import edu.wpi.first.wpilibj.Servo;


public class Gimbal
{
    private final Servo servoXaxis;
    private final Servo servoYaxis;

    public Gimbal(Servo servoXaxis,Servo servoYaxis)
    {
        this.servoXaxis = servoXaxis;
        this.servoYaxis = servoYaxis;
    }
    public void gimbalInitialPosition()
    {
        servoXaxis.set(0);
        servoYaxis.set(0);
    }
    public void gimbalHorizontalAbsolute(double positionValue)
    {
        servoXaxis.set(positionValue);
    }
    public void gimbalVerticalAbsolute(double positionValue)
    {
        servoYaxis.set(positionValue);
    }
    public void gimbalHorizontalRelative(double positionDelta)
    {
        servoXaxis.set(servoXaxis.get() + positionDelta);
    }
    public void gimbalVerticalRelative(double positionDelta)
    {
        servoYaxis.set(servoYaxis.get() + positionDelta);
    }
}