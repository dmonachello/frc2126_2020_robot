package frc.robot.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.mockito.Mockito;

import frc.robot.Constants;
import frc.robot.DriveSpeedMode;
import frc.robot.subsystems.BeltSubsystem;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.RollerSubsystem;

// Focused command tests keep test doubles out of the robot's production construction path.
public class TeleopCommandsTest {
    @Test
    public void driveTeleopCommandUsesNormalDriveScale() {
        DriveSubsystem driveSubsystem = Mockito.mock(DriveSubsystem.class);
        DriveSpeedMode driveSpeedMode = new DriveSpeedMode();
        DriveTeleopCommand command =
            new DriveTeleopCommand(driveSubsystem, () -> 1.0, () -> -1.0, driveSpeedMode);

        command.execute();

        Mockito.verify(driveSubsystem).drive(0.8, -0.8);
    }

    @Test
    public void toggleClimberArmsCommandTogglesArmsOnce() {
        ClimberSubsystem climberSubsystem = Mockito.mock(ClimberSubsystem.class);
        ToggleClimberArmsCommand command = new ToggleClimberArmsCommand(climberSubsystem);

        command.initialize();

        Mockito.verify(climberSubsystem).toggleArms();
        assertTrue(command.isFinished());
    }

    @Test
    public void beltInCommandRunsForwardAndStops() {
        BeltSubsystem beltSubsystem = Mockito.mock(BeltSubsystem.class);
        BeltInCommand command = new BeltInCommand(beltSubsystem);

        command.execute();
        command.end(false);

        Mockito.verify(beltSubsystem).run(Constants.Tuning.BELT_SPEED);
        Mockito.verify(beltSubsystem).stop();
    }

    @Test
    public void beltOutCommandRunsBackwardAndStops() {
        BeltSubsystem beltSubsystem = Mockito.mock(BeltSubsystem.class);
        BeltOutCommand command = new BeltOutCommand(beltSubsystem);

        command.execute();
        command.end(false);

        Mockito.verify(beltSubsystem).run(-Constants.Tuning.BELT_SPEED);
        Mockito.verify(beltSubsystem).stop();
    }

    @Test
    public void rollerCommandRunsAndStops() {
        RollerSubsystem rollerSubsystem = Mockito.mock(RollerSubsystem.class);
        RollerCommand command = new RollerCommand(rollerSubsystem);

        command.execute();
        command.end(false);

        Mockito.verify(rollerSubsystem).run(Constants.Tuning.ROLLER_SPEED);
        Mockito.verify(rollerSubsystem).stop();
    }

    @Test
    public void slowDriveCommandSetsAndClearsSlowMode() {
        DriveSpeedMode driveSpeedMode = new DriveSpeedMode();
        SlowDriveCommand command = new SlowDriveCommand(driveSpeedMode);

        command.initialize();
        assertTrue(driveSpeedMode.isSlow());
        command.end(false);

        assertFalse(driveSpeedMode.isSlow());
    }
}
