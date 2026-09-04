package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.climber.Climber;

public class ClimberSubsystem extends SubsystemBase {
    private final Climber climber;

    public ClimberSubsystem(Climber climber) {
        this.climber = climber;
    }

    public void up() {
        climber.up();
    }

    public void down() {
        climber.down();
    }
}
