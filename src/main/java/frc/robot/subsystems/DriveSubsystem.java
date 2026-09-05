package frc.robot.subsystems;

import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.motorcontrol.Talon;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.motorcontrol.DualMotorController;

/**
 * NAME
 *     DriveSubsystem - owns the drive motors.
 *
 * DESCRIPTION
 *     Receives left and right tank-drive speeds and sends them to the two drive motor pairs.
 */
public class DriveSubsystem extends SubsystemBase {
    private final MotorController leftDriveMotors;
    private final MotorController rightDriveMotors;

    /**
     * NAME
     *     DriveSubsystem - creates the drive motors from Constants.
     */
    public DriveSubsystem() {
        MotorController frontLeftMotor = new Talon(Constants.Hardware.PWM_FRONT_LEFT_DRIVE);
        MotorController backLeftMotor = new Talon(Constants.Hardware.PWM_BACK_LEFT_DRIVE);
        MotorController frontRightMotor = new Talon(Constants.Hardware.PWM_FRONT_RIGHT_DRIVE);
        MotorController backRightMotor = new Talon(Constants.Hardware.PWM_BACK_RIGHT_DRIVE);

        leftDriveMotors = new DualMotorController(frontLeftMotor, backLeftMotor);
        rightDriveMotors = new DualMotorController(frontRightMotor, backRightMotor);
        rightDriveMotors.setInverted(true);
    }

    /**
     * NAME
     *     drive - commands tank-drive motion.
     *
     * PARAMETERS
     *     leftSpeed - requested left-side speed; positive is forward.
     *     rightSpeed - requested right-side speed; positive is forward.
     */
    public void drive(double leftSpeed, double rightSpeed) {
        leftDriveMotors.set(leftSpeed);
        rightDriveMotors.set(rightSpeed);
    }
}
