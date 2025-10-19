package org.firstinspires.ftc.teamcode.Shooter;

import android.util.Log;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.util.StateMachine;

public class Shooter {
    private DcMotor flywheel;
    private Servo flap;
    private Servo pivot;

    private final double flapDown = 0.25;
    private final double flapUp = 0.5;
    private final double pivotDown = 0;
    private final double pivotUp = 1;

    private enum State{
        IDLE,
        RUNNING,
        FLAP_UP,
        INTAKE
    }

    public enum Command{
        TOGGLE_SHOOTER,
        ACTIVATE_FLAP,
        TOGGLE_INTAKE
    }

    private StateMachine<State> fsm = new StateMachine<>(State.IDLE);
    private Command unexecutedCommand;

    public Shooter(HardwareMap hardwareMap){
        flywheel = hardwareMap.get(DcMotor.class, "Flywheel");
        flap = hardwareMap.get(Servo.class, "Flap");
        pivot = hardwareMap.get(Servo.class, "Pivot");

        flap.setDirection(Servo.Direction.REVERSE);
        flap.setPosition(flapDown);
        pivot.setPosition(pivotUp);
    }

    public void command(Command command) {
        this.unexecutedCommand = command;
    }

    public void setupShooter(){
        // when IDLE, stop the flywheel
        fsm.onStateEnter(State.IDLE,  () -> {
            flywheel.setPower(0);
        });
        fsm.onStateUpdate(State.IDLE,  () -> {
            // from IDLE, listen for TOGGLE_SHOOTER
            if(unexecutedCommand == Command.TOGGLE_SHOOTER){
                unexecutedCommand = null;
                return State.RUNNING;
            }
            return null;
        });

        // when RUNNING, power the flywheel
        fsm.onStateEnter(State.RUNNING, () -> {
            flywheel.setPower(0.7);
        });
        fsm.onStateUpdate(State.RUNNING, () -> {
            // from RUNNING, listen for TOGGLE_SHOOTER and ACTIVATE_FLAP
            if(unexecutedCommand == Command.TOGGLE_SHOOTER){
                unexecutedCommand = null;
                return State.IDLE;
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

        // final initialisation
        fsm.init();
    }

    public void updateShooter(){
        fsm.update();
        Log.d("stateFlywheel", "" + fsm.getCurrentState());
    }
}
