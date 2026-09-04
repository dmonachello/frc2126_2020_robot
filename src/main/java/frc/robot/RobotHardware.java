package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.motorcontrol.Talon;
import frc.robot.ballmanipulator.BallManipulator;
import frc.robot.ballmanipulator.Belts;
import frc.robot.ballmanipulator.Roller;
import frc.robot.climber.Climber;
import frc.robot.climber.Piston;
import frc.robot.drivebase.DriveBase;
import frc.robot.motorcontrol.DualMotorController;
import frc.robot.pinout.PinOut;
import frc.robot.subsystems.BallManipulatorSubsystem;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.ultrasonic.Ultrasonic;

// Stage 2 architecture note:
// RobotHardware owns concrete WPILib construction. RobotContainer consumes an already-built
// hardware bundle so tests can supply fakes without adding test-specific seams to runtime code.
public class RobotHardware {
    final PinOut pinout;
    final Joystick joystickLeft;
    final Joystick joystickRight;
    final Joystick gamepad;
    final DriveSubsystem driveSubsystem;
    final ClimberSubsystem climberSubsystem;
    final BallManipulatorSubsystem ballManipulatorSubsystem;
    final Ultrasonic ultrasonicRight;
    final Compressor compressor;

    RobotHardware(
        PinOut pinout,
        Joystick joystickLeft,
        Joystick joystickRight,
        Joystick gamepad,
        DriveSubsystem driveSubsystem,
        ClimberSubsystem climberSubsystem,
        BallManipulatorSubsystem ballManipulatorSubsystem,
        Ultrasonic ultrasonicRight,
        Compressor compressor) {
        this.pinout = pinout;
        this.joystickLeft = joystickLeft;
        this.joystickRight = joystickRight;
        this.gamepad = gamepad;
        this.driveSubsystem = driveSubsystem;
        this.climberSubsystem = climberSubsystem;
        this.ballManipulatorSubsystem = ballManipulatorSubsystem;
        this.ultrasonicRight = ultrasonicRight;
        this.compressor = compressor;
    }

    public static RobotHardware createReal() {
        PinOut pinout = new PinOut();

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
        DriveSubsystem driveSubsystem = new DriveSubsystem(new DriveBase(
            speedControllerGroupLeftDrive,
            speedControllerGroupRightDrive,
            (Encoder) null,
            (Encoder) null,
            pinout.rightDriveForward,
            leftLimitSwitch,
            rightLimitSwitch,
            pinout.leftLimitSwitchTrippedValue,
            pinout.rightLimitSwitchTrippedValue));

        Joystick joystickLeft = new Joystick(pinout.leftJoystickNum);
        Joystick joystickRight = new Joystick(pinout.rightJoystickNum);
        Joystick gamepad = new Joystick(pinout.gamepadNum);

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
        ClimberSubsystem climberSubsystem = new ClimberSubsystem(new Climber(pistonLeft, pistonRight));

        Compressor compressor = new Compressor(pinout.CAMpcm, PneumaticsModuleType.CTREPCM);

        MotorController speedControllerBelt = new Talon(pinout.PWMBelt);
        MotorController speedControllerRoller = new Talon(pinout.PWMRoller);
        Belts belt = new Belts(speedControllerBelt);
        Roller roller = new Roller(speedControllerRoller);
        BallManipulatorSubsystem ballManipulatorSubsystem = new BallManipulatorSubsystem(
            new BallManipulator(belt, roller, pinout.isBeltPositive, pinout.isRollerPositive));

        Ultrasonic ultrasonicRight = new Ultrasonic(pinout.ultrasonicRight);

        CameraServer.startAutomaticCapture(0);

        return new RobotHardware(
            pinout,
            joystickLeft,
            joystickRight,
            gamepad,
            driveSubsystem,
            climberSubsystem,
            ballManipulatorSubsystem,
            ultrasonicRight,
            compressor);
    }
}
