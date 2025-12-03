package org.firstinspires.ftc.teamcode.Robot;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.util.StateMachine;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Shooter {
    private final Intake intake;
    private final Outtake outtake;
    private final Servo pivot;

    private final double pivotIntake = 0.68;
    private final double pivotIdle = 0.88;
    private final double pivotShoot = 0.96;
    
    private boolean isAuto = false;

    private final BlockingQueue<Command> queue = new ArrayBlockingQueue<>(16);

    private enum State {
        DEAD,
        INTAKE,
        INTAKE_REJECT,
        IDLE,
        SHOOTING,
        BARRIER_RAISE, // launch chain
        FLAP_UP,
    }

    public enum Command {
        TOGGLE_SHOOTING,
        LAUNCH,
        TOGGLE_INTAKE,
        TOGGLE_IDLE,
        TOGGLE_DEAD,
        TOGGLE_INTAKE_REJECT
    }

    private final StateMachine<State> fsm = new StateMachine<>(State.DEAD);;
    private Command unexecutedCommand;

    public Shooter(HardwareMap hardwareMap, boolean isAuto){
        intake = new Intake(hardwareMap);
        outtake = new Outtake(hardwareMap);

        pivot = hardwareMap.get(Servo.class, "pivot");

        this.isAuto = isAuto;
    }

    public void command(Command command) {
        if(!isAuto)
            this.unexecutedCommand = command;
        else
            queue.offer(command);
    }

    public void setupShooter(){
        // DEAD -> do nothing, wait for start
        fsm.onStateEnter(State.DEAD, () -> {
            intake.stop();
        });
        fsm.onStateUpdate(State.DEAD, () -> {
            if (unexecutedCommand == Command.TOGGLE_IDLE) {
                unexecutedCommand = null;
                return State.IDLE;
            }
            return null;
        });
        fsm.onStateExit(State.DEAD, () -> {
            outtake.close();
        });

        // IDLE -> human load position
        fsm.onStateEnter(State.IDLE, () -> {
            intake.idle();
            outtake.stopFlywheel();
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
            if (unexecutedCommand == Command.TOGGLE_DEAD) {
                unexecutedCommand = null;
                return State.DEAD;
            }
            return null;
        });

        // SHOOTING -> launch position
        fsm.onStateEnter(State.SHOOTING,  () -> {
            outtake.startFlywheel();
            intake.shoot();
        });
        fsm.onStateUpdate(State.SHOOTING,  (current, timeSinceTransition) -> {
            if (timeSinceTransition > 200)
                pivot.setPosition(pivotShoot);
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

        // BARRIER_RAISE and FLAP_UP -> launch chain
        fsm.onStateEnter(State.BARRIER_RAISE, () -> {
            outtake.raise();
        });
        fsm.onStateUpdate(State.BARRIER_RAISE, (current, timeSinceTransition) -> {
            if (timeSinceTransition > 150) {
                return State.FLAP_UP;
            }
            return null;
        });
        fsm.onStateEnter(State.FLAP_UP, () -> {
            outtake.launch();
        });
        fsm.onStateUpdate(State.FLAP_UP, (current, timeSinceTransition) -> {
            if(timeSinceTransition > 280) {
                return State.SHOOTING;
            }
            return null;
        });
        fsm.onStateExit(State.FLAP_UP,  () -> {
            outtake.close();
            intake.openGripper();
        });

        // INTAKE -> intake mechanism
        fsm.onStateEnter(State.INTAKE, () -> {
            intake.start();
            outtake.stopFlywheel();
            pivot.setPosition(pivotIntake);
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
            if (unexecutedCommand == Command.TOGGLE_INTAKE_REJECT) {
                unexecutedCommand = null;
                return State.INTAKE_REJECT;
            }
            return null;
        });
        fsm.onStateExit(State.INTAKE, () -> {
            intake.closeGripper();
        });

        // REJECT_INTAKE
        fsm.onStateEnter(State.INTAKE_REJECT, () -> {
            intake.reject();
        });
        fsm.onStateUpdate(State.INTAKE_REJECT, () -> {
            if (unexecutedCommand == Command.TOGGLE_INTAKE_REJECT) {
                unexecutedCommand = null;
                return State.INTAKE;
            }
            return null;
        });

        // final initialisation
        fsm.init();
    }

    public void updateShooter() {
        try {
            unexecutedCommand = (!queue.isEmpty() && unexecutedCommand == null) ?
                    queue.take() : unexecutedCommand;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        fsm.update();
        outtake.update();
    }
}
