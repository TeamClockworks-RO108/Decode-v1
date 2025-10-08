package org.firstinspires.ftc.teamcode.util;

public class AngleMapper {
    public static double map(double minAngle, double maxAngle, double angle){
        return angle / (maxAngle - minAngle);
    }
}
