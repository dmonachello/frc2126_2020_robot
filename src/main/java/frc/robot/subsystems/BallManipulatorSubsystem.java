package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.ballmanipulator.BallManipulator;

public class BallManipulatorSubsystem extends SubsystemBase {
    private final BallManipulator ballManipulator;

    public BallManipulatorSubsystem(BallManipulator ballManipulator) {
        this.ballManipulator = ballManipulator;
    }

    public void intake(double speed) {
        ballManipulator.intake(speed);
    }

    public void outtake(double speed) {
        ballManipulator.outtake(speed);
    }
}
