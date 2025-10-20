package org.firstinspires.ftc.teamcode.Shooter;

import android.util.Log;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

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

    private final double flyWheelTicks = 3000;
    private final double flapDown = 0.25;
    private final double flapUp = 0.5;

    private final double pivotDown = 0.1;
    private final double pivotIdle = 0.32;
    private final double pivotUp = 0.4;

    private final double barrierOff = 0.2;
    private final double barrierOn = 0.6;

    private enum State{
        INTAKE,
        IDLE,
        SHOOTING,
        BARRIER_RAISE,
        FLAP_UP,
    }

    public enum Command{
        TOGGLE_SHOOTING,
        ACTIVATE_FLAP,
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
        fsm.onStateEnter(State.IDLE, () -> {
            intakeMotor.setPower(intakeHumanPower);
            flywheel.setVelocity(0);
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

        // when
        fsm.onStateEnter(State.SHOOTING,  () -> {
            flywheel.setVelocity(flyWheelTicks);
            intakeMotor.setPower(intakeIdlePower);
            pivot.setPosition(pivotUp);
        });
        fsm.onStateUpdate(State.SHOOTING,  () -> {
            // from SHOOTING, you can go to INTAKE, IDLE, and ACTIVATE_FLAP
            if(unexecutedCommand == Command.TOGGLE_INTAKE) {
                unexecutedCommand = null;
                return State.INTAKE;
            }
            if (unexecutedCommand == Command.TOGGLE_IDLE) {
                unexecutedCommand = null;
                return State.IDLE;
            }
            if(unexecutedCommand == Command.ACTIVATE_FLAP){
                unexecutedCommand = null;
                return State.BARRIER_RAISE;
            }
            return null;
        });

        fsm.onStateEnter(State.BARRIER_RAISE, () -> {
            barrier.setPosition(barrierOff);
        });
        fsm.onStateUpdate(State.BARRIER_RAISE, (current, timeSinceTransition) -> {
            if (timeSinceTransition > 200) {
                return State.FLAP_UP;
            }
            return null;
        });

        // when FLAP_UP, move the servo to flapUp
        fsm.onStateEnter(State.FLAP_UP, () -> {
            flap.setPosition(flapUp);
        });
        fsm.onStateUpdate(State.FLAP_UP, (current, timeSinceTransition) -> {
            // wait for 200 milliseconds, return to RUNNING
            if(timeSinceTransition > 200){
                return State.SHOOTING;
            }
            return null;
        });
        // when leaving FLAP_UP, move the servo back
        fsm.onStateExit(State.FLAP_UP,  () -> {
            flap.setPosition(flapDown);
            barrier.setPosition(barrierOn);
        });

        // when INTAKE, start intakeMotor and set pivot down
        fsm.onStateEnter(State.INTAKE, () -> {
            intakeMotor.setPower(intakePower);
            flywheel.setVelocity(0);
            pivot.setPosition(pivotDown);
        });
        fsm.onStateUpdate(State.INTAKE, () -> {
            // from INTAKE, you go to SHOOTING and IDLE
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
        // when leaving INTAKE reset the the pivot servo
        fsm.onStateExit(State.INTAKE, () -> {
            pivot.setPosition(pivotUp);
        });

        // final initialisation
        fsm.init();
    }

    public void updateShooter(){
        fsm.update();
        String str = "" + fsm.getCurrentState();
        Log.d("stateFlywheel", str);
    }
}
