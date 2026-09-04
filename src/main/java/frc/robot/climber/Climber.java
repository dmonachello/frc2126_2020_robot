package frc.robot.climber;

public class Climber
{
    private final Piston pistonLeft;
    private final Piston pistonRight;

    public Climber (Piston pistonLeft, Piston pistonRight)
    {
        this.pistonLeft = pistonLeft;
        this.pistonRight = pistonRight;
    }
    public void up()
    {
        pistonRight.out();
        pistonLeft.out();
    }
    public void down()
    {
        pistonRight.in();
        pistonLeft.in();
    }
}