package frc.robot.commands;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import frc.robot.ballmanipulator.BallManipulator;
import frc.robot.climber.Climber;
import frc.robot.drivebase.DriveBase;
import frc.robot.subsystems.BallManipulatorSubsystem;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.DriveSubsystem;

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

}
