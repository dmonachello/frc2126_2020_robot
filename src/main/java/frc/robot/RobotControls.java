package frc.robot;

import java.util.Objects;
import java.util.function.DoubleSupplier;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/** Maps the selected controller hardware to robot actions. */
public class RobotControls {
    private static final int JOYSTICK_BELT_IN_BUTTON = 1;
    private static final int JOYSTICK_BELT_OUT_BUTTON = 2;
    private static final int JOYSTICK_ROLLER_BUTTON = 3;
    private static final int JOYSTICK_TOGGLE_CLIMBER_BUTTON = 4;

    private final Mapping mapping;

    /** Creates the controller objects required by the selected Driver Station layout. */
    public RobotControls(ControlConfiguration configuration) {
        Objects.requireNonNull(configuration, "configuration");

        mapping = switch (configuration) {
            case XBOX_CONTROLLERS -> xboxControllers();
            case JOYSTICK_CONTROLLERS -> joystickControllers();
            case JOYSTICK_DRIVE_XBOX_OPERATOR -> joystickDriveXboxOperator();
        };
    }

    /** Returns the normalized left drivetrain input. Positive values mean forward. */
    public double getLeftDriveValue() {
        return normalizeDriveValue(mapping.leftDriveAxis().getAsDouble());
    }

    /** Returns the normalized right drivetrain input. Positive values mean forward. */
    public double getRightDriveValue() {
        return normalizeDriveValue(mapping.rightDriveAxis().getAsDouble());
    }

    public Trigger slowDrive() {
        return mapping.slowDrive();
    }

    public Trigger beltIn() {
        return mapping.beltIn();
    }

    public Trigger beltOut() {
        return mapping.beltOut();
    }

    public Trigger runRoller() {
        return mapping.runRoller();
    }

    public Trigger toggleClimber() {
        return mapping.toggleClimber();
    }

    private static Mapping xboxControllers() {
        CommandXboxController driver =
            new CommandXboxController(Constants.Operator.USB_0);
        CommandXboxController operator =
            new CommandXboxController(Constants.Operator.USB_1);

        return new Mapping(
            driver::getLeftY,
            driver::getRightY,
            driver.rightBumper(),
            operator.a(),
            operator.b(),
            operator.x(),
            operator.y());
    }

    private static Mapping joystickControllers() {
        CommandJoystick leftDrive =
            new CommandJoystick(Constants.Operator.USB_0);
        CommandJoystick rightDrive =
            new CommandJoystick(Constants.Operator.USB_1);
        CommandJoystick operator =
            new CommandJoystick(Constants.Operator.USB_2);

        return new Mapping(
            leftDrive::getY,
            rightDrive::getY,
            rightDrive.trigger(),
            operator.button(JOYSTICK_BELT_IN_BUTTON),
            operator.button(JOYSTICK_BELT_OUT_BUTTON),
            operator.button(JOYSTICK_ROLLER_BUTTON),
            operator.button(JOYSTICK_TOGGLE_CLIMBER_BUTTON));
    }

    private static Mapping joystickDriveXboxOperator() {
        CommandJoystick leftDrive =
            new CommandJoystick(Constants.Operator.USB_0);
        CommandJoystick rightDrive =
            new CommandJoystick(Constants.Operator.USB_1);
        CommandXboxController operator =
            new CommandXboxController(Constants.Operator.USB_2);

        return new Mapping(
            leftDrive::getY,
            rightDrive::getY,
            rightDrive.trigger(),
            operator.a(),
            operator.b(),
            operator.x(),
            operator.y());
    }

    private static double normalizeDriveValue(double rawValue) {
        return -MathUtil.applyDeadband(rawValue, Constants.Tuning.DRIVE_DEADBAND);
    }

    private record Mapping(
        DoubleSupplier leftDriveAxis,
        DoubleSupplier rightDriveAxis,
        Trigger slowDrive,
        Trigger beltIn,
        Trigger beltOut,
        Trigger runRoller,
        Trigger toggleClimber) {
    }
}
