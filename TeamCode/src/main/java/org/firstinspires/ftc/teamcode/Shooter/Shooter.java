package org.firstinspires.ftc.teamcode.Shooter;

import android.util.Log;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.util.StateMachine;

public class Shooter {
    private DcMotor flywheel;

    private Servo flap;

    double flapDown = 0.25;
    double flapUp = 0.5;

    private enum State{
        IDLE,
        RUNNING,
        FLAP_UP
    }

    public enum Command{
        TOGGLE_SHOOTER,
        ACTIVATE_FLAP,
    }

    public Shooter(HardwareMap hardwareMap){
        flywheel = hardwareMap.get(DcMotor.class, "Flywheel");
        flap = hardwareMap.get(Servo.class, "Flap");
        flap.setDirection(Servo.Direction.REVERSE);
        flap.setPosition(flapDown);
    }

    private StateMachine<State> fsm = new StateMachine<>(State.IDLE);

    public void command(Command command) {
        this.unexecutedCommand = command;
    }

    private Command unexecutedCommand;

    public void setupShooter(){
        fsm.onStateEnter(State.IDLE,  () -> {
            flywheel.setPower(0);
        });
        fsm.onStateUpdate(State.IDLE,  () -> {
            if(unexecutedCommand == Command.TOGGLE_SHOOTER){
                unexecutedCommand = null;
                return State.RUNNING;
            }
            return null;
        });

        fsm.onStateEnter(State.RUNNING, () -> {
            flywheel.setPower(0.7);
        });
        fsm.onStateUpdate(State.RUNNING, () -> {
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

        fsm.onStateEnter(State.FLAP_UP, () -> {
            flap.setPosition(flapUp);
        });
        fsm.onStateUpdate(    State.FLAP_UP, (current, timeSinceTransition) -> {
            if(timeSinceTransition > 200){
                return State.RUNNING;
            }
            return null;
        });

        fsm.onStateExit(State.FLAP_UP,  () -> {
            flap.setPosition(flapDown);
        });


        fsm.init();

    }

    public void updateShooter(){
        fsm.update();
        Log.d("stateFlywheel", "" + fsm.getCurrentState());
    }




}
