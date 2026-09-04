package frc.robot;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.simulation.DriverStationSim;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.ballmanipulator.BallManipulator;
import frc.robot.climber.Climber;
import frc.robot.drivebase.DriveBase;
import frc.robot.pinout.PinOut;
import frc.robot.subsystems.BallManipulatorSubsystem;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.DriveSubsystem;

// Stage 2 migration note:
// These tests run the scheduler against RobotContainer itself so students can see that the
// active teleop behavior is not only tested in pieces, but also through the command-based
// wiring layer that replaced the old Teleop coordinator.
public class RobotContainerSchedulerTest {
    private final CommandScheduler scheduler = CommandScheduler.getInstance();

    private PinOut pinout;
    private Joystick joystickLeft;
    private Joystick joystickRight;
    private Joystick gamepad;
    private DriveBase driveBase;
    private Climber climber;
    private BallManipulator ballManipulator;
    private RobotContainer robotContainer;

    @Before
    public void setup() {
        HAL.initialize(500, 0);
        scheduler.cancelAll();
        DriverStationSim.setEnabled(true);
        DriverStationSim.setAutonomous(false);
        DriverStationSim.setTest(false);
        DriverStationSim.notifyNewData();

        pinout = new PinOut();
        joystickLeft = Mockito.mock(Joystick.class);
        joystickRight = Mockito.mock(Joystick.class);
        gamepad = Mockito.mock(Joystick.class);
        driveBase = Mockito.mock(DriveBase.class);
        climber = Mockito.mock(Climber.class);
        ballManipulator = Mockito.mock(BallManipulator.class);

        DriveSubsystem driveSubsystem = new DriveSubsystem(driveBase);
        ClimberSubsystem climberSubsystem = new ClimberSubsystem(climber);
        BallManipulatorSubsystem ballManipulatorSubsystem =
            new BallManipulatorSubsystem(ballManipulator);

        RobotHardware hardware = new RobotHardware(
            pinout,
            joystickLeft,
            joystickRight,
            gamepad,
            driveSubsystem,
            climberSubsystem,
            ballManipulatorSubsystem,
            null,
            null);
        robotContainer = new RobotContainer(hardware);
    }

    @After
    public void tearDown() {
        scheduler.cancelAll();
        DriverStationSim.setEnabled(false);
        DriverStationSim.notifyNewData();
    }

    @Test
    public void schedulerRunsTeleopDefaultsThroughRobotContainer() {
        Mockito.when(joystickLeft.getRawAxis(pinout.leftDriveAxis)).thenReturn(-1.0);
        Mockito.when(joystickLeft.getRawButton(pinout.driveSlowButton)).thenReturn(false);
        Mockito.when(joystickRight.getRawAxis(pinout.rightDriveAxis)).thenReturn(-1.0);
        Mockito.when(joystickRight.getRawButton(pinout.driveSlowButton)).thenReturn(true);
        Mockito.when(gamepad.getRawButton(pinout.climberButton)).thenReturn(true);
        Mockito.when(gamepad.getRawButton(pinout.beltInButton)).thenReturn(true);
        Mockito.when(gamepad.getRawButton(pinout.beltOutButton)).thenReturn(false);
        Mockito.when(joystickLeft.getRawButton(pinout.rollerButton)).thenReturn(false);
        Mockito.when(joystickRight.getRawButton(pinout.rollerButton)).thenReturn(true);
        Mockito.when(joystickLeft.getRawButtonPressed(pinout.reverseButton)).thenReturn(false);
        Mockito.when(joystickRight.getRawButtonPressed(pinout.reverseButton)).thenReturn(false);

        scheduler.run();
        DriverStationSim.notifyNewData();
        scheduler.run();

        Mockito.verify(driveBase).drive(0.8, 0.4);
        Mockito.verify(climber).up();
        Mockito.verify(ballManipulator).intake(1.0);
        Mockito.verify(ballManipulator).outtake(1.0);
        Mockito.verify(driveBase, Mockito.never()).reverseDrive();
    }

    @Test
    public void schedulerRunsReverseBindingThroughRobotContainer() {
        Mockito.when(joystickLeft.getRawAxis(pinout.leftDriveAxis)).thenReturn(0.0);
        Mockito.when(joystickRight.getRawAxis(pinout.rightDriveAxis)).thenReturn(0.0);
        Mockito.when(gamepad.getRawButton(pinout.climberButton)).thenReturn(false);
        Mockito.when(gamepad.getRawButton(pinout.beltInButton)).thenReturn(false);
        Mockito.when(gamepad.getRawButton(pinout.beltOutButton)).thenReturn(false);
        Mockito.when(joystickLeft.getRawButton(pinout.rollerButton)).thenReturn(false);
        Mockito.when(joystickRight.getRawButton(pinout.rollerButton)).thenReturn(false);
        Mockito.when(joystickLeft.getRawButtonPressed(pinout.reverseButton)).thenReturn(true);
        Mockito.when(joystickRight.getRawButtonPressed(pinout.reverseButton)).thenReturn(false);

        scheduler.run();

        Mockito.verify(driveBase).reverseDrive();
    }
}
