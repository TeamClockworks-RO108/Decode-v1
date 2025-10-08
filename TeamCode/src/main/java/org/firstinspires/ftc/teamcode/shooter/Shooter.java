package org.firstinspires.ftc.teamcode.shooter;

import android.util.Log;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.util.StateMachine;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Shooter {
    private DcMotorEx flywheel;
    private Servo servo;

    private boolean isAuto;
    private StateMachine<States> fsm = new StateMachine<>(States.IDLE);

    private BlockingQueue<Command> commandQueue = new LinkedBlockingQueue<>(5);
    private double RAMP_UP = 0.25;

    public double launchingSpeed() {
        return launchingSpeed;
    }

    public void launchingSpeed(double launchingSpeed) {
        this.launchingSpeed = launchingSpeed;
    }

    private double launchingSpeed = 1300;

    public double RAMP_DOWN() {
        return RAMP_DOWN;
    }

    public void RAMP_DOWN(double value){
        RAMP_DOWN = value;
    }

    private double RAMP_DOWN = 0.096;

    public double RAMP_UP() {
        return RAMP_UP;
    }

    public void RAMP_UP(double value){
        RAMP_UP = value;
    }
    private Command unexecutedCommand;

    private enum States {
        IDLE,
        ROTATING,
        LAUNCHING,
        RETURN
    }

    public enum Command {
        START_ROTATION,
        LAUNCH
    }

    public Shooter(HardwareMap hardwareMap, boolean isAuto) {
        flywheel = hardwareMap.get(DcMotorEx.class, "flywheel");
        servo = hardwareMap.get(Servo.class, "servo");
        this.isAuto = isAuto;
    }

    public void setupStateMachine() {
        fsm.init();
        fsm.onStateEnter(States.IDLE,  () -> {
            flywheel.setVelocity(0);
            servo.setPosition(RAMP_DOWN);
        });

        fsm.onStateUpdate(States.IDLE, () -> {
            if(unexecutedCommand == Command.START_ROTATION){
                unexecutedCommand = null;
                return States.ROTATING;
            }
            return null;
        });

        fsm.onStateEnter(States.ROTATING,() -> {
            flywheel.setVelocity(launchingSpeed);
        });

        fsm.onStateUpdate(States.ROTATING, (current, timeSinceTransition) -> {
            if (timeSinceTransition > 1000 && unexecutedCommand == Command.LAUNCH){
                unexecutedCommand = null;
                return States.LAUNCHING;
            }

            if(unexecutedCommand == Command.START_ROTATION){
                unexecutedCommand = null;
                return States.IDLE;
            }
            return null;
        });

        fsm.onStateEnter(States.LAUNCHING, () -> {
            servo.setPosition(RAMP_UP);
        });

        fsm.onStateUpdate(States.LAUNCHING, (current, timeSinceTransition) -> {
            if (timeSinceTransition > 200) {
                return States.RETURN;
            }
            return null;
        });

        fsm.onStateEnter(States.RETURN, () -> {
            servo.setPosition(RAMP_DOWN);
        });

        fsm.onStateUpdate(States.RETURN, () -> {
            if (unexecutedCommand == Command.LAUNCH){
                unexecutedCommand = null;
                return States.LAUNCHING;
            }

            if(unexecutedCommand == Command.START_ROTATION){
                unexecutedCommand = null;
                return States.IDLE;
            }
            return null;
        });

    }

    public void updateStateMachine() {

        if (unexecutedCommand == null && !commandQueue.isEmpty()) {
            try {
                unexecutedCommand = commandQueue.take();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        fsm.update();
        Log.d("rotation: ", ""+ flywheel.getVelocity());
    }

    public void command(Command command){
        if(isAuto) {
            commandQueue.offer(command);
        }else{
            unexecutedCommand = command;
        }
    }


}
