package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.PIDFCoefficients;

public class FlywheelConstants {
    public static double kp = 380;
    public static double ki = 40;
    public static double kd = 20;
    public static double kf = 0;
    public static PIDFCoefficients flywheelCoefficients = new PIDFCoefficients(
            kp, ki, kd, kf
    );

    public static double target = 1100;

    public static double farTarget = 3500;
}
