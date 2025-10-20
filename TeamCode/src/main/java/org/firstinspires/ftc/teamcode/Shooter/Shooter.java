package org.firstinspires.ftc.teamcode.Shooter;

import android.util.Log;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.util.StateMachine;

public class Shooter {
    private DcMotor flywheel;
    private DcMotor intakeMotor;
    private Servo flap;
    private Servo pivot;

    private final double intakePower = 0.7;
    private final double flywheelPower = 0.7;
    private final double flapDown = 0.25;
    private final double flapUp = 0.5;
    private final double pivotDown = 0.1;
    private final double pivotIdle = 0.32;
    private final double pivotUp = 0.4;

    private enum State{
        IDLE,
        SHOOTING,
        RUNNING,
        FLAP_UP,
        INTAKE
    }

    public enum Command{
        TOGGLE_SHOOTER,
        ACTIVATE_FLAP,
        TOGGLE_INTAKE,
        TOGGLE_IDLE
    }

    private StateMachine<State> fsm = new StateMachine<>(State.IDLE);
    private Command unexecutedCommand;

    public Shooter(HardwareMap hardwareMap){
        flywheel = hardwareMap.get(DcMotor.class, "flywheel");
        flap = hardwareMap.get(Servo.class, "flap");
        pivot = hardwareMap.get(Servo.class, "pivot");
        intakeMotor = hardwareMap.get(DcMotor.class, "intake");

        flap.setDirection(Servo.Direction.REVERSE);
        flap.setPosition(flapDown);
        pivot.setPosition(pivotUp);
    }

    public void command(Command command) {
        this.unexecutedCommand = command;
    }

    public void setupShooter(){

        fsm.onStateEnter(State.IDLE, () -> {
            intakeMotor.setPower(0);
            pivot.setPosition(pivotIdle);
        });
        fsm.onStateUpdate(State.IDLE, () -> {
            if (unexecutedCommand == Command.TOGGLE_INTAKE) {
                unexecutedCommand = null;
                return State.INTAKE;
            }
            if (unexecutedCommand == Command.TOGGLE_IDLE) {
                unexecutedCommand = null;
                return State.SHOOTING;
            }
            return null;
        });

        fsm.onStateEnter(State.SHOOTING,  () -> {
            flywheel.setPower(0);
            pivot.setPosition(pivotUp);
        });
        fsm.onStateUpdate(State.SHOOTING,  () -> {
            // from IDLE, listen for TOGGLE_SHOOTER, TOGGLE_INTAKE
            if(unexecutedCommand == Command.TOGGLE_SHOOTER) {
                unexecutedCommand = null;
                return State.RUNNING;
            }
            if (unexecutedCommand == Command.TOGGLE_IDLE) {
                unexecutedCommand = null;
                return State.IDLE;
            }
            return null;
        });

        // when RUNNING, power the flywheel
        fsm.onStateEnter(State.RUNNING, () -> {
            flywheel.setPower(flywheelPower);
        });
        fsm.onStateUpdate(State.RUNNING, () -> {
            // from RUNNING, listen for TOGGLE_SHOOTER and ACTIVATE_FLAP
            if(unexecutedCommand == Command.TOGGLE_SHOOTER){
                unexecutedCommand = null;
                return State.SHOOTING;
            }
            if(unexecutedCommand == Command.ACTIVATE_FLAP){
                unexecutedCommand = null;
                return State.FLAP_UP;
            }
            return null;
        });

        // when FLAP_UP, move the servo to flapUp
        fsm.onStateEnter(State.FLAP_UP, () -> {
            flap.setPosition(flapUp);
        });
        fsm.onStateUpdate(State.FLAP_UP, (current, timeSinceTransition) -> {
            // from FLAP_UP, wait for 200 milliseconds, return to RUNNING
            if(timeSinceTransition > 200){
                return State.RUNNING;
            }
            return null;
        });
        // when leaving FLAP_UP, move the servo back
        fsm.onStateExit(State.FLAP_UP,  () -> {
            flap.setPosition(flapDown);
        });

        // when INTAKE, start intakeMotor and set pivot down
        fsm.onStateEnter(State.INTAKE, () -> {
            intakeMotor.setPower(intakePower);
            pivot.setPosition(pivotDown);
        });
        fsm.onStateUpdate(State.INTAKE, () -> {
            // from INTAKE, wait for the TOGGLE_INTAKE
            if (unexecutedCommand == Command.TOGGLE_INTAKE) {
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
