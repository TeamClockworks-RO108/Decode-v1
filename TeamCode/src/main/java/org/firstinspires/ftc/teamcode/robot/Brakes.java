package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Brakes {
    private static final double brakeRightOff = 0.75, brakeRightOn = 0.55;
    private static final double brakeLeftOff = 0.75, brakeLeftOn = 0.575;

    private final Servo brakeRight;
    private final Servo brakeLeft;

    public Brakes(HardwareMap hardwareMap) {
        brakeRight = hardwareMap.get(Servo.class, "brakeRight");
        brakeLeft = hardwareMap.get(Servo.class, "brakeLeft");

        brakeLeft.setDirection(Servo.Direction.REVERSE);
        off();
    }

    public void on() {
        brakeRight.setPosition(brakeRightOn);
        brakeLeft.setPosition(brakeLeftOn);
    }

    public void off() {
        brakeRight.setPosition(brakeRightOff);
        brakeLeft.setPosition(brakeLeftOff);
    }
}