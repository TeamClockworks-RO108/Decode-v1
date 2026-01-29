package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.robot.Shooter;
import org.firstinspires.ftc.teamcode.opmodes.TeamColor;
import org.firstinspires.ftc.teamcode.util.StateMachine;

@Disabled
@Autonomous(name = "Auto BLUE 9")
public class AutoBlue extends AutoBase {
    protected final StateMachine<State> fsm = new StateMachine<>(State.INIT);

    protected enum State {
        INIT,
        START_POSITION,
        SHOOT_A, // preload
        FIRST_INTAKE, GRAB_B, SHOOT_FIRST_INTAKE, SHOOT_B,
        SECOND_INTAKE, GRAB_C, SHOOT_SECOND_INTAKE, SHOOT_C,
        THIRD_INTAKE, GRAB_D, SHOOT_THIRD_INTAKE, SHOOT_D,
        GO_HOME, GO_HOME_FROM_INTAKE, GO_OPEN_GATE, OPEN_GATE,
        Go_TURN_HOME
    }

    protected void setColor() {
        color = TeamColor.BLUE;
    }
    protected void setStartingPose() {
        startingPose = poses.goalStart;
    }

    protected void setupFSM(){
        // handle preload
        fsm.onStateEnter(State.START_POSITION, () -> {
            movement.followPath(paths.goShootPreload);
            shooter.command(Shooter.Command.TOGGLE_SHOOTING);
        });
        fsm.onStateUpdate(State.START_POSITION, (current, timeSinceTransition) -> {
            if(!movement.isBusy() || timeSinceTransition > 600) {
                return State.SHOOT_A;
            }
            return null;
        });

        setShoot3Artifacts(State.SHOOT_A, State.FIRST_INTAKE);

        // handle first intake
        fsm.onStateEnter(State.FIRST_INTAKE, () -> {
            movement.followPath(paths.goFirstIntake);
            shooter.command(Shooter.Command.TOGGLE_INTAKE);
        });
        fsm.onStateUpdate(State.FIRST_INTAKE, () -> {
            if (!movement.isBusy())
                return State.GRAB_B;
            return null;
        });

        fsm.onStateEnter(State.GRAB_B, () -> {
            shooter.command(Shooter.Command.TOGGLE_SHOOTING);
        });
        fsm.onStateUpdate(State.GRAB_B, (current, timeSinceTransition) -> {
            if (timeSinceTransition > grabTime)
                return State.SHOOT_FIRST_INTAKE;
            return null;
        });

        fsm.onStateEnter(State.SHOOT_FIRST_INTAKE, () -> {
            movement.followPath(paths.goShootFirstIntake);
        });
        fsm.onStateUpdate(State.SHOOT_FIRST_INTAKE, () -> {
            if (!movement.isBusy())
                return State.SHOOT_B;
            return null;
        });

        setShoot3Artifacts(State.SHOOT_B, State.SECOND_INTAKE);

        // handle second intake
        fsm.onStateEnter(State.SECOND_INTAKE, () -> {
            movement.followPath(paths.goSecondIntake);
            shooter.command(Shooter.Command.TOGGLE_INTAKE);
        });
        fsm.onStateUpdate(State.SECOND_INTAKE, () -> {
            if (!movement.isBusy())
                return State.GRAB_C;
            return null;
        });

        fsm.onStateEnter(State.GRAB_C, () -> {
            shooter.command(Shooter.Command.TOGGLE_SHOOTING);
        });
        fsm.onStateUpdate(State.GRAB_C, (current, timeSinceTransition) -> {
            if (timeSinceTransition > grabTime)
                return State.SHOOT_SECOND_INTAKE;
            return null;
        });

        fsm.onStateEnter(State.SHOOT_SECOND_INTAKE, () -> {
            movement.followPath(paths.goShootSecondIntake);
        });
        fsm.onStateUpdate(State.SHOOT_SECOND_INTAKE, () -> {
            if (!movement.isBusy())
                return State.SHOOT_C;
            return null;
        });

        setShoot3Artifacts(State.SHOOT_C, State.GO_HOME);

        fsm.onStateEnter(State.GO_HOME, () -> {
            movement.followPath(paths.goGoalHome);
            shooter.command(Shooter.Command.TOGGLE_IDLE);
        });
        fsm.onStateUpdate(State.GO_HOME, () -> {
            if (!movement.isBusy()) {
                shooter.command(Shooter.Command.TOGGLE_DEAD);
            }
        });

        fsm.init();
    }

    protected void startFSM() {
        fsm.onStateUpdate(State.INIT, () -> State.START_POSITION);
    }
    protected void updateFSM() { fsm.update(); }

    protected void setShoot3Artifacts(State startState, State nextState) {
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
