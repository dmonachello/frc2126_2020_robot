package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.controls.Controls;
import frc.robot.subsystems.GimbalSubsystem;

// Stage 2 migration note:
// The gimbal is still controlled as relative servo motion each cycle, matching the 2020 code.
public class GimbalTeleopCommand extends RunCommand {
    public GimbalTeleopCommand(GimbalSubsystem gimbalSubsystem, Controls controls) {
        super(
            () -> {
                gimbalSubsystem.gimbalHorizontalRelative(controls.gimbalXValue() * 0.01);
                gimbalSubsystem.gimbalVerticalRelative(controls.gimbalYValue() * 0.01);
            },
            gimbalSubsystem);
    }
}
