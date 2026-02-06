package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.util.StateMachine;

public class Outtake implements Subsystem {
    public enum State {
        OFF,
        CHARGING,
        RAISED,
        LAUNCHING,
        RELOADING
    }

    private final StateMachine<State> fsm = new StateMachine<>(State.OFF);
    private State targetState = State.OFF;

    private final DcMotorEx flywheel;
    private final Servo flap;
    private final Servo barrier;

    private final double flapDown = 0.3;
    private final double flapUp = 0.02;

    private final double barrierUp = 0.95;
    private final double barrierDown = 0.40;

    private PIDFCoefficients constants;

    private Telemetry telemetry;

    private double targetVelocity;

    public Outtake(HardwareMap hardwareMap, Telemetry telemetry) {
        flywheel = hardwareMap.get(DcMotorEx.class, "flywheel");
        flap = hardwareMap.get(Servo.class, "flap");
        barrier = hardwareMap.get(Servo.class, "barrier");

        this.telemetry = telemetry;

        constants = FlywheelConstants.getPIDF();

        flywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        flywheel.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER,
                constants);

        flap.setDirection(Servo.Direction.REVERSE);

        setupFSM();
    }

    public void off() {
        targetState = State.OFF;
    }
    public void charge() {
        targetState = State.CHARGING;
    }
    public void raise() {
        targetState = State.RAISED;
    }
    public void launch() {
        targetState = State.LAUNCHING;
    }
    public void reload() {
        targetState = State.RELOADING;
    }

    @Override
    public void update() {
        fsm.update();
        flywheel.setVelocity(targetVelocity);

        boolean changed = constants.d != FlywheelConstants.kd ||
                constants.p != FlywheelConstants.kp ||
                constants.i != FlywheelConstants.ki ||
                constants.f != FlywheelConstants.kf;

        if (changed) {
            constants = FlywheelConstants.getPIDF();
            flywheel.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER,
                    constants);
        }
    }

    private void setupFSM() {
        fsm.onStateEnter(State.OFF, () -> {
            targetVelocity = 0;
        });
        fsm.onStateUpdate(State.OFF, () -> {
            if (targetState == State.CHARGING) return State.CHARGING;
            return null;
        });

        fsm.onStateEnter(State.CHARGING, () -> {
            targetVelocity = FlywheelConstants.target;
            barrier.setPosition(barrierDown);
            flap.setPosition(flapDown);
        });
        fsm.onStateUpdate(State.CHARGING, () -> {
            if (targetState == State.OFF) return State.OFF;
            if (targetState == State.RAISED) return State.RAISED;
            return null;
        });

        fsm.onStateEnter(State.RAISED, () -> {
            barrier.setPosition(barrierUp);
        });
        fsm.onStateUpdate(State.RAISED, () -> {
            if (targetState == State.LAUNCHING) return State.LAUNCHING;
            return null;
        });

        fsm.onStateEnter(State.LAUNCHING, () -> {
            flap.setPosition(flapUp);
        });
        fsm.onStateUpdate(State.LAUNCHING, () -> {
            if (targetState == State.RELOADING) return State.RELOADING;
            if (targetState == State.CHARGING) return State.CHARGING;
            return null;
        });

        fsm.onStateEnter(State.RELOADING, () -> {
            flap.setPosition(flapDown);
        });
        fsm.onStateUpdate(State.RELOADING, () -> {
            if (targetState == State.LAUNCHING) return State.LAUNCHING;
            if (targetState == State.CHARGING) return State.CHARGING;
            return null;
        });
    }
}
