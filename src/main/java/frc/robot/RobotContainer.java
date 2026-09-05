package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.BeltInCommand;
import frc.robot.commands.BeltOutCommand;
import frc.robot.commands.ToggleClimberArmsCommand;
import frc.robot.commands.DriveTeleopCommand;
import frc.robot.commands.RollerCommand;
import frc.robot.commands.SlowDriveCommand;
import frc.robot.subsystems.BeltSubsystem;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.RollerSubsystem;

/**
 * NAME
 *     RobotContainer - owns controllers, subsystems, default commands, and button bindings.
 *
 * DESCRIPTION
 *     This is the command-based wiring point for the live robot.
 */
public class RobotContainer {
    private final Joystick joystickLeft;
    private final Joystick joystickRight;
    private final Joystick gamepad;
    private final DriveSubsystem driveSubsystem;
    private final ClimberSubsystem climberSubsystem;
    private final BeltSubsystem beltSubsystem;
    private final RollerSubsystem rollerSubsystem;
    private final DriveSpeedMode driveSpeedMode;

    /** NAME: RobotContainer - creates live controls and robot subsystems. SIDE EFFECTS: starts USB camera capture and installs commands. */
    public RobotContainer() {
        joystickLeft = new Joystick(Constants.Operator.LEFT_JOYSTICK);
        joystickRight = new Joystick(Constants.Operator.RIGHT_JOYSTICK);
        gamepad = new Joystick(Constants.Operator.GAMEPAD);
        driveSubsystem = new DriveSubsystem();
        climberSubsystem = new ClimberSubsystem();
        beltSubsystem = new BeltSubsystem();
        rollerSubsystem = new RollerSubsystem();
        this.driveSpeedMode = new DriveSpeedMode();

        CameraServer.startAutomaticCapture(0);

        configureDefaultCommands();
        configureBindings();
    }

    /** NAME: resetRobot - stops active drive and ball-handling outputs. */
    public void resetRobot() {
        driveSubsystem.drive(0, 0);
        beltSubsystem.stop();
        rollerSubsystem.stop();
    }

    /** NAME: drive - commands tank-drive output. PARAMETERS: left - left output; right - right output. */
    public void drive(double left, double right) {
        driveSubsystem.drive(left, right);
    }

    /** NAME: intake - commands the belt. PARAMETERS: speed - signed belt output. */
    public void intake(double speed) {
        beltSubsystem.run(speed);
    }

    /** NAME: outtake - commands the roller. PARAMETERS: speed - signed roller output. */
    public void outtake(double speed) {
        rollerSubsystem.run(speed);
    }

    /** NAME: configureBindings - maps discrete controller buttons to commands. */
    private void configureBindings() {
        // Every discrete operator action is declared here. Default commands below are reserved
        // for continuous inputs, such as the two drive-stick axes.
        // Each press toggles the arms. Releasing the button does not command pneumatic motion.
        new JoystickButton(gamepad, Constants.Operator.CLIMBER_BUTTON)
            .onTrue(new ToggleClimberArmsCommand(climberSubsystem));

        // The belt and roller have independent motors, so each gets a separate subsystem and
        // binding. This allows all mechanism controls to run simultaneously.
        new JoystickButton(gamepad, Constants.Operator.BELT_IN_BUTTON)
            .whileTrue(new BeltInCommand(beltSubsystem));
        new JoystickButton(gamepad, Constants.Operator.BELT_OUT_BUTTON)
            .whileTrue(new BeltOutCommand(beltSubsystem));
        new JoystickButton(gamepad, Constants.Operator.ROLLER_BUTTON)
            .whileTrue(new RollerCommand(rollerSubsystem));

        new JoystickButton(joystickLeft, Constants.Operator.DRIVE_SLOW_BUTTON)
            .whileTrue(new SlowDriveCommand(driveSpeedMode));
    }

    /** NAME: configureDefaultCommands - installs continuous behavior for unclaimed subsystems. */
    private void configureDefaultCommands() {
        // The drive axes are continuous controls, so driving remains the only default command.
        driveSubsystem.setDefaultCommand(
            new DriveTeleopCommand(
                driveSubsystem,
                this::getLeftDriveValue,
                this::getRightDriveValue,
                driveSpeedMode));
    }

    /** NAME: getLeftDriveValue - reads the oriented left drive axis. RETURNS: signed tank-drive input. */
    private double getLeftDriveValue() {
        return -joystickLeft.getRawAxis(Constants.Operator.LEFT_DRIVE_AXIS);
    }

    /** NAME: getRightDriveValue - reads the oriented right drive axis. RETURNS: signed tank-drive input. */
    private double getRightDriveValue() {
        return -joystickRight.getRawAxis(Constants.Operator.RIGHT_DRIVE_AXIS);
    }

}
