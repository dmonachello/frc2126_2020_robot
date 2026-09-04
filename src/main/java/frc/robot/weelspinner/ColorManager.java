package frc.robot.weelspinner;

import com.revrobotics.ColorSensorV3;

public class ColorManager
{
    private final ColorSensorV3 colorSensor;
    private final ColorManager colorManager;

    private ColorManager (ColorSensorV3 colorSensor, ColorManager colorManager)
    {
        this.colorSensor = colorSensor;
        this.colorManager = colorManager;
    }
}