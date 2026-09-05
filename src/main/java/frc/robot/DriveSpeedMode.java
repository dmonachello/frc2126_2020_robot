package frc.robot;

/**
 * NAME
 *     DriveSpeedMode - stores whether precision drive mode is active.
 *
 * DESCRIPTION
 *     The slow-drive command writes this state and the default drive command reads it.
 */
public class DriveSpeedMode {
    private boolean slow;

    /**
     * NAME
     *     isSlow - reports whether precision drive mode is active.
     *
     * RETURNS
     *     true while slow drive is active.
     */
    public boolean isSlow() {
        return slow;
    }

    /**
     * NAME
     *     setSlow - changes precision drive mode.
     *
     * PARAMETERS
     *     slow - requested precision-drive state.
     */
    public void setSlow(boolean slow) {
        this.slow = slow;
    }
}
