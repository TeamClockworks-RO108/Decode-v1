package org.firstinspires.ftc.teamcode.robot;

import android.adservices.adselection.ReportEventRequest;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Brakes {

    private final Servo brakeRight;
    private final Servo brakeLeft;

    private double brakeRightOn = 0.75, brakeRightOff = 0.55;
    private double brakeLeftOn = 0.75, brakeLeftOff =0.575;


    public Brakes(HardwareMap hardwareMap) {
       brakeRight = hardwareMap.get(Servo.class, "brakeRight");
       brakeLeft = hardwareMap.get(Servo.class, "brakeLeft");
       brakeLeft.setDirection(Servo.Direction.REVERSE);
       brakesOff();
    }

    public void brakesOn (){
        brakeRight.setPosition(brakeRightOn);
        brakeLeft.setPosition(brakeLeftOn);
    }

    public void brakesOff(){
        brakeRight.setPosition(brakeRightOff);
        brakeLeft.setPosition(brakeLeftOff);
    }
}
