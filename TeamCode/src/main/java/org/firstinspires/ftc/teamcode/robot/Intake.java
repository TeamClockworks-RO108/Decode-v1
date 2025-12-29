package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Intake {
    private final DcMotor intakeMotor;
    private final CRServo miniIntake;
    private final Servo leftGripper;
    private final Servo rightGripper;

    private final double intakePower = 0.8;
    private final double pushPower = 0.5;
    private final double shootingPower = 0.1;
    private final double idlePower = 0.4;
    private final double miniPower = 1;

    private final double leftGripperOpen = 0.48;
    private final double leftGripperClosed = 0.33;
    private final double rightGripperOpen = 0.45;
    private final double rightGripperClosed = 0.35;

    public Intake(HardwareMap hardwareMap) {
        intakeMotor = hardwareMap.get(DcMotor.class, "intake");
        miniIntake = hardwareMap.get(CRServo.class, "miniIntake");
        leftGripper = hardwareMap.get(Servo.class, "leftGripper");
        rightGripper = hardwareMap.get(Servo.class, "rightGripper");

        miniIntake.setDirection(DcMotorSimple.Direction.REVERSE);
        leftGripper.setDirection(Servo.Direction.REVERSE);
    }

    public void stop() {
        intakeMotor.setPower(0);
        miniIntake.setPower(0);
    }
    public void start() {
        intakeMotor.setPower(intakePower);
        miniIntake.setPower(miniPower);
        openGripper();
    }
    public void push() {
        intakeMotor.setPower(pushPower);
        openGripper();
    }
    public void idle() {
        intakeMotor.setPower(idlePower);
        miniIntake.setPower(miniPower);
    }
    public void shoot() {
        intakeMotor.setPower(shootingPower);
        miniIntake.setPower(miniPower);
    }
    public void reject() {
        intakeMotor.setPower(-intakePower);
        miniIntake.setPower(-miniPower);
        openGripper();
    }

    public void closeGripper() {
        leftGripper.setPosition(leftGripperClosed);
        rightGripper.setPosition(rightGripperClosed);
    }
    public void openGripper() {
        leftGripper.setPosition(leftGripperOpen);
        rightGripper.setPosition(rightGripperOpen);
    }
}
