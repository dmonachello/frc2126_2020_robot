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
        public static final int CLIMBER_SOLENOID_FORWARD = 0;
        public static final int CLIMBER_SOLENOID_REVERSE = 1;

        public static final Value CLIMBER_ARMS_OUT = Value.kForward;
        public static final Value CLIMBER_ARMS_IN = Value.kReverse;

        /** NAME: Hardware - prevents construction of a constants-only group. */
        private Hardware() {
        }
    }

    /** NAME: Operator - controller USB ports. */
    public static final class Operator {
        public static final int DRIVER_CONTROLLER = 0;
        public static final int OPERATOR_CONTROLLER = 1;

        /** NAME: Operator - prevents construction of a constants-only group. */
        private Operator() {
        }
    }

    /** NAME: Tuning - named speed and scaling values used by commands. */
    public static final class Tuning {
        public static final double ROLLER_SPEED = 1.0;
        public static final double BELT_SPEED = 1.0;
        public static final double NORMAL_DRIVE_SCALE = 0.40;
        public static final double SLOW_DRIVE_SCALE = 0.20;
        public static final double DRIVE_DEADBAND = 0.08;
        public static final double DRIVE_SLEW_RATE_LIMIT = 0.8;

        /** NAME: Tuning - prevents construction of a constants-only group. */
        private Tuning() {
        }
    }
}
