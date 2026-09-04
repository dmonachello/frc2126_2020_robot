package frc.robot.climber;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedController;

import org.mockito.ArgumentCaptor;
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