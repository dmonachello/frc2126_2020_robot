package frc.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.BallManipulatorTeleopCommand;
import frc.robot.commands.ClimberTeleopCommand;
import frc.robot.commands.DriveTeleopCommand;
import frc.robot.pinout.PinOut;
import frc.robot.subsystems.BallManipulatorSubsystem;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.ultrasonic.Ultrasonic;

public class RobotContainer {
    private final PinOut pinout;
    private final Joystick joystickLeft;
    private final Joystick joystickRight;
    private final Joystick gamepad;
    private final DriveSubsystem driveSubsystem;
    private final ClimberSubsystem climberSubsystem;
    private final BallManipulatorSubsystem ballManipulatorSubsystem;
    private final Ultrasonic ultrasonicRight;
    private final Compressor compressor;

    public RobotContainer() {
        this(RobotHardware.createReal());
    }

    RobotContainer(RobotHardware hardware) {
        this.pinout = hardware.pinout;
        this.joystickLeft = hardware.joystickLeft;
        this.joystickRight = hardware.joystickRight;
        this.gamepad = hardware.gamepad;
        this.driveSubsystem = hardware.driveSubsystem;
        this.climberSubsystem = hardware.climberSubsystem;
        this.ballManipulatorSubsystem = hardware.ballManipulatorSubsystem;
        this.ultrasonicRight = hardware.ultrasonicRight;
        this.compressor = hardware.compressor;

        configureDefaultCommands();
        configureBindings();
    }

    public void enableCompressor() {
        compressor.enableDigital();
    }

    public void updateAutonomousDashboard() {
        ultrasonicRight.updateDashboard();
    }

    public void resetRobot() {
        driveSubsystem.drive(0, 0);
        ballManipulatorSubsystem.intake(0);
        ballManipulatorSubsystem.outtake(0);
    }

    public void drive(double left, double right) {
        driveSubsystem.drive(left, right);
    }

    public void intake(double speed) {
        ballManipulatorSubsystem.intake(speed);
    }

    public void outtake(double speed) {
        ballManipulatorSubsystem.outtake(speed);
    }

    private void configureBindings() {
        // This is the first real button-style command binding migrated out of Teleop.periodic().
        new Trigger(this::isReversePressed)
            .onTrue(new InstantCommand(driveSubsystem::reverseDrive, driveSubsystem));
    }

    private void configureDefaultCommands() {
        // Each default command mirrors one slice of the old Teleop.periodic() coordinator.
        // Stage 2 change: RobotContainer now supplies interpreted joystick values directly
        // instead of routing them through the legacy Controls helper.
        driveSubsystem.setDefaultCommand(
            new DriveTeleopCommand(driveSubsystem, this::getLeftDriveValue, this::getRightDriveValue));
        climberSubsystem.setDefaultCommand(
            new ClimberTeleopCommand(climberSubsystem, this::isClimberUpRequested));
        ballManipulatorSubsystem.setDefaultCommand(
            new BallManipulatorTeleopCommand(
                ballManipulatorSubsystem,
                this::getBeltCommandSpeed,
                this::getRollerCommandSpeed));
    }

    double getLeftDriveValue() {
        return applyDriveScale(-joystickLeft.getRawAxis(pinout.leftDriveAxis), joystickLeft);
    }

    double getRightDriveValue() {
        return applyDriveScale(-joystickRight.getRawAxis(pinout.rightDriveAxis), joystickRight);
    }

    boolean isClimberUpRequested() {
        return gamepad.getRawButton(pinout.climberButton);
    }

    boolean isReversePressed() {
        return joystickLeft.getRawButtonPressed(pinout.reverseButton)
            || joystickRight.getRawButtonPressed(pinout.reverseButton);
    }

    double getBeltCommandSpeed() {
        if (gamepad.getRawButton(pinout.beltInButton)) {
            return pinout.beltSpeed;
        }
        if (gamepad.getRawButton(pinout.beltOutButton)) {
            return -pinout.beltSpeed;
        }
        return 0;
    }

    double getRollerCommandSpeed() {
        if (joystickLeft.getRawButton(pinout.rollerButton)
            || joystickRight.getRawButton(pinout.rollerButton)) {
            return pinout.rollerSpeed;
        }
        return 0;
    }

    double applyDriveScale(double joystickValue, Joystick joystick) {
        if (joystick.getRawButton(pinout.driveSlowButton)) {
            return joystickValue * pinout.slowValue;
        }
        return joystickValue * pinout.normalValue;
    }

    private static double deadZone(double joystickValue, double range) {
        if (Math.abs(joystickValue) <= range) {
            return 0.0;
        }
        return joystickValue;
    }
}
