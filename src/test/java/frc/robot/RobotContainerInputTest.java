package frc.robot;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import edu.wpi.first.wpilibj.Joystick;
import frc.robot.ballmanipulator.BallManipulator;
import frc.robot.climber.Climber;
import frc.robot.drivebase.DriveBase;
import frc.robot.gimbal.Gimbal;
import frc.robot.pinout.PinOut;
import frc.robot.subsystems.BallManipulatorSubsystem;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.GimbalSubsystem;
import frc.robot.subsystems.WheelSpinnerSubsystem;
import frc.robot.weelspinner.WeelSpinner;

// Stage 2 migration note:
// These tests verify the input-mapping behavior that moved from Controls.java into
// RobotContainer. They make the "same behavior, new home" part of the migration explicit.
public class RobotContainerInputTest {
    private PinOut pinout;
    private Joystick joystickLeft;
    private Joystick joystickRight;
    private Joystick gamepad;
    private RobotContainer robotContainer;

    @Before
    public void setup() {
        pinout = new PinOut();
        joystickLeft = Mockito.mock(Joystick.class);
        joystickRight = Mockito.mock(Joystick.class);
        gamepad = Mockito.mock(Joystick.class);

        DriveSubsystem driveSubsystem = new DriveSubsystem(Mockito.mock(DriveBase.class));
        ClimberSubsystem climberSubsystem = new ClimberSubsystem(Mockito.mock(Climber.class));
        WheelSpinnerSubsystem wheelSpinnerSubsystem =
            new WheelSpinnerSubsystem(Mockito.mock(WeelSpinner.class));
        BallManipulatorSubsystem ballManipulatorSubsystem =
            new BallManipulatorSubsystem(Mockito.mock(BallManipulator.class));
        GimbalSubsystem gimbalSubsystem = new GimbalSubsystem(Mockito.mock(Gimbal.class));

        robotContainer = new RobotContainer(
            pinout,
            joystickLeft,
            joystickRight,
            gamepad,
            driveSubsystem,
            climberSubsystem,
            wheelSpinnerSubsystem,
            ballManipulatorSubsystem,
            gimbalSubsystem);
    }

    @Test
    public void leftDriveUsesNormalScalingWhenSlowModeIsOff() {
        Mockito.when(joystickLeft.getRawAxis(pinout.leftDriveAxis)).thenReturn(-1.0);
        Mockito.when(joystickLeft.getRawButton(pinout.driveSlowButton)).thenReturn(false);

        assertEquals(0.8, robotContainer.getLeftDriveValue(), 0.0000001);
    }

    @Test
    public void rightDriveUsesSlowScalingWhenSlowModeIsOn() {
        Mockito.when(joystickRight.getRawAxis(pinout.rightDriveAxis)).thenReturn(-1.0);
        Mockito.when(joystickRight.getRawButton(pinout.driveSlowButton)).thenReturn(true);

        assertEquals(0.4, robotContainer.getRightDriveValue(), 0.0000001);
    }

    @Test
    public void climberButtonMapsToUpRequest() {
        Mockito.when(gamepad.getRawButton(pinout.climberButton)).thenReturn(true);

        assertTrue(robotContainer.isClimberUpRequested());
    }

    @Test
    public void reversePressedUsesEitherDriveJoystick() {
        Mockito.when(joystickLeft.getRawButtonPressed(pinout.reverseButton)).thenReturn(false);
        Mockito.when(joystickRight.getRawButtonPressed(pinout.reverseButton)).thenReturn(true);

        assertTrue(robotContainer.isReversePressed());
    }

    @Test
    public void beltInCommandUsesPositiveConfiguredSpeed() {
        Mockito.when(gamepad.getRawButton(pinout.beltInButton)).thenReturn(true);
        Mockito.when(gamepad.getRawButton(pinout.beltOutButton)).thenReturn(false);

        assertEquals(pinout.beltSpeed, robotContainer.getBeltCommandSpeed(), 0.0000001);
    }

    @Test
    public void beltOutCommandUsesNegativeConfiguredSpeed() {
        Mockito.when(gamepad.getRawButton(pinout.beltInButton)).thenReturn(false);
        Mockito.when(gamepad.getRawButton(pinout.beltOutButton)).thenReturn(true);

        assertEquals(-pinout.beltSpeed, robotContainer.getBeltCommandSpeed(), 0.0000001);
    }

    @Test
    public void rollerCommandTurnsOnWhenEitherDriveJoystickRequestsIt() {
        Mockito.when(joystickLeft.getRawButton(pinout.rollerButton)).thenReturn(false);
        Mockito.when(joystickRight.getRawButton(pinout.rollerButton)).thenReturn(true);

        assertEquals(pinout.rollerSpeed, robotContainer.getRollerCommandSpeed(), 0.0000001);
    }

    @Test
    public void spinnerDeadbandReturnsZeroInsideRange() {
        Mockito.when(gamepad.getRawAxis(0)).thenReturn(0.2);

        assertEquals(0.0, robotContainer.getSpinnerSpeed(), 0.0000001);
    }

    @Test
    public void gimbalAxesApplyDeadbandAndYInversion() {
        Mockito.when(gamepad.getRawAxis(pinout.gimbalXAxisChannel)).thenReturn(0.5);
        Mockito.when(gamepad.getRawAxis(pinout.gimbalYAxisChannel)).thenReturn(0.5);

        assertEquals(0.5, robotContainer.getGimbalXValue(), 0.0000001);
        assertEquals(-0.5, robotContainer.getGimbalYValue(), 0.0000001);
    }

    @Test
    public void reverseIsFalseWhenNeitherJoystickRequestsIt() {
        Mockito.when(joystickLeft.getRawButtonPressed(pinout.reverseButton)).thenReturn(false);
        Mockito.when(joystickRight.getRawButtonPressed(pinout.reverseButton)).thenReturn(false);

        assertFalse(robotContainer.isReversePressed());
    }
}
