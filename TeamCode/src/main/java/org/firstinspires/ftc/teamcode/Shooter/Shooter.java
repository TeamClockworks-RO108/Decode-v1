package org.firstinspires.ftc.teamcode.Shooter;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.util.StateMachine;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Shooter {
    private DcMotorEx flywheel;
    private DcMotor intakeMotor;
    private Servo flap;
    private Servo pivot;
    private Servo barrier;
    private Servo leftGripper;
    private Servo rightGripper;

    private final double intakePower = 0.8;
    private final double intakeShooterPower = 0.1;
    private final double intakeHumanPower = 0.5;

    private final double flapDown = 0.3;
    private final double flapUp = 0.6;

    private final double pivotDown = 0.68;
    private final double pivotIdle = 0.88;
    private final double pivotUp = 0.98;

    private final double barrierUp = 0.65;
    private final double barrierDown = 0.1;

    private boolean isAuto;

    private final double leftGripperOpen = 0.5;
    private final double leftGripperClosed = 0.32;
    private final double rightGripperOpen = 0.5;
    private final double rightGripperClosed = 0.35;

    private double targetVelocity;

    private final BlockingQueue<Command> queue = new ArrayBlockingQueue<>(16);

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

    public Shooter(HardwareMap hardwareMap, boolean isAuto){
        flywheel = hardwareMap.get(DcMotorEx.class, "flywheel");
        flap = hardwareMap.get(Servo.class, "flap");
        pivot = hardwareMap.get(Servo.class, "pivot");
        intakeMotor = hardwareMap.get(DcMotor.class, "intake");
        barrier = hardwareMap.get(Servo.class, "barrier");
        leftGripper = hardwareMap.get(Servo.class, "leftGripper");
        rightGripper = hardwareMap.get(Servo.class, "rightGripper");

        flywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        flywheel.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, ShooterConstants.flywheelCoeffs);

        flap.setDirection(Servo.Direction.REVERSE);
        flap.setPosition(flapDown);
        barrier.setPosition(barrierDown);
        pivot.setPosition(pivotUp);

        leftGripper.setDirection(Servo.Direction.REVERSE);
        leftGripper.setPosition(leftGripperOpen);
        rightGripper.setPosition(rightGripperOpen);

        this.isAuto = isAuto;
    }

    public void command(Command command) {

        if(!isAuto) {
            this.unexecutedCommand = command;
            return;
        } else
            queue.offer(command);
    }

    public void setupShooter(){
        // IDLE -> human load position
        fsm.onStateEnter(State.IDLE, () -> {
            intakeMotor.setPower(intakeHumanPower);
            targetVelocity = 0;
            pivot.setPosition(pivotIdle);
            rightGripper.setPosition(rightGripperOpen);
            leftGripper.setPosition(leftGripperOpen);
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
            targetVelocity = ShooterConstants.target;
            intakeMotor.setPower(intakeShooterPower);
        });
        fsm.onStateUpdate(State.SHOOTING,  (current, timeSinceTransition) -> {
            if (timeSinceTransition > 50)
                pivot.setPosition(pivotUp);
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
            barrier.setPosition(barrierUp);
        });
        fsm.onStateUpdate(State.BARRIER_RAISE, (current, timeSinceTransition) -> {
            if (timeSinceTransition > 150) {
                return State.FLAP_UP;
            }
            return null;
        });
        fsm.onStateEnter(State.FLAP_UP, () -> {
            flap.setPosition(flapUp);
        });
        fsm.onStateUpdate(State.FLAP_UP, (current, timeSinceTransition) -> {
            if(timeSinceTransition > 300) {
                return State.SHOOTING;
            }
            return null;
        });
        fsm.onStateExit(State.FLAP_UP,  () -> {
            flap.setPosition(flapDown);
            barrier.setPosition(barrierDown);
            rightGripper.setPosition(rightGripperOpen);
            leftGripper.setPosition(leftGripperOpen);
        });

        // INTAKE -> intake mechanism
        fsm.onStateEnter(State.INTAKE, () -> {
            intakeMotor.setPower(intakePower);
            targetVelocity = 0;
            pivot.setPosition(pivotDown);
            rightGripper.setPosition(rightGripperOpen);
            leftGripper.setPosition(leftGripperOpen);

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
        fsm.onStateExit(State.INTAKE, () -> {
            rightGripper.setPosition(rightGripperClosed);
            leftGripper.setPosition(leftGripperClosed);
        });

        // final initialisation
        fsm.init();
    }

    public void updateShooter(){

        try {
            unexecutedCommand = (!queue.isEmpty() && unexecutedCommand == null) ?
                    queue.take() : unexecutedCommand;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        fsm.update();
        flywheel.setVelocity(targetVelocity);

        // this will mostly have to be removed
        // the flywheel has been configured
        if (ShooterConstants.active) {
            PIDFCoefficients coef = flywheel.getPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER);
            boolean changed = false;
            if (coef.f != ShooterConstants.kf) {
                changed = true;
                coef.f = ShooterConstants.kf;
            }
            if (coef.p != ShooterConstants.kp) {
                changed = true;
                coef.p = ShooterConstants.kp;
            }
            if (coef.i != ShooterConstants.ki) {
                changed = true;
                coef.i = ShooterConstants.ki;
            }
            if (coef.d != ShooterConstants.kd) {
                changed = true;
                coef.d = ShooterConstants.kd;
            }
            if (changed) {
                flywheel.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, coef);
            }
        }

    }
}
