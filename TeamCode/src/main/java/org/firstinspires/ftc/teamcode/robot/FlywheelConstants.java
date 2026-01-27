package org.firstinspires.ftc.teamcode.robot;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

@Configurable
public class FlywheelConstants {
    public static double kp = 1000;
    public static double ki = 50;
    public static double kd = 33;
    public static double kf = 0;

    public static double target = 1100; //1100

    public static double farTarget = 1200; //1300
}
