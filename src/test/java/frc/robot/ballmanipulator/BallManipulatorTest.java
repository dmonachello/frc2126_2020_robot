package frc.robot.ballmanipulator;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;


public class BallManipulatorTest
{
    private void genericTest(boolean isBeltPositive, boolean isRollerPositive, Double beltSpeed,Double rollerSpeed,Double expectedBeltSpeed,Double expectedRollerSpeed)
    {
        Belts belt = Mockito.mock(Belts.class);
        Roller roller = Mockito.mock(Roller.class);
        BallManipulator ballManipulator = new BallManipulator(belt,roller,isBeltPositive,isRollerPositive);

        if(beltSpeed != null)
        {
            ballManipulator.intake(beltSpeed);
        }
        if(rollerSpeed != null)
        {
            ballManipulator.outtake(rollerSpeed);
        }
        final ArgumentCaptor<Double> captor = ArgumentCaptor.forClass(Double.class);
        if(expectedBeltSpeed != null)
        {
            Mockito.verify(belt).spin(captor.capture());
            assertEquals("Belt Value Incorrect", expectedBeltSpeed, captor.getValue(), 0.0000001);
        }
        else
        {
            Mockito.verify(belt, Mockito.never()).spin(captor.capture());
        }
        

        if(expectedRollerSpeed != null)
        {
            Mockito.verify(roller).spin(captor.capture());
            assertEquals("Roller Value Incorrect", expectedRollerSpeed, captor.getValue(), 0.0000001);
        }
        else
        {
            Mockito.verify(roller, Mockito.never()).spin(captor.capture());
        }
        
    }

    @Test
    public void rollerPositive()
    {
        genericTest(true, true, null, 1.0, null, 1.0);
    }
    @Test
    public void intakePositive()
    {
        genericTest(true, true, 1.0, null, 1.0, null);
    }

    @Test
    public void rollerNegative()
    {
        genericTest(false, false, null, 1.0, null, -1.0);
    }
    @Test
    public void intakeNagative()
    {
        genericTest(false, false, 1.0, null, -1.0, null);
    }
}