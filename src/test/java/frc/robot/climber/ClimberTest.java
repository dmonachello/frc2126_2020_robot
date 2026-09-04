package frc.robot.climber;

import org.junit.Test;
import org.mockito.Mockito;

public class ClimberTest
{
    @Test
    public void climberUp()
    {
        Piston pistonLeft = Mockito.mock(Piston.class);
        Piston pistonRight = Mockito.mock(Piston.class);
        Climber climber = new Climber(pistonLeft,pistonRight);
        climber.up();
        Mockito.verify(pistonLeft).out();
        Mockito.verify(pistonRight).out();
    }

    @Test
    public void climberDown()
    {
        Piston pistonLeft = Mockito.mock(Piston.class);
        Piston pistonRight = Mockito.mock(Piston.class);
        Climber climber = new Climber(pistonLeft,pistonRight);
        climber.down();
        Mockito.verify(pistonLeft).in();
        Mockito.verify(pistonRight).in();
    }
}
