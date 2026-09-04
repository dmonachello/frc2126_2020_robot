package frc.robot.ultrasonic;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Ultrasonic {
  // A MB1013 distance sensor - http://www.maxbotix.com/documents/HRLV-MaxSonar-EZ_Datasheet.pdf
  // (pins 3, 6 and 7 from sensor to analog input 0)
  private static final AnalogInput mb1013 = new AnalogInput(0);
  
  // TODO - You will need to determine how to convert voltage to distance
  // (use information from the data sheet, or your own measurements)
  private  final double VOLTS_TO_DIST = .977;
  private final int channel;

  public Ultrasonic(int channel)
  {
     this.channel = channel;
  }

  public double getVoltage() {
    return mb1013.getVoltage();
  }
  
  public double getDistance() {
    return getVoltage() * VOLTS_TO_DIST;
  }
  
  public void updateDashboard() {
    SmartDashboard.putNumber("Distance (volts)", getVoltage());
    SmartDashboard.putNumber("Distance (real)", getDistance());
  }
}