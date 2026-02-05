package org.firstinspires.ftc.teamcode.robot;

import com.bylazar.graph.GraphManager;
import com.bylazar.graph.PanelsGraph;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorImplEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDCoefficients;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Outtake {
    private final DcMotorEx flywheel;
    private final Servo flap;
    private final Servo barrier;

    private final double flapDown = 0.3;
    private final double flapUp = 0.02;

    private final double barrierUp = 0.95;
    private final double barrierDown = 0.40;

    private final PIDFCoefficients constants = new PIDFCoefficients();

    private Telemetry telemetry;

    private double targetVelocity;

    public Outtake(HardwareMap hardwareMap, Telemetry telemetry) {
        flywheel = hardwareMap.get(DcMotorEx.class, "flywheel");
        flap = hardwareMap.get(Servo.class, "flap");
        barrier = hardwareMap.get(Servo.class, "barrier");

        this.telemetry = telemetry;

        constants.d = FlywheelConstants.kd;
        constants.p = FlywheelConstants.kp;
        constants.i = FlywheelConstants.ki;
        constants.f = FlywheelConstants.kf;

        flywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        flywheel.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER,
                constants);

        flap.setDirection(Servo.Direction.REVERSE);
    }

    public void close() {
        flap.setPosition(flapDown);
        barrier.setPosition(barrierDown);
    }
    public void raise() {
        barrier.setPosition(barrierUp);
    }
    public void launch() {
        flap.setPosition(flapUp);
    }
    public void reload() {
        flap.setPosition(flapDown);
    }

    public void startFlywheel() {
        targetVelocity = FlywheelConstants.target;
    }
    public void stopFlywheel() {
        targetVelocity = 0;
    }
    public void update() {
        flywheel.setVelocity(targetVelocity);

        boolean changed = constants.d != FlywheelConstants.kd ||
                constants.p != FlywheelConstants.kp ||
                constants.i != FlywheelConstants.ki ||
                constants.f != FlywheelConstants.kf;

        if (changed) {
            constants.d = FlywheelConstants.kd;
            constants.p = FlywheelConstants.kp;
            constants.i = FlywheelConstants.ki;
            constants.f = FlywheelConstants.kf;
            flywheel.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER,
                    constants);
        }
    }}
