package frc.robot;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.SpeedController;
import frc.robot.ballmanipulator.BallManipulator;
import frc.robot.climber.Climber;
import frc.robot.controls.Controls;
import frc.robot.drivebase.DriveBase;
import frc.robot.weelspinner.WeelSpinner;
import frc.robot.gimbal.*;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;

public class TeleopTest
{
    private final Climber climber = Mockito.mock(Climber.class);
    private final Controls controls = Mockito.mock(Controls.class);
    private final DriveBase driveBase = Mockito.mock(DriveBase.class);
    private final WeelSpinner weelSpinner = Mockito.mock(WeelSpinner.class);
    private final BallManipulator ballManipulator = Mockito.mock(BallManipulator.class);
    private final Gimbal gimbal = Mockito.mock(Gimbal.class);

    @Test
    public void testOff()
    {
        final ArgumentCaptor<Double> captor1 = ArgumentCaptor.forClass(Double.class);
        final ArgumentCaptor<Double> captor2 = ArgumentCaptor.forClass(Double.class);
        Teleop teleop = new Teleop(climber, controls, driveBase, weelSpinner, ballManipulator,gimbal, 1, 1, 1, 0.5);
        Mockito.when(controls.isRollerOn()).then(x->false);
        Mockito.when(controls.getBeltDirection()).then(x->Controls.BeltDirection.off);
        Mockito.when(controls.climberDirection()).then(x->Controls.ClimberState.down);
        Mockito.when(controls.leftSpeed()).then(x->Controls.DriveSpeed.normal);
        Mockito.when(controls.rightSpeed()).then(x->Controls.DriveSpeed.normal);
        teleop.periodic();
        Mockito.verify(climber).down();
        Mockito.verify(driveBase).drive(captor1.capture(), captor2.capture());
        assertEquals("Left value incorrect", 0, captor1.getValue(), 0.0000001);
        assertEquals("Right value incorrect", 0, captor2.getValue(), 0.0000001);
        Mockito.verify(ballManipulator).intake(captor1.capture());
        assertEquals("Intake value incorrect", 0, captor1.getValue(), 0.0000001);
        Mockito.verify(ballManipulator).outtake(captor1.capture());
        assertEquals("Outtake value incorrect", 0, captor1.getValue(), 0.0000001);
    }

    @Test
    public void testOn()
    {
        final ArgumentCaptor<Double> captor1 = ArgumentCaptor.forClass(Double.class);
        final ArgumentCaptor<Double> captor2 = ArgumentCaptor.forClass(Double.class);
        Teleop teleop = new Teleop(climber, controls, driveBase, weelSpinner, ballManipulator,gimbal, 1, 1, 1, .5);
        Mockito.when(controls.isRollerOn()).then(x->true);
        Mockito.when(controls.getBeltDirection()).then(x->Controls.BeltDirection.in);
        Mockito.when(controls.climberDirection()).then(x->Controls.ClimberState.up);
        Mockito.when(controls.getLeftDrive()).then(x->1.0);
        Mockito.when(controls.getRightDrive()).then(x->1.0);
        Mockito.when(controls.leftSpeed()).then(x->Controls.DriveSpeed.normal);
        Mockito.when(controls.rightSpeed()).then(x->Controls.DriveSpeed.normal);
        teleop.periodic();
        Mockito.verify(climber).up();
        Mockito.verify(driveBase).drive(captor1.capture(), captor2.capture());
        assertEquals("Left value incorrect", 1, captor1.getValue(), 0.0000001);
        assertEquals("Right value incorrect", 1, captor2.getValue(), 0.0000001);
        Mockito.verify(ballManipulator).intake(captor1.capture());
        assertEquals("Intake value incorrect", 1, captor1.getValue(), 0.0000001);
        Mockito.verify(ballManipulator).outtake(captor1.capture());
        assertEquals("Outtake value incorrect", 1, captor1.getValue(), 0.0000001);
    }

    @Test
    public void testReverseOutakeSlow()
    {
        final ArgumentCaptor<Double> captor1 = ArgumentCaptor.forClass(Double.class);
        final ArgumentCaptor<Double> captor2 = ArgumentCaptor.forClass(Double.class);
        Teleop teleop = new Teleop(climber, controls, driveBase, weelSpinner, ballManipulator,gimbal, 1, 1, 1, 0.5);
        Mockito.when(controls.isRollerOn()).then(x->false);
        Mockito.when(controls.getBeltDirection()).then(x->Controls.BeltDirection.out);
        Mockito.when(controls.climberDirection()).then(x->Controls.ClimberState.down);
        Mockito.when(controls.isReverse()).then(x->true);
        Mockito.when(controls.getLeftDrive()).then(x->1.0);
        Mockito.when(controls.getRightDrive()).then(x->1.0);
        Mockito.when(controls.leftSpeed()).then(x->Controls.DriveSpeed.slow);
        Mockito.when(controls.rightSpeed()).then(x->Controls.DriveSpeed.slow);
        teleop.periodic();
        Mockito.verify(climber).down();
        Mockito.verify(driveBase).reverseDrive();
        Mockito.verify(driveBase).drive(captor1.capture(), captor2.capture());
        assertEquals("Left value incorrect", .5, captor1.getValue(), 0.0000001);
        assertEquals("Right value incorrect", .5, captor2.getValue(), 0.0000001);
        Mockito.verify(ballManipulator).intake(captor1.capture());
        assertEquals("Intake value incorrect", -1, captor1.getValue(), 0.0000001);
        Mockito.verify(ballManipulator).outtake(captor1.capture());
        assertEquals("Outtake value incorrect", 0, captor1.getValue(), 0.0000001);
    }
}
