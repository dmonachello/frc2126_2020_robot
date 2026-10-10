package frc.robot;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.simulation.DriverStationSim;

public class RobotControlsTest {
    private static final double TOLERANCE = 0.0000001;

    @BeforeClass
    public static void initializeHal() {
        assertTrue(HAL.initialize(500, 0));
    }

    @Before
    public void resetDriverStation() {
        DriverStationSim.resetData();
        for (int port = Constants.Operator.USB_0; port <= Constants.Operator.USB_2; port++) {
            DriverStationSim.setJoystickAxisCount(port, 6);
            DriverStationSim.setJoystickButtonCount(port, 12);
        }
        DriverStationSim.notifyNewData();
    }

    @Test
    public void xboxControllersProvideEveryRobotAction() {
        RobotControls controls = new RobotControls(ControlConfiguration.XBOX_CONTROLLERS);

        assertDriveAxes(
            controls,
            Constants.Operator.USB_0,
            XboxController.Axis.kLeftY.value,
            Constants.Operator.USB_0,
            XboxController.Axis.kRightY.value);
        assertAction(
            controls,
            Constants.Operator.USB_0,
            XboxController.Button.kRightBumper.value,
            Action.SLOW_DRIVE);
        assertAction(
            controls,
            Constants.Operator.USB_1,
            XboxController.Button.kA.value,
            Action.BELT_IN);
        assertAction(
            controls,
            Constants.Operator.USB_1,
            XboxController.Button.kB.value,
            Action.BELT_OUT);
        assertAction(
            controls,
            Constants.Operator.USB_1,
            XboxController.Button.kX.value,
            Action.RUN_ROLLER);
        assertAction(
            controls,
            Constants.Operator.USB_1,
            XboxController.Button.kY.value,
            Action.TOGGLE_CLIMBER);
    }

    @Test
    public void joystickControllersProvideEveryRobotAction() {
        RobotControls controls = new RobotControls(ControlConfiguration.JOYSTICK_CONTROLLERS);

        assertDriveAxes(
            controls,
            Constants.Operator.USB_0,
            Joystick.AxisType.kY.value,
            Constants.Operator.USB_1,
            Joystick.AxisType.kY.value);
        assertAction(
            controls,
            Constants.Operator.USB_1,
            1,
            Action.SLOW_DRIVE);
        assertAction(
            controls,
            Constants.Operator.USB_2,
            1,
            Action.BELT_IN);
        assertAction(
            controls,
            Constants.Operator.USB_2,
            2,
            Action.BELT_OUT);
        assertAction(
            controls,
            Constants.Operator.USB_2,
            3,
            Action.RUN_ROLLER);
        assertAction(
            controls,
            Constants.Operator.USB_2,
            4,
            Action.TOGGLE_CLIMBER);
    }

    @Test
    public void joystickDriveAndXboxOperatorProvideEveryRobotAction() {
        RobotControls controls =
            new RobotControls(ControlConfiguration.JOYSTICK_DRIVE_XBOX_OPERATOR);

        assertDriveAxes(
            controls,
            Constants.Operator.USB_0,
            Joystick.AxisType.kY.value,
            Constants.Operator.USB_1,
            Joystick.AxisType.kY.value);
        assertAction(
            controls,
            Constants.Operator.USB_1,
            1,
            Action.SLOW_DRIVE);
        assertAction(
            controls,
            Constants.Operator.USB_2,
            XboxController.Button.kA.value,
            Action.BELT_IN);
        assertAction(
            controls,
            Constants.Operator.USB_2,
            XboxController.Button.kB.value,
            Action.BELT_OUT);
        assertAction(
            controls,
            Constants.Operator.USB_2,
            XboxController.Button.kX.value,
            Action.RUN_ROLLER);
        assertAction(
            controls,
            Constants.Operator.USB_2,
            XboxController.Button.kY.value,
            Action.TOGGLE_CLIMBER);
    }

    private void assertDriveAxes(
        RobotControls controls,
        int leftPort,
        int leftAxis,
        int rightPort,
        int rightAxis) {
        DriverStationSim.setJoystickAxis(leftPort, leftAxis, -0.54);
        DriverStationSim.setJoystickAxis(rightPort, rightAxis, 0.04);
        DriverStationSim.notifyNewData();

        assertEquals(0.5, controls.getLeftDriveValue(), TOLERANCE);
        assertEquals(0.0, controls.getRightDriveValue(), TOLERANCE);

        DriverStationSim.setJoystickAxis(leftPort, leftAxis, -0.04);
        DriverStationSim.setJoystickAxis(rightPort, rightAxis, 0.54);
        DriverStationSim.notifyNewData();

        assertEquals(0.0, controls.getLeftDriveValue(), TOLERANCE);
        assertEquals(-0.5, controls.getRightDriveValue(), TOLERANCE);
    }

    private void assertAction(
        RobotControls controls,
        int port,
        int button,
        Action expectedAction) {
        DriverStationSim.setJoystickButton(port, button, true);
        DriverStationSim.notifyNewData();

        assertEquals(expectedAction == Action.SLOW_DRIVE, controls.slowDrive().getAsBoolean());
        assertEquals(expectedAction == Action.BELT_IN, controls.beltIn().getAsBoolean());
        assertEquals(expectedAction == Action.BELT_OUT, controls.beltOut().getAsBoolean());
        assertEquals(expectedAction == Action.RUN_ROLLER, controls.runRoller().getAsBoolean());
        assertEquals(
            expectedAction == Action.TOGGLE_CLIMBER,
            controls.toggleClimber().getAsBoolean());

        DriverStationSim.setJoystickButton(port, button, false);
        DriverStationSim.notifyNewData();

        assertFalse(controls.slowDrive().getAsBoolean());
        assertFalse(controls.beltIn().getAsBoolean());
        assertFalse(controls.beltOut().getAsBoolean());
        assertFalse(controls.runRoller().getAsBoolean());
        assertFalse(controls.toggleClimber().getAsBoolean());
    }

    private enum Action {
        SLOW_DRIVE,
        BELT_IN,
        BELT_OUT,
        RUN_ROLLER,
        TOGGLE_CLIMBER
    }
}
