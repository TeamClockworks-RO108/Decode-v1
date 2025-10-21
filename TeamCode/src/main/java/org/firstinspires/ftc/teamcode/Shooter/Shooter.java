package org.firstinspires.ftc.teamcode.Shooter;

import android.util.Log;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.opmodes.ShooterCt;
import org.firstinspires.ftc.teamcode.util.StateMachine;

public class Shooter {
    private DcMotorEx flywheel;
    private DcMotor intakeMotor;
    private Servo flap;
    private Servo pivot;
    private Servo barrier;

    private final double intakePower = 0.7;
    private final double intakeIdlePower = 0.3;

    private final double intakeHumanPower = 0.5;

    private double flyWheelTicks = ShooterCt.target;
    private final double flapDown = 0.25;
    private final double flapUp = 0.5;

    private final double pivotDown = 0.1;
    private final double pivotIdle = 0.32;
    private final double pivotUp = 0.4;

    private final double barrierOff = 0.2;
    private final double barrierOn = 0.6;

    private double targetVelocity;
    private double flywheelVelocity;

    public double getFlywheelVelocity() {
        return flywheel.getVelocity();
    }

    public double getTargetVelocity() {
        return targetVelocity;
    }

    private enum State{
        INTAKE,
        IDLE,
        SHOOTING,
        BARRIER_RAISE, // launch chain
        FLAP_UP,
    }

    public enum Command{
        TOGGLE_SHOOTING,
        LAUNCH,
        TOGGLE_INTAKE,
        TOGGLE_IDLE
    }

    private StateMachine<State> fsm = new StateMachine<>(State.IDLE);
    private Command unexecutedCommand;

    public Shooter(HardwareMap hardwareMap){
        flywheel = hardwareMap.get(DcMotorEx.class, "flywheel");
        flywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        flap = hardwareMap.get(Servo.class, "flap");
        pivot = hardwareMap.get(Servo.class, "pivot");
        intakeMotor = hardwareMap.get(DcMotor.class, "intake");
        barrier = hardwareMap.get(Servo.class, "barrier");

        flap.setDirection(Servo.Direction.REVERSE);
        flap.setPosition(flapDown);
        pivot.setPosition(pivotUp);
        barrier.setPosition(barrierOn);
    }

    public void command(Command command) {
        this.unexecutedCommand = command;
    }

    public void setupShooter(){
        // IDLE -> human load position
        fsm.onStateEnter(State.IDLE, () -> {
            intakeMotor.setPower(intakeHumanPower);
            targetVelocity = 0;
            pivot.setPosition(pivotIdle);
        });
        fsm.onStateUpdate(State.IDLE, () -> {
            if (unexecutedCommand == Command.TOGGLE_INTAKE) {
                unexecutedCommand = null;
                return State.INTAKE;
            }
            if (unexecutedCommand == Command.TOGGLE_SHOOTING) {
                unexecutedCommand = null;
                return State.SHOOTING;
            }
            return null;
        });

        // SHOOTING -> launch position
        fsm.onStateEnter(State.SHOOTING,  () -> {
            targetVelocity = flyWheelTicks;
            intakeMotor.setPower(intakeIdlePower);
            pivot.setPosition(pivotUp);
        });
        fsm.onStateUpdate(State.SHOOTING,  () -> {
            if(unexecutedCommand == Command.TOGGLE_INTAKE) {
                unexecutedCommand = null;
                return State.INTAKE;
            }
            if (unexecutedCommand == Command.TOGGLE_IDLE) {
                unexecutedCommand = null;
                return State.IDLE;
            }
            if(unexecutedCommand == Command.LAUNCH){
                unexecutedCommand = null;
                return State.BARRIER_RAISE;
            }
            return null;
        });

        // BARRIER_RAISE and FLAP_UP -> launch sequence chain
        fsm.onStateEnter(State.BARRIER_RAISE, () -> {
            barrier.setPosition(barrierOff);
        });
        fsm.onStateUpdate(State.BARRIER_RAISE, (current, timeSinceTransition) -> {
            if (timeSinceTransition > 200) {
                return State.FLAP_UP;
            }
            return null;
        });
        fsm.onStateEnter(State.FLAP_UP, () -> {
            flap.setPosition(flapUp);
        });
        fsm.onStateUpdate(State.FLAP_UP, (current, timeSinceTransition) -> {
            if(timeSinceTransition > 200){
                return State.SHOOTING;
            }
            return null;
        });
        fsm.onStateExit(State.FLAP_UP,  () -> {
            flap.setPosition(flapDown);
            barrier.setPosition(barrierOn);
        });

        // INTAKE -> intake mechanism
        fsm.onStateEnter(State.INTAKE, () -> {
            intakeMotor.setPower(intakePower);
            targetVelocity = 0;
            pivot.setPosition(pivotDown);
        });
        fsm.onStateUpdate(State.INTAKE, () -> {
            if (unexecutedCommand == Command.TOGGLE_SHOOTING) {
                unexecutedCommand = null;
                return State.SHOOTING;
            }
            if (unexecutedCommand == Command.TOGGLE_IDLE) {
                unexecutedCommand = null;
                return State.IDLE;
            }
            return null;
        });

        // final initialisation
        fsm.init();
    }

    public void updateShooter(Telemetry telemetry){
        fsm.update();
        flywheel.setVelocity(targetVelocity);
        if (flyWheelTicks != ShooterCt.target)
            flyWheelTicks = ShooterCt.target;
        if (ShooterCt.active) {
            PIDFCoefficients coef = flywheel.getPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER);
            boolean changed = false;
            if (coef.f != ShooterCt.kf) {
                changed = true;
                coef.f = ShooterCt.kf;
            }
            if (coef.p != ShooterCt.kp) {
                changed = true;
                coef.p = ShooterCt.kp;
            }
            if (coef.i != ShooterCt.ki) {
                changed = true;
                coef.i = ShooterCt.ki;
            }
            if (coef.d != ShooterCt.kd) {
                changed = true;
                coef.d = ShooterCt.kd;
            }
            if (changed) {
                flywheel.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, coef);
            }
        }

    }
}
