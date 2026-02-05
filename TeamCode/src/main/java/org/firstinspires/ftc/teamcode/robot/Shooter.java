package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.util.StateMachine;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Shooter {
    private enum State {
        DEAD,
        INTAKE,
        INTAKE_REJECT,
        IDLE,
        SHOOTING,
        RAISE_FIRE,       // single launch
        LAUNCHING,
        RAISE_RAPID_FIRE, // triple launch
        LAUNCH1, RELOAD1,
        LAUNCH2, RELOAD2,
        LAUNCH3,
    }
    public enum Command {
        TOGGLE_SHOOTING,
        FIRE, RAPID_FIRE,
        TOGGLE_INTAKE,
        TOGGLE_IDLE,
        TOGGLE_DEAD,
        TOGGLE_INTAKE_REJECT
    }

    private final StateMachine<State> fsm = new StateMachine<>(State.DEAD);;
    private Command unexecutedCommand;

    private final Pivot pivot;
    private final Intake intake;
    private final Outtake outtake;
    private final int raiseTime = 150, launchingTime = 175, reloadTime = 300; //200, 400

    private boolean isAuto = false;

    private final BlockingQueue<Command> queue = new ArrayBlockingQueue<>(16);

    public Shooter(HardwareMap hardwareMap, Telemetry telemetry, boolean isAuto) {
        pivot = new Pivot(hardwareMap);
        intake = new Intake(hardwareMap);
        outtake = new Outtake(hardwareMap, telemetry);

        this.isAuto = isAuto;
    }

    public void command(Command command) {
        if(!isAuto)
            this.unexecutedCommand = command;
        else
            queue.offer(command);
    }

    public long getRapidFireTime() {
        return (long)(raiseTime + 3 * launchingTime + 2.2 * reloadTime);
    }

    public void setupShooter(){
        // DEAD -> do nothing, wait for start
        fsm.onStateUpdate(State.DEAD, () -> {
            if (unexecutedCommand == Command.TOGGLE_IDLE) {
                unexecutedCommand = null;
                return State.IDLE;
            }
            else if (unexecutedCommand == Command.TOGGLE_SHOOTING) {
                unexecutedCommand = null;
                return State.SHOOTING;
            }
            return null;
        });
        fsm.onStateExit(State.DEAD, () -> {
            outtake.close();
        });

        // IDLE -> human load position
        fsm.onStateEnter(State.IDLE, () -> {
            pivot.idle();
            intake.idle();
            outtake.stopFlywheel();
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
            if (timeSinceTransition > 200) {
                pivot.shoot();
            }
            if(unexecutedCommand == Command.TOGGLE_INTAKE) {
                unexecutedCommand = null;
                return State.INTAKE;
            }
            if (unexecutedCommand == Command.TOGGLE_IDLE) {
                unexecutedCommand = null;
                return State.IDLE;
            }
            if (unexecutedCommand == Command.FIRE) {
                unexecutedCommand = null;
                return State.RAISE_FIRE;
            }
            if (unexecutedCommand == Command.RAPID_FIRE) {
                unexecutedCommand = null;
                return State.RAISE_RAPID_FIRE;
            }
            return null;
        });

        setupFire();

        setupRapidFire();

        // INTAKE -> intake mechanism
        fsm.onStateEnter(State.INTAKE, () -> {
            pivot.intake();
            intake.intake();
            outtake.stopFlywheel();
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

    private void setupFire() {
        // BARRIER_RAISE and FLAP_UP -> launch chain
        fsm.onStateEnter(State.RAISE_FIRE, () -> {
            outtake.raise();
        });
        fsm.onStateUpdate(State.RAISE_FIRE, (current, timeSinceTransition) -> {
            if (timeSinceTransition > raiseTime) {
                return State.LAUNCHING;
            }
            return null;
        });
        fsm.onStateEnter(State.LAUNCHING, () -> {
            outtake.launch();
            intake.push();
        });
        fsm.onStateUpdate(State.LAUNCHING, (current, timeSinceTransition) -> {
            if(timeSinceTransition > launchingTime + 150) {
                return State.SHOOTING;
            }
            return null;
        });
        fsm.onStateExit(State.LAUNCHING, () -> {
            outtake.close();
            intake.shoot();
        });
    }

    private void setupRapidFire() {
        fsm.onStateEnter(State.RAISE_RAPID_FIRE, () -> {
            outtake.raise();
        });
        fsm.onStateUpdate(State.RAISE_RAPID_FIRE, (current, timeSinceTransition) -> {
            if (timeSinceTransition > raiseTime)
                return State.LAUNCH1;
            return null;
        });

        fsm.onStateEnter(State.LAUNCH1, () -> {
            outtake.launch();
        });
        fsm.onStateUpdate(State.LAUNCH1, (current, timeSinceTransition) -> {
            if (timeSinceTransition > launchingTime)
                return State.RELOAD1;
            return null;
        });
        fsm.onStateEnter(State.RELOAD1, () -> {
            outtake.reload();
            intake.push();
        });
        fsm.onStateUpdate(State.RELOAD1, (current, timeSinceTransition) -> {
            if (timeSinceTransition > reloadTime)
                return State.LAUNCH2;
            return null;
        });

        fsm.onStateEnter(State.LAUNCH2, () -> outtake.launch());
        fsm.onStateUpdate(State.LAUNCH2, (current, timeSinceTransition) -> {
            if (timeSinceTransition > launchingTime)
                return State.RELOAD2;
            return null;
        });
        fsm.onStateEnter(State.RELOAD2, () -> outtake.reload());
        fsm.onStateUpdate(State.RELOAD2, (current, timeSinceTransition) -> {
            if (timeSinceTransition > reloadTime + 50)
                return State.LAUNCH3;
            return null;
        });

        fsm.onStateEnter(State.LAUNCH3, () -> {
            outtake.launch();
            intake.shoot();
        });
        fsm.onStateUpdate(State.LAUNCH3, (current, timeSinceTransition) -> {
            if (timeSinceTransition > launchingTime + 260)
                return State.SHOOTING;
            return null;
        });
        fsm.onStateExit(State.LAUNCH3, () -> outtake.close());
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
        intake.update();
    }
}
