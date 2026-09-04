package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.motorcontrol.Talon;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.ballmanipulator.BallManipulator;
import frc.robot.ballmanipulator.Belts;
import frc.robot.ballmanipulator.Roller;
import frc.robot.climber.Climber;
import frc.robot.climber.Piston;
import frc.robot.commands.BallManipulatorTeleopCommand;
import frc.robot.commands.ClimberTeleopCommand;
import frc.robot.commands.DriveTeleopCommand;
import frc.robot.commands.GimbalTeleopCommand;
import frc.robot.commands.WheelSpinnerTeleopCommand;
import frc.robot.drivebase.DriveBase;
import frc.robot.gimbal.Gimbal;
import frc.robot.motorcontrol.DualMotorController;
import frc.robot.pinout.PinOut;
import frc.robot.subsystems.BallManipulatorSubsystem;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.GimbalSubsystem;
import frc.robot.subsystems.WheelSpinnerSubsystem;
import frc.robot.ultrasonic.Ultrasonic;
import frc.robot.weelspinner.WeelSpinner;

public class RobotContainer {
    private final PinOut pinout;
    private final Joystick joystickLeft;
    private final Joystick joystickRight;
    private final Joystick gamepad;
    private final DriveSubsystem driveSubsystem;
    private final ClimberSubsystem climberSubsystem;
    private final WheelSpinnerSubsystem wheelSpinnerSubsystem;
    private final BallManipulatorSubsystem ballManipulatorSubsystem;
    private final GimbalSubsystem gimbalSubsystem;
    private final Ultrasonic ultrasonicRight;
    private final Compressor compressor;

    public RobotContainer() {
        // PinOut still owns the original numeric constants so students can compare the
        // migrated command-based structure to the 2020 source without losing the wiring map.
        pinout = new PinOut();

        Servo servoXaxis = new Servo(pinout.PWMServoXAxis);
        Servo servoYaxis = new Servo(pinout.PWMServoYAxis);
        gimbalSubsystem = new GimbalSubsystem(new Gimbal(servoXaxis, servoYaxis));

        DigitalInput leftLimitSwitch = new DigitalInput(pinout.DIOleftLimitSwitch);
        DigitalInput rightLimitSwitch = new DigitalInput(pinout.DIOrightLimitSwitch);

        MotorController speedControllerFrontLeftDrive = new Talon(pinout.PWMfrontLeftDrive);
        MotorController speedControllerBackLeftDrive = new Talon(pinout.PWMbackLeftDrive);
        MotorController speedControllerFrontRightDrive = new Talon(pinout.PWMfrontRightDrive);
        MotorController speedControllerBackRightDrive = new Talon(pinout.PWMbackRightDrive);
        DualMotorController speedControllerGroupLeftDrive =
            new DualMotorController(speedControllerFrontLeftDrive, speedControllerBackLeftDrive);
        DualMotorController speedControllerGroupRightDrive =
            new DualMotorController(speedControllerFrontRightDrive, speedControllerBackRightDrive);
        // Stage 1 compatibility change: the old WPILib SpeedControllerGroup type was removed
        // from the active path and replaced by a small local wrapper with the same behavior.
        driveSubsystem = new DriveSubsystem(new DriveBase(
            speedControllerGroupLeftDrive,
            speedControllerGroupRightDrive,
            (Encoder) null,
            (Encoder) null,
            pinout.rightDriveForward,
            leftLimitSwitch,
            rightLimitSwitch,
            pinout.leftLimitSwitchTrippedValue,
            pinout.rightLimitSwitchTrippedValue));

        joystickLeft = new Joystick(pinout.leftJoystickNum);
        joystickRight = new Joystick(pinout.rightJoystickNum);
        gamepad = new Joystick(pinout.gamepadNum);
        // Stage 2 change on September 4, 2026:
        // joystick ownership moved directly into RobotContainer. This is closer to standard
        // WPILib command-based structure and removes Controls from the active teleop path.

        DoubleSolenoid leftSolenoid = new DoubleSolenoid(
            pinout.CAMpcm,
            PneumaticsModuleType.CTREPCM,
            pinout.ChannelSolenoidLeftForward,
            pinout.ChannelSolenoidLeftReverse);
        DoubleSolenoid rightSolenoid = new DoubleSolenoid(
            pinout.CAMpcm,
            PneumaticsModuleType.CTREPCM,
            pinout.ChannelSolenoidRightForward,
            pinout.ChannelSolenoidRightReverse);
        Piston pistonLeft = new Piston(leftSolenoid, pinout.solenoidLeftIn, pinout.solenoidLeftOut);
        Piston pistonRight = new Piston(rightSolenoid, pinout.solenoidRightIn, pinout.solenoidRightOut);
        climberSubsystem = new ClimberSubsystem(new Climber(pistonLeft, pistonRight));

        // Stage 1 compatibility change: 2026 WPILib pneumatics APIs require a module type.
        compressor = new Compressor(pinout.CAMpcm, PneumaticsModuleType.CTREPCM);

        MotorController spinner = new Talon(pinout.PWMspinner);
        wheelSpinnerSubsystem = new WheelSpinnerSubsystem(new WeelSpinner(spinner));

        MotorController speedControllerBelt = new Talon(pinout.PWMBelt);
        MotorController speedControllerRoller = new Talon(pinout.PWMRoller);
        Belts belt = new Belts(speedControllerBelt);
        Roller roller = new Roller(speedControllerRoller);
        ballManipulatorSubsystem = new BallManipulatorSubsystem(
            new BallManipulator(belt, roller, pinout.isBeltPositive, pinout.isRollerPositive));

        ultrasonicRight = new Ultrasonic(pinout.ultrasonicRight);

        // Stage 1 compatibility change: CameraServer.startAutomaticCapture(...) replaced the
        // older CameraServer.getInstance().startAutomaticCapture(...) usage.
        CameraServer.startAutomaticCapture(0);

        configureDefaultCommands();
        configureBindings();
    }

    // Stage 2 test seam:
    // The active robot uses the hardware-building constructor above. This package-private
    // constructor exists so unit tests can verify the input-mapping logic that replaced
    // Controls.java without creating real WPILib hardware objects.
    RobotContainer(
        PinOut pinout,
        Joystick joystickLeft,
        Joystick joystickRight,
        Joystick gamepad,
        DriveSubsystem driveSubsystem,
        ClimberSubsystem climberSubsystem,
        WheelSpinnerSubsystem wheelSpinnerSubsystem,
        BallManipulatorSubsystem ballManipulatorSubsystem,
        GimbalSubsystem gimbalSubsystem) {
        this.pinout = pinout;
        this.joystickLeft = joystickLeft;
        this.joystickRight = joystickRight;
        this.gamepad = gamepad;
        this.driveSubsystem = driveSubsystem;
        this.climberSubsystem = climberSubsystem;
        this.wheelSpinnerSubsystem = wheelSpinnerSubsystem;
        this.ballManipulatorSubsystem = ballManipulatorSubsystem;
        this.gimbalSubsystem = gimbalSubsystem;
        this.ultrasonicRight = null;
        this.compressor = null;
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
        wheelSpinnerSubsystem.spin(0);
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
        gimbalSubsystem.setDefaultCommand(
            new GimbalTeleopCommand(gimbalSubsystem, this::getGimbalXValue, this::getGimbalYValue));
        wheelSpinnerSubsystem.setDefaultCommand(
            new WheelSpinnerTeleopCommand(wheelSpinnerSubsystem, this::getSpinnerSpeed));
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

    double getSpinnerSpeed() {
        return deadZone(gamepad.getRawAxis(0), 0.25);
    }

    double getGimbalXValue() {
        return deadZone(gamepad.getRawAxis(pinout.gimbalXAxisChannel), 0.25);
    }

    double getGimbalYValue() {
        return deadZone(-gamepad.getRawAxis(pinout.gimbalYAxisChannel), 0.25);
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
