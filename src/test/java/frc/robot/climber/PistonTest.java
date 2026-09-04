package frc.robot.climber;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class PistonTest
{
    @Test
    public void pistonOutReverse()
    {
        DoubleSolenoid solenoid  = Mockito.mock(DoubleSolenoid.class);
        Piston piston = new Piston(solenoid,DoubleSolenoid.Value.kForward,DoubleSolenoid.Value.kReverse);
        piston.out();
        Mockito.verify(solenoid).set(DoubleSolenoid.Value.kReverse);
    }
    @Test
    public void pistonOutForward()
    {
        DoubleSolenoid solenoid  = Mockito.mock(DoubleSolenoid.class);
        Piston piston = new Piston(solenoid,DoubleSolenoid.Value.kReverse,DoubleSolenoid.Value.kForward);
        piston.out();
        Mockito.verify(solenoid).set(DoubleSolenoid.Value.kForward);
    }
    @Test
    public void pistonInReverse()
    {
        DoubleSolenoid solenoid  = Mockito.mock(DoubleSolenoid.class);
        Piston piston = new Piston(solenoid,DoubleSolenoid.Value.kReverse,DoubleSolenoid.Value.kForward);
        piston.in();
        Mockito.verify(solenoid).set(DoubleSolenoid.Value.kReverse);
    }
    @Test
    public void pistonInForward()
    {
        DoubleSolenoid solenoid  = Mockito.mock(DoubleSolenoid.class);
        Piston piston = new Piston(solenoid,DoubleSolenoid.Value.kForward,DoubleSolenoid.Value.kReverse);
        piston.in();
        Mockito.verify(solenoid).set(DoubleSolenoid.Value.kForward);
    }
}