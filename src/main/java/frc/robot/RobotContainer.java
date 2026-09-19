package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
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
    private final CommandXboxController driverController;
    private final CommandXboxController operatorController;
    private final DriveSubsystem driveSubsystem;
    private final ClimberSubsystem climberSubsystem;
    private final BeltSubsystem beltSubsystem;
    private final RollerSubsystem rollerSubsystem;
    private final DriveSpeedMode driveSpeedMode;

    /** NAME: RobotContainer - creates live controls and robot subsystems. SIDE EFFECTS: starts USB camera capture and installs commands. */
    public RobotContainer() {
        driverController = new CommandXboxController(Constants.Operator.DRIVER_CONTROLLER);
        operatorController = new CommandXboxController(Constants.Operator.OPERATOR_CONTROLLER);
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

    /** NAME: configureBindings - maps discrete controller buttons to commands. */
    private void configureBindings() {
        // Every discrete controller action is declared here. The default drive command reads
        // the driver's two stick axes continuously.
        // Y toggles the arms once. Releasing the button does not command pneumatic motion.
        operatorController.y()
            .onTrue(new ToggleClimberArmsCommand(climberSubsystem));

        // The belt and roller have independent motors, so each gets a separate subsystem and
        // binding. This allows all mechanism controls to run simultaneously.
        // whileTrue starts a mechanism command when its button is pressed and cancels it when the
        // button is released. Each belt or roller command stops its motor from end() afterward.
        operatorController.a()
            .whileTrue(new BeltInCommand(beltSubsystem));
        operatorController.b()
            .whileTrue(new BeltOutCommand(beltSubsystem));
        // The roller uses the same whileTrue lifecycle: releasing its button cancels
        // RollerCommand, which stops the roller motor from its end() method.
        operatorController.x()
            .whileTrue(new RollerCommand(rollerSubsystem));

        driverController.rightBumper()
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

    /** NAME: getLeftDriveValue - reads the oriented left Xbox stick. RETURNS: signed tank-drive input. */
    private double getLeftDriveValue() {
        return -MathUtil.applyDeadband(
            driverController.getLeftY(),
            Constants.Tuning.DRIVE_DEADBAND);
    }

    /** NAME: getRightDriveValue - reads the oriented right Xbox stick. RETURNS: signed tank-drive input. */
    private double getRightDriveValue() {
        return -MathUtil.applyDeadband(
            driverController.getRightY(),
            Constants.Tuning.DRIVE_DEADBAND);
    }

}
