package org.firstinspires.ftc.teamcode.robot;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

@Configurable
public class FlywheelConstants {
    public static double kp = 300;
    public static double ki = 14;
    public static double kd = 5;
    public static double kf = 0;
    public static double target = 1250;

    public static PIDFCoefficients getPIDF() {
        return new PIDFCoefficients(kp, ki, kd, kf);
    }
}