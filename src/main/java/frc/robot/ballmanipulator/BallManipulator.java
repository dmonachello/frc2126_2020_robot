package frc.robot.ballmanipulator;

public class BallManipulator
{
    private final Belts belt;
    private final Roller roller;
    private final boolean isRollerPositive;
    private final boolean isBeltPositive;


    public BallManipulator(final Belts belt, final Roller roller, final boolean isBeltPositive,
            final boolean isRollerPositive) {
        this.roller = roller;
        this.belt = belt;
        this.isRollerPositive = isRollerPositive;
        this.isBeltPositive = isBeltPositive;
    }

    public void intake(final double speed) {
        if(isBeltPositive){
            belt.spin(speed);
        } 
        else
        {
            belt.spin(-speed);
        }
    }

    public void outtake(final double speed)
    {
        if(isRollerPositive){
            roller.spin(speed);
        } 
        else
        {
            roller.spin(-speed);
        }
    }
}

