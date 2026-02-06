package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.util.StateMachine;

public class Intake implements Subsystem {
    public enum State {
        OFF,
        INTAKE,
        REJECT,
        IDLE,
        SHOOT,
        PUSH,
    }

    private final StateMachine<State> fsm = new StateMachine<>(State.OFF);
    private State targetState = State.OFF;

    private final DcMotor intakeMotor;
    private final DcMotor miniIntake;
    private final Servo leftGripper;
    private final Servo rightGripper;

    private final double intakePower = 0.8;
    private final double pushPower = 0.9;
    private final double shootingPower = 0.1;
    private final double idlePower = 0.4;

    private final double leftGripperOpen = 0.45;
    private final double leftGripperClosed = 0.33;
    private final double rightGripperOpen = 0.425;
    private final double rightGripperClosed = 0.32;

    public Intake(HardwareMap hardwareMap) {
        intakeMotor = hardwareMap.get(DcMotor.class, "intake");
        miniIntake = hardwareMap.get(DcMotor.class, "miniIntake");
        leftGripper = hardwareMap.get(Servo.class, "leftGripper");
        rightGripper = hardwareMap.get(Servo.class, "rightGripper");

        leftGripper.setDirection(Servo.Direction.REVERSE);

        setupFSM();
    }

    public void update() {
        fsm.update();
    }

    public void intake() {
        targetState = State.INTAKE;
    }
    public void push() {
        targetState = State.PUSH;
    }
    public void idle() {
        targetState = State.IDLE;
    }
    public void shoot() {
        targetState = State.SHOOT;
    }
    public void reject() {
        targetState = State.REJECT;
    }

    private void closeGripper() {
        leftGripper.setPosition(leftGripperClosed);
        rightGripper.setPosition(rightGripperClosed);
    }
    private void openGripper() {
        leftGripper.setPosition(leftGripperOpen);
        rightGripper.setPosition(rightGripperOpen);
    }

    private void setupFSM() {
        fsm.onStateUpdate(State.OFF, () -> {
            if (targetState == State.IDLE) return State.IDLE;
            if (targetState == State.SHOOT) return State.SHOOT;
            if (targetState == State.INTAKE) return State.INTAKE;
            return null;
        });
        fsm.onStateExit(State.OFF, () -> {
            openGripper();
        });

        fsm.onStateEnter(State.INTAKE, () -> {
            intakeMotor.setPower(intakePower);
            miniIntake.setPower(intakePower);
            openGripper();
        });
        fsm.onStateUpdate(State.INTAKE, () -> {
            if (targetState == State.IDLE) return State.IDLE;
            if (targetState == State.SHOOT) return State.SHOOT;
            if (targetState == State.REJECT) return State.REJECT;
            return null;
        });
        fsm.onStateExit(State.INTAKE, () -> {
            closeGripper();
        });

        fsm.onStateEnter(State.REJECT, () -> {
            intakeMotor.setPower(-intakePower);
            miniIntake.setPower(-intakePower);
            openGripper();
        });
        fsm.onStateUpdate(State.INTAKE, () -> {
            if (targetState == State.IDLE) return State.IDLE;
            if (targetState == State.SHOOT) return State.SHOOT;
            if (targetState == State.INTAKE) return State.INTAKE;
            return null;
        });
        fsm.onStateExit(State.INTAKE, () -> {
            closeGripper();
        });

        fsm.onStateEnter(State.IDLE, () -> {
            intakeMotor.setPower(idlePower);
            miniIntake.setPower(idlePower);
        });
        fsm.onStateUpdate(State.IDLE, () -> {
            if (targetState == State.SHOOT) return State.SHOOT;
            if (targetState == State.INTAKE) return State.INTAKE;
            return null;
        });

        fsm.onStateEnter(State.SHOOT, () -> {
            intakeMotor.setPower(shootingPower);
            miniIntake.setPower(shootingPower);
        });
        fsm.onStateUpdate(State.SHOOT, () -> {
            if (targetState == State.IDLE) return State.IDLE;
            if (targetState == State.INTAKE) return State.INTAKE;
            if (targetState == State.PUSH) return State.PUSH;
            return null;
        });

        fsm.onStateEnter(State.PUSH, () -> {
            intakeMotor.setPower(pushPower);
            miniIntake.setPower(pushPower);
            openGripper();
        });
        fsm.onStateUpdate(State.PUSH, () -> {
            if (targetState == State.SHOOT) return State.SHOOT;
            return null;
        });
    }
}
