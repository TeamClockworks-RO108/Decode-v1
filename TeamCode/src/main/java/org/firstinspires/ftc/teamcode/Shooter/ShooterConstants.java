package org.firstinspires.ftc.teamcode.Shooter;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

@Config
public class ShooterConstants {
    public static double kp = 380;
    public static double ki = 40;
    public static double kd = 20;
    public static double kf = 0;
    public static PIDFCoefficients flywheelCoeffs = new PIDFCoefficients(
            kp, ki, kd, kf
    );

    public static boolean active = false;
    public static double target = 1180;
}
