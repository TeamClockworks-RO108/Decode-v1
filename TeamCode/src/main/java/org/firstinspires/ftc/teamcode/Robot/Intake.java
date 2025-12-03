package org.firstinspires.ftc.teamcode.Robot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Intake {
    private final DcMotor intakeMotor;
    private final Servo leftGripper;
    private final Servo rightGripper;

    private final double intakePower = 0.8;
    private final double shootingPower = 0.1;
    private final double idlePower = 0.4;

    private final double leftGripperOpen = 0.5;
    private final double leftGripperClosed = 0.33;
    private final double rightGripperOpen = 0.47;
    private final double rightGripperClosed = 0.35;

    public Intake(HardwareMap hardwareMap) {
        intakeMotor = hardwareMap.get(DcMotor.class, "intake");
        leftGripper = hardwareMap.get(Servo.class, "leftGripper");
        rightGripper = hardwareMap.get(Servo.class, "rightGripper");

        leftGripper.setDirection(Servo.Direction.REVERSE);
    }

    public void stop() {
        intakeMotor.setPower(0);
    }
    public void start() {
        intakeMotor.setPower(intakePower);
        openGripper();
    }
    public void idle() {
        intakeMotor.setPower(idlePower);
    }
    public void shoot() {
        intakeMotor.setPower(shootingPower);
    }
    public void reject() {
        intakeMotor.setPower(-intakePower);
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
