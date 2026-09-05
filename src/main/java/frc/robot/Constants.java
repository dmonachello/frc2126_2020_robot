package frc.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

/**
 * NAME
 *     Constants - central read-only robot configuration.
 *
 * DESCRIPTION
 *     Groups hardware channels, operator-controller mappings, and tuning values so code does
 *     not contain unexplained numeric literals.
 */
public final class Constants {
    /** NAME: Constants - prevents construction of a constants-only class. */
    private Constants() {
    }

    /** NAME: Hardware - CAN, PWM, DIO, and pneumatic configuration values. */
    public static final class Hardware {
        public static final int PWM_BACK_LEFT_DRIVE = 1;
        public static final int PWM_FRONT_LEFT_DRIVE = 0;
        public static final int PWM_BACK_RIGHT_DRIVE = 2;
        public static final int PWM_FRONT_RIGHT_DRIVE = 3;

        public static final int PWM_BELT = 5;
        public static final int PWM_ROLLER = 4;

        public static final int PCM = 0;
        public static final int PDB = 1;
        public static final int SOLENOID_LEFT_FORWARD = 0;
        public static final int SOLENOID_LEFT_REVERSE = 1;
        public static final int SOLENOID_RIGHT_FORWARD = 2;
        public static final int SOLENOID_RIGHT_REVERSE = 3;

        public static final Value SOLENOID_LEFT_OUT = Value.kForward;
        public static final Value SOLENOID_LEFT_IN = Value.kReverse;
        public static final Value SOLENOID_RIGHT_OUT = Value.kForward;
        public static final Value SOLENOID_RIGHT_IN = Value.kReverse;

        /** NAME: Hardware - prevents construction of a constants-only group. */
        private Hardware() {
        }
    }

    /** NAME: Operator - controller ports, axes, and button mappings. */
    public static final class Operator {
        public static final int LEFT_JOYSTICK = 0;
        public static final int RIGHT_JOYSTICK = 1;
        public static final int GAMEPAD = 2;

        public static final int LEFT_DRIVE_AXIS = 1;
        public static final int RIGHT_DRIVE_AXIS = 1;
        public static final int CLIMBER_BUTTON = 5;
        public static final int BELT_IN_BUTTON = 6;
        public static final int BELT_OUT_BUTTON = 8;
        public static final int ROLLER_BUTTON = 1;
        public static final int DRIVE_SLOW_BUTTON = 2;

        /** NAME: Operator - prevents construction of a constants-only group. */
        private Operator() {
        }
    }

    /** NAME: Tuning - named speed and scaling values used by commands. */
    public static final class Tuning {
        public static final double ROLLER_SPEED = 1.0;
        public static final double BELT_SPEED = 1.0;
        public static final double NORMAL_DRIVE_SCALE = 0.8;
        public static final double SLOW_DRIVE_SCALE = 0.4;

        /** NAME: Tuning - prevents construction of a constants-only group. */
        private Tuning() {
        }
    }
}
