package frc.robot.drivebase;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.SpeedController;

import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import frc.robot.drivebase.DriveBase;

public class DriveBaseTest 
{
    @Test
    public void neutralSticks()
    {
        genericTest(true, false, false, 0, 0.0, 0.0, 0.0, 0.0);
    }

    public void leftForward() {
        genericTest(true, false, false, 0, 1.0, 0.0, -1.0, 0.0 );
    }

    @Test
    public void fullForwardSticks()
    {
        genericTest(true, false, false, 0, 1.0, 1.0, -1.0, 1.0);
    }

    @Test
    public void fullBackwardSticks()
    {
        genericTest(true, false, false, 0, -1.0, -1.0, 1.0, -1.0);
    }

    @Test
    public void rightLimitSwitchHit()
    {
        genericTest(true,false, true, 0, 1.0,1.0, -1.0, 0.0);
    }

    @Test
    public void leftLimitSwitchHit()
    {
        genericTest(true, true, false, 0, 1.0,1.0, 0, 1);
    }

    @Test
    public void fullLeftReversed()
    {
        genericTest(true, false, false, 1, 1.0,0, 0, -1);
    }

    @Test
    public void fullRightReversed()
    {
        genericTest(true,false, false, 1, 0,1.0, 1, 0);
    }

    @Test
    public void FullForwardReversed()
    {
        genericTest(true,false, false, 1, 1.0,1.0, 1, -1);
    }

    @Test
    public void FullForwardDoubleReversed()
    {
        genericTest(true, false, false, 2, 1.0,1.0, -1, 1);
    }






    @Test
    public void neutralSticksLeftDriveForward()
    {
        genericTest(false, false, false, 0, 0.0, 0.0, 0.0, 0.0);
    }

    public void leftForwardLeftDriveForward() {
        genericTest(false, false, false, 0, 1.0, 0.0, 1.0, 0.0 );
    }

    @Test
    public void fullForwardSticksLeftDriveForward()
    {
        genericTest(false, false, false, 0, 1.0, 1.0, 1.0, -1.0);
    }

    @Test
    public void fullBackwardSticksLeftDriveForward()
    {
        genericTest(false, false, false, 0, -1.0, -1.0, -1.0, 1.0);
    }

    @Test
    public void rightLimitSwitchHitLeftDriveForward()
    {
        genericTest(false,false, true, 0, 1.0,1.0, 1.0, 0.0);
    }

    @Test
    public void leftLimitSwitchHitLeftDriveForward()
    {
        genericTest(false, true, false, 0, 1.0,1.0, 0, -1);
    }

    @Test
    public void fullLeftReversedLeftDriveForward()
    {
        genericTest(false, false, false, 1, 1.0,0, 0, 1);
    }

    @Test
    public void fullRightReversedLeftDriveForward()
    {
        genericTest(false,false, false, 1, 0,1.0, -1, 0);
    }

    @Test
    public void FullForwardReversedLeftDriveForward()
    {
        genericTest(false,false, false, 1, 1.0,1.0, -1, 1);
    }

    @Test
    public void FullForwardDoubleReversedLeftDriveForward()
    {
        genericTest(false, false, false, 2, 1.0,1.0, 1, -1);
    }





    private void genericTest(
            boolean rightDriveForward,
            boolean leftLimit,
            boolean rightLimit,
            int numReverse,
            double leftDrive,
            double rightDrive,
            double leftExpected,
            double rightExpected)
    {
        SpeedController dummyLeft = Mockito.mock(SpeedController.class);
        SpeedController dummyRight = Mockito.mock(SpeedController.class);
        DigitalInput dummyLimitSwitchLeft = Mockito.mock(DigitalInput.class);
        DigitalInput dummyLimitSwitchRight = Mockito.mock(DigitalInput.class);
        DriveBase robot = new DriveBase(dummyLeft, dummyRight, null, null, rightDriveForward, dummyLimitSwitchLeft, dummyLimitSwitchRight,true,true);
        
        for(int i=0; i<numReverse;i++)
        {
            robot.reverseDrive();
        }

        Mockito.when(dummyLimitSwitchLeft.get()).thenAnswer(x -> leftLimit);
        Mockito.when(dummyLimitSwitchRight.get()).thenAnswer(x -> rightLimit);
        robot.drive(leftDrive, rightDrive);
        final ArgumentCaptor<Double> captor = ArgumentCaptor.forClass(Double.class);
        Mockito.verify(dummyLeft).set(captor.capture());
        assertEquals("Left value incorrect", leftExpected, captor.getValue(), 0.0000001);
        Mockito.verify(dummyRight).set(captor.capture());
        assertEquals("Right value incorrect", rightExpected, captor.getValue(), 0.0000001);
        
    }
}