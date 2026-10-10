package frc.robot;

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
    private final RobotControls robotControls;
    private final DriveSubsystem driveSubsystem;
    private final ClimberSubsystem climberSubsystem;
    private final BeltSubsystem beltSubsystem;
    private final RollerSubsystem rollerSubsystem;
    private final DriveSpeedMode driveSpeedMode;

    /** NAME: RobotContainer - creates live controls and robot subsystems. SIDE EFFECTS: starts USB camera capture and installs commands. */
    public RobotContainer() {
        robotControls = new RobotControls(Constants.Operator.CONTROL_CONFIGURATION);
        driveSubsystem = new DriveSubsystem();
        climberSubsystem = new ClimberSubsystem();
        beltSubsystem = new BeltSubsystem();
        rollerSubsystem = new RollerSubsystem();
        this.driveSpeedMode = new DriveSpeedMode();

        // CameraServer.startAutomaticCapture(0);

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
        // the two drive inputs continuously.
        // The climber action toggles the arms once. Releasing it does not command motion.
        robotControls.toggleClimber()
            .onTrue(new ToggleClimberArmsCommand(climberSubsystem));

        // The belt and roller have independent motors, so each gets a separate subsystem and
        // binding. This allows all mechanism controls to run simultaneously.
        // whileTrue starts a mechanism command when its control is pressed and cancels it when the
        // control is released. Each belt or roller command stops its motor from end() afterward.
        robotControls.beltIn()
            .whileTrue(new BeltInCommand(beltSubsystem));
        robotControls.beltOut()
            .whileTrue(new BeltOutCommand(beltSubsystem));
        // The roller uses the same whileTrue lifecycle: releasing its control cancels
        // RollerCommand, which stops the roller motor from its end() method.
        robotControls.runRoller()
            .whileTrue(new RollerCommand(rollerSubsystem));

        robotControls.slowDrive()
            .whileTrue(new SlowDriveCommand(driveSpeedMode));
    }

    /** NAME: configureDefaultCommands - installs continuous behavior for unclaimed subsystems. */
    private void configureDefaultCommands() {
        // The drive axes are continuous controls, so driving remains the only default command.
        driveSubsystem.setDefaultCommand(
            new DriveTeleopCommand(
                driveSubsystem,
                robotControls::getLeftDriveValue,
                robotControls::getRightDriveValue,
                driveSpeedMode));
    }
}
