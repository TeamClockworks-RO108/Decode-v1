package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.opmodes.TeamColor;
import org.firstinspires.ftc.teamcode.robot.Shooter;
import org.firstinspires.ftc.teamcode.util.StateMachine;


@Autonomous(name = "auto BLUE 69999999999")
public class AutoBlue69 extends AutoBase {

    protected final StateMachine<State> fsm = new StateMachine<>(State.INIT);

    private final double timeToCollectFromGate = 2600
            ;

    private final double grabTime = 25;

    protected enum State{
        INIT,
        START_POSITION,
        SHOOT_A, // preload
        FIRST_INTAKE, WAIT_FOR_INTAKE_B, GRAB_B, WAIT_AT_GATE, GO_OPEN_GATE, GO_GATE_MIDDLE_GOAL,  SHOOT_B,
        GATE_INTAKE, GATE_COLLECT, SHOOT_GATE_INTAKE, SHOOT_FROM_GATE, WAIT_TO_COLLECT,
        THIRD_INTAKE,  WAIT_FOR_INTAKE_C, GRAB_C, SHOOT_C,
        FOURTH_INTAKE,  WAIT_FOR_INTAKE_D, GRAB_D, SHOOT_D,
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
            movement.followPath(paths.goShootPreload);
            shooter.command(Shooter.Command.TOGGLE_SHOOTING);
        });

        fsm.onStateUpdate(State.START_POSITION, (current, timeSinceTransition) -> {
            if(!movement.isBusy() || timeSinceTransition > 2375 ) {
                return State.SHOOT_A;
            }
            return null;
        });

        setShoot3Artifacts(State.SHOOT_A, State.FIRST_INTAKE);

        fsm.onStateEnter(State.FIRST_INTAKE, () -> {
            movement.followPath(paths.goFirstIntake69);
            shooter.command(Shooter.Command.TOGGLE_INTAKE);
        });
        fsm.onStateUpdate(State.FIRST_INTAKE, () -> {
            if (!movement.isBusy())
                return State.WAIT_FOR_INTAKE_B;
            return null;
        });

        fsm.onStateUpdate(State.WAIT_FOR_INTAKE_B,   (current, timeSinceTransition) -> {
            if(timeSinceTransition > grabTime){
                return State.GO_GATE_MIDDLE_GOAL;
            }
            return null;
        } );

        fsm.onStateEnter(State.GRAB_B, () -> {
            movement.followPath(paths.goOpenGate69);
        });
        fsm.onStateUpdate(State.GRAB_B,  (current, timeSinceTransition) -> {
            if (!movement.isBusy() || timeSinceTransition > 750 ){
                return State.WAIT_AT_GATE;
            }
            return null;
        });

        fsm.onStateUpdate(State.WAIT_AT_GATE,  (current, timeSinceTransition) -> {
            if(timeSinceTransition > 500){
                return State.GO_GATE_MIDDLE_GOAL;
            }
            return null;
        });

        fsm.onStateEnter(State.GO_GATE_MIDDLE_GOAL,    () -> {
           // movement.followPath(paths.goGateGoal69);
            movement.followPath(paths.goShootFirstIntake69);
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
            shooter.command(Shooter.Command.TOGGLE_INTAKE);
            return State.WAIT_TO_COLLECT;
        });

        fsm.onStateUpdate(State.WAIT_TO_COLLECT, (current, timeSinceTransition) -> {
            if(timeSinceTransition > timeToCollectFromGate){
                return State.SHOOT_GATE_INTAKE;
            }
            return null;
        });

        fsm.onStateEnter(State.SHOOT_GATE_INTAKE, () -> {
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

        fsm.onStateEnter(State.THIRD_INTAKE, () -> {
            movement.followPath(paths.goSecondIntake69);
            shooter.command(Shooter.Command.TOGGLE_INTAKE);
        });

        fsm.onStateUpdate(State.THIRD_INTAKE, () -> {
            if(!movement.isBusy()){
                return State.WAIT_FOR_INTAKE_C;
            }
            return null;
        });

        fsm.onStateUpdate(State.WAIT_FOR_INTAKE_C,  (current, timeSinceTransition) -> {
            if(timeSinceTransition > grabTime){
                return State.GRAB_C;
            }
            return null;
        });

        fsm.onStateEnter(State.GRAB_C, () -> {
            movement.followPath(paths.goShootSecondIntake69);
            shooter.command(Shooter.Command.TOGGLE_SHOOTING_MIDDLE);
        });

        fsm.onStateUpdate(State.GRAB_C, () -> {
            if(!movement.isBusy()){
                return State.SHOOT_C;
            }
            return null;
        });

        setShoot3Artifacts(State.SHOOT_C, State.FOURTH_INTAKE );

        fsm.onStateEnter(State.FOURTH_INTAKE, () -> {
            movement.followPath(paths.goThirdIntake69);
            shooter.command(Shooter.Command.TOGGLE_INTAKE);
        });

        fsm.onStateUpdate(State.FOURTH_INTAKE,   () -> {
            if(!movement.isBusy()){
                return State.WAIT_FOR_INTAKE_D;
            }
            return null;
        });

        fsm.onStateUpdate(State.WAIT_FOR_INTAKE_D, (current, timeSinceTransition) -> {
            if(timeSinceTransition > grabTime){
                return State.GRAB_D;
            }
            return null;
        });

        fsm.onStateEnter(State.GRAB_D,   () -> {
            movement.followPath(paths.goShootThirdIntake69);
            shooter.command(Shooter.Command.TOGGLE_SHOOTING);
        });

        fsm.onStateUpdate(State.GRAB_D, () -> {
            if(!movement.isBusy()){
                return State.SHOOT_D;
            }
            return null;
        });

        setShoot3Artifacts(State.SHOOT_D, State.GO_HOME);

        fsm.onStateEnter(State.GO_HOME,  () -> {
            movement.followPath(paths.goGoalHome);
        });

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
