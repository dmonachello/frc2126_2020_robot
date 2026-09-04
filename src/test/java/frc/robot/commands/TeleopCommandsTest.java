package frc.robot.commands;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import frc.robot.ballmanipulator.BallManipulator;
import frc.robot.climber.Climber;
import frc.robot.drivebase.DriveBase;
import frc.robot.gimbal.Gimbal;
import frc.robot.subsystems.BallManipulatorSubsystem;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.GimbalSubsystem;
import frc.robot.subsystems.WheelSpinnerSubsystem;
import frc.robot.weelspinner.WeelSpinner;

// Stage 2 migration note:
// These tests validate the active command-based teleop path. They sit beside TeleopTest so
// students can compare how behavior coverage moved from one polling coordinator test suite
// into smaller tests focused on individual commands.
public class TeleopCommandsTest {
    @Test
    public void driveTeleopCommandUsesSuppliedDriveValues() {
        DriveBase driveBase = Mockito.mock(DriveBase.class);
        DriveSubsystem driveSubsystem = new DriveSubsystem(driveBase);
        DriveTeleopCommand command = new DriveTeleopCommand(driveSubsystem, () -> 0.75, () -> -0.25);

        command.execute();

        Mockito.verify(driveBase).drive(0.75, -0.25);
    }

    @Test
    public void climberTeleopCommandDrivesClimberUpWhenRequested() {
        Climber climber = Mockito.mock(Climber.class);
        ClimberSubsystem climberSubsystem = new ClimberSubsystem(climber);
        ClimberTeleopCommand command = new ClimberTeleopCommand(climberSubsystem, () -> true);

        command.execute();

        Mockito.verify(climber).up();
        Mockito.verify(climber, Mockito.never()).down();
    }

    @Test
    public void climberTeleopCommandDrivesClimberDownWhenNotRequested() {
        Climber climber = Mockito.mock(Climber.class);
        ClimberSubsystem climberSubsystem = new ClimberSubsystem(climber);
        ClimberTeleopCommand command = new ClimberTeleopCommand(climberSubsystem, () -> false);

        command.execute();

        Mockito.verify(climber).down();
        Mockito.verify(climber, Mockito.never()).up();
    }

    @Test
    public void ballManipulatorTeleopCommandUsesBothSuppliedSpeeds() {
        BallManipulator ballManipulator = Mockito.mock(BallManipulator.class);
        BallManipulatorSubsystem ballManipulatorSubsystem = new BallManipulatorSubsystem(ballManipulator);
        BallManipulatorTeleopCommand command =
            new BallManipulatorTeleopCommand(ballManipulatorSubsystem, () -> 1.0, () -> 0.5);

        command.execute();

        Mockito.verify(ballManipulator).outtake(0.5);
        Mockito.verify(ballManipulator).intake(1.0);
    }

    @Test
    public void gimbalTeleopCommandAppliesExpectedScaling() {
        Gimbal gimbal = Mockito.mock(Gimbal.class);
        GimbalSubsystem gimbalSubsystem = new GimbalSubsystem(gimbal);
        GimbalTeleopCommand command = new GimbalTeleopCommand(gimbalSubsystem, () -> 0.5, () -> -0.25);

        command.execute();

        final ArgumentCaptor<Double> horizontalCaptor = ArgumentCaptor.forClass(Double.class);
        final ArgumentCaptor<Double> verticalCaptor = ArgumentCaptor.forClass(Double.class);
        Mockito.verify(gimbal).gimbalHorizontalRelative(horizontalCaptor.capture());
        Mockito.verify(gimbal).gimbalVerticalRelative(verticalCaptor.capture());
        assertEquals("Horizontal gimbal delta incorrect", 0.005, horizontalCaptor.getValue(), 0.0000001);
        assertEquals("Vertical gimbal delta incorrect", -0.0025, verticalCaptor.getValue(), 0.0000001);
    }

    @Test
    public void wheelSpinnerTeleopCommandUsesSuppliedSpeed() {
        WeelSpinner wheelSpinner = Mockito.mock(WeelSpinner.class);
        WheelSpinnerSubsystem wheelSpinnerSubsystem = new WheelSpinnerSubsystem(wheelSpinner);
        WheelSpinnerTeleopCommand command = new WheelSpinnerTeleopCommand(wheelSpinnerSubsystem, () -> -0.4);

        command.execute();

        Mockito.verify(wheelSpinner).spin(-0.4);
    }
}
