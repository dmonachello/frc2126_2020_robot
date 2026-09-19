package frc.robot;

import edu.wpi.first.wpilibj2.command.CommandScheduler;

/** Keeps the robot stopped while autonomous routines are disabled. */
final class AutonomousMode {
    private final RobotContainer robotContainer;

    AutonomousMode(RobotContainer robotContainer) {
        this.robotContainer = robotContainer;
    }

    /** Prevents default teleop commands from being scheduled during autonomous mode. */
    void enter() {
        CommandScheduler scheduler = CommandScheduler.getInstance();
        scheduler.cancelAll();
        scheduler.disable();
        robotContainer.resetRobot();
    }

    /** Restores command scheduling when the robot leaves autonomous mode. */
    void exit() {
        robotContainer.resetRobot();
        CommandScheduler.getInstance().enable();
    }

    /*
     * Disabled autonomous implementation retained for future work.
     *
     * private static final String kDontDoAnyThingAuto = "don't do anything";
     * private static final String kDriveForwardCountinouslyAuto = "drive forward continously";
     * private static final String kDriveForwardAndScore = "drive forward and score";
     *
     * private String m_autoSelected;
     * private final SendableChooser<String> m_chooser = new SendableChooser<>();
     * private enum DriveForwardScoreState {NotStarted, DrivingForward, Score}
     * private DriveForwardScoreState driveForwardScoreState;
     * private long drivingForwardStartTime;
     *
     * void configureChooser() {
     *     m_chooser.setDefaultOption("Drive Forawrd And Score", kDriveForwardAndScore);
     *     m_chooser.addOption("Drive Forward", kDriveForwardCountinouslyAuto);
     *     m_chooser.addOption("Don't Do Anything", kDontDoAnyThingAuto);
     *     SmartDashboard.putData("Auto choices", m_chooser);
     * }
     *
     * void initializeSelectedMode() {
     *     CommandScheduler.getInstance().cancelAll();
     *     driveForwardScoreState = DriveForwardScoreState.NotStarted;
     *     m_autoSelected = m_chooser.getSelected();
     *     System.out.println("Auto selected: " + m_autoSelected);
     *     robotContainer.resetRobot();
     * }
     *
     * void runSelectedMode() {
     *     switch (m_autoSelected) {
     *         case kDontDoAnyThingAuto:
     *             break;
     *         case kDriveForwardCountinouslyAuto:
     *             robotContainer.drive(-0.5, -0.5 * 0.975);
     *             break;
     *         case kDriveForwardAndScore:
     *         default:
     *             switch (driveForwardScoreState) {
     *                 case NotStarted:
     *                     drivingForwardStartTime = System.currentTimeMillis();
     *                     driveForwardScoreState = DriveForwardScoreState.DrivingForward;
     *                     break;
     *                 case DrivingForward:
     *                     robotContainer.drive(-0.5, -0.5 * 0.975);
     *                     if (System.currentTimeMillis() - drivingForwardStartTime >= 2500) {
     *                         driveForwardScoreState = DriveForwardScoreState.Score;
     *                     }
     *                     break;
     *                 case Score:
     *                     robotContainer.drive(0, 0);
     *                     robotContainer.outtake(1);
     *                     break;
     *             }
     *             break;
     *     }
     * }
     */
}
