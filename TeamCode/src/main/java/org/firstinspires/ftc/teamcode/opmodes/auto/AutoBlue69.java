package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.opmodes.TeamColor;
import org.firstinspires.ftc.teamcode.robot.Shooter;
import org.firstinspires.ftc.teamcode.util.StateMachine;


@Autonomous(name = "auto BLUE 69999999999")
public class AutoBlue69 extends AutoBase {

    protected final StateMachine<State> fsm = new StateMachine<>(State.INIT);

    private final double timeToCollectFromGate = 200;


    protected enum State {
        INIT,
        START_POSITION,
        SHOOT_A, // preload
        FIRST_INTAKE, GRAB_B, WAIT_AT_GATE, GO_OPEN_GATE, GO_GATE_MIDDLE_GOAL,  SHOOT_B,
        GATE_INTAKE, GATE_COLLECT, SHOOT_GATE_INTAKE, SHOOT_FROM_GATE, WAIT_TO_COLLECT,
        THIRD_INTAKE, GRAB_D, SHOOT_THIRD_INTAKE, SHOOT_D,
        GO_HOME, GO_HOME_FROM_INTAKE,  OPEN_GATE,
        Go_TURN_HOME
    }

    protected void setColor() {
        color = TeamColor.BLUE;
    }
    protected void setStartingPose() {
        startingPose = poses.goalStart;
    }

    protected void setupFSM(){

        fsm.onStateEnter(State.START_POSITION, () -> {
            movement.followPath(paths.goShootMiddlePreload);
            shooter.command(Shooter.Command.TOGGLE_SHOOTING_MIDDLE);
        });

        fsm.onStateUpdate(State.START_POSITION, (current, timeSinceTransition) -> {
            if(!movement.isBusy() || timeSinceTransition > 600) {
                return State.SHOOT_A;
            }
            return null;
        });

        setShoot3Artifacts(State.SHOOT_A, State.FIRST_INTAKE);

        fsm.onStateEnter(State.FIRST_INTAKE, () -> {
            movement.followPath(paths.getGoFirstIntake69);
            shooter.command(Shooter.Command.TOGGLE_INTAKE);
        });
        fsm.onStateUpdate(State.FIRST_INTAKE, () -> {
            if (!movement.isBusy())
                return State.GRAB_B;
            return null;
        });

        fsm.onStateEnter(State.GRAB_B, () -> {
            movement.followPath(paths.goOpenGate69);
        });
        fsm.onStateUpdate(State.GRAB_B,  () -> {
            if (!movement.isBusy()){
                return State.WAIT_AT_GATE;
            }
            return null;
        });

        fsm.onStateUpdate(State.WAIT_AT_GATE,     (current, timeSinceTransition) -> {
            if(timeSinceTransition > 500){
                return State.GO_GATE_MIDDLE_GOAL;
            }
            return null;
        });

        fsm.onStateEnter(State.GO_GATE_MIDDLE_GOAL,    () -> {
            movement.followPath(paths.goGateGoal69);
            shooter.command(Shooter.Command.TOGGLE_SHOOTING_MIDDLE);
        });

        fsm.onStateUpdate(State.GO_GATE_MIDDLE_GOAL, () -> {
            if(!movement.isBusy()){
                return State.SHOOT_B;
            }
            return null;
        });

        setShoot3Artifacts(State.SHOOT_B, State.GATE_INTAKE);

        fsm.onStateEnter(State.GATE_INTAKE, () -> {
            movement.followPath(paths.turnCollectFromGate69);
        });

        fsm.onStateUpdate(State.GATE_INTAKE, () -> {
            if(!movement.isBusy()){
                return State.GATE_COLLECT;
            }
            return null;
        });

        fsm.onStateEnter(State.GATE_COLLECT, () -> {
            movement.followPath(paths.goCollectFromGate69);
        });

        fsm.onStateUpdate(State.GATE_COLLECT,   () -> {
            if(!movement.isBusy()){
                return State.WAIT_TO_COLLECT;
            }
            return null;
        });

        fsm.onStateUpdate(State.WAIT_TO_COLLECT, (current, timeSinceTransition) -> {
            if(timeSinceTransition > timeToCollectFromGate){
                return State.SHOOT_GATE_INTAKE;
            }
            return null;
        });

        fsm.onStateUpdate(State.SHOOT_GATE_INTAKE, () -> {
            movement.followPath(paths.goShootMiddleFromGate69);
            shooter.command(Shooter.Command.TOGGLE_SHOOTING_MIDDLE);
        });

        fsm.onStateUpdate(State.SHOOT_GATE_INTAKE, () -> {
            if(!movement.isBusy()){
                return State.SHOOT_FROM_GATE;
            }
            return null;
        });

        setShoot3Artifacts(State.SHOOT_FROM_GATE, State.THIRD_INTAKE);




        fsm.init();


    }

    protected void startFSM() {
        fsm.onStateUpdate(State.INIT, () -> State.START_POSITION);
    }
    protected void updateFSM() { fsm.update(); }

    protected void setShoot3Artifacts(AutoBlue69.State startState, AutoBlue69.State nextState) {
        fsm.onStateEnter(startState, () -> {
            shooter.command(Shooter.Command.RAPID_FIRE);
        });
        fsm.onStateUpdate(startState, (current, timeSinceTransition) -> {
            if (timeSinceTransition > shooter.getRapidFireTime()) {
                return nextState;
            }
            return null;
        });
    }
}
