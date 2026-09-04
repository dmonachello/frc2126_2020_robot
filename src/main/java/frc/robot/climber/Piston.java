package frc.robot.climber;

import edu.wpi.first.wpilibj.DoubleSolenoid;



public class Piston
{
    private final DoubleSolenoid solenoid;
    private final DoubleSolenoid.Value in;
    private final DoubleSolenoid.Value out;
    public Piston (DoubleSolenoid solenoid, DoubleSolenoid.Value in, DoubleSolenoid.Value out)
    {
        this.solenoid = solenoid;
        this.in = in;
        this.out = out;
    }
    public void out()
    {
        solenoid.set(out);
    }
    public void in()
    {
        solenoid.set(in);
    }
}