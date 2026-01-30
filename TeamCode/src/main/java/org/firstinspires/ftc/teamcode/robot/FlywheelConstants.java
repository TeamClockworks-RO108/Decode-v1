package org.firstinspires.ftc.teamcode.robot;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

@Configurable
public class FlywheelConstants {
    public static double kp = 300;
    public static double ki = 14;
    public static double kd = 5;
    public static double kf = 0;

    public static double aimingTarget;

    public static double target = 1250; //1100

    public static double middleTarget = 1325; //1300
    public static double centerTarget = 1525; //1300

    public static void setTargetClose(){
        aimingTarget= target;
    }

    public static void setTargetCenter(){
        aimingTarget = centerTarget;
    }

    public static void setTargetMiddle(){
        aimingTarget = middleTarget;
    }
}
