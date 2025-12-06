package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.robot.Shooter;
import org.firstinspires.ftc.teamcode.opmodes.AutoPaths;
import org.firstinspires.ftc.teamcode.opmodes.AutoPoses;
import org.firstinspires.ftc.teamcode.opmodes.TeamColor;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.util.StateMachine;

@Autonomous(name = "Auto BLUE")
public class AutoBlue extends AutoBase {
    private final StateMachine<State> fsm = new StateMachine<>(State.INIT);

    enum State {
        INIT,
        START_POSITION,
        SHOOT_A, // preload
        FIRST_INTAKE, SHOOT_FIRST_INTAKE, SHOOT_B,
        SECOND_INTAKE, SHOOT_SECOND_INTAKE, SHOOT_C,
        THIRD_INTAKE, SHOOT_THIRD_INTAKE, SHOOT_D,
        GO_HOME,
    }

    protected void setColor() {
        color = TeamColor.BLUE;
    }
    protected void setStartingPose() {
        follower.setStartingPose(poses.goalStart);
    }

    protected void setupFSM(){
        // handle preload
        fsm.onStateEnter(State.START_POSITION, () -> {
            follower.followPath(paths.goShootPreload);
            shooter.command(Shooter.Command.TOGGLE_SHOOTING);
        });
        fsm.onStateUpdate(State.START_POSITION, () -> {
            if(!follower.isBusy()) {
                return State.SHOOT_A;
            }
            return null;
        });

        setShoot3Artifacts(State.SHOOT_A, State.FIRST_INTAKE);

        // handle first intake
        fsm.onStateEnter(State.FIRST_INTAKE, () -> {
            follower.followPath(paths.goFirstIntake);
            shooter.command(Shooter.Command.TOGGLE_INTAKE);
        });
        fsm.onStateUpdate(State.FIRST_INTAKE, () -> {
            if (!follower.isBusy())
                return State.SHOOT_FIRST_INTAKE;
            return null;
        });

        fsm.onStateEnter(State.SHOOT_FIRST_INTAKE, () -> {
            follower.followPath(paths.goShootFirstIntake);
            shooter.command(Shooter.Command.TOGGLE_SHOOTING);
        });
        fsm.onStateUpdate(State.SHOOT_FIRST_INTAKE, () -> {
            if (!follower.isBusy())
                return State.SHOOT_B;
            return null;
        });

        setShoot3Artifacts(State.SHOOT_B, State.SECOND_INTAKE);

        // handle second intake
        fsm.onStateEnter(State.SECOND_INTAKE, () -> {
            follower.followPath(paths.goSecondIntake);
            shooter.command(Shooter.Command.TOGGLE_INTAKE);
        });
        fsm.onStateUpdate(State.SECOND_INTAKE, () -> {
            if (!follower.isBusy())
                return State.SHOOT_SECOND_INTAKE;
            return null;
        });

        fsm.onStateEnter(State.SHOOT_SECOND_INTAKE, () -> {
            follower.followPath(paths.goShootSecondIntake);
            shooter.command(Shooter.Command.TOGGLE_SHOOTING);
        });
        fsm.onStateUpdate(State.SHOOT_SECOND_INTAKE, () -> {
            if (!follower.isBusy())
                return State.SHOOT_C;
            return null;
        });

        setShoot3Artifacts(State.SHOOT_C, State.THIRD_INTAKE);

        // handle third intake
        fsm.onStateEnter(State.THIRD_INTAKE, () -> {
            follower.followPath(paths.goThirdIntake);
            shooter.command(Shooter.Command.TOGGLE_INTAKE);
        });
        fsm.onStateUpdate(State.THIRD_INTAKE, () -> {
            if (!follower.isBusy())
                return State.SHOOT_THIRD_INTAKE;
            return null;
        });

        fsm.onStateEnter(State.SHOOT_THIRD_INTAKE, () -> {
            follower.followPath(paths.goShootThirdIntake);
            shooter.command(Shooter.Command.TOGGLE_SHOOTING);
        });
        fsm.onStateUpdate(State.SHOOT_THIRD_INTAKE, () -> {
            if (!follower.isBusy())
                return State.SHOOT_D;
            return null;
        });

        setShoot3Artifacts(State.SHOOT_D, State.GO_HOME);

        fsm.onStateEnter(State.GO_HOME, () -> {
            follower.followPath(paths.goGoalHome);
            shooter.command(Shooter.Command.TOGGLE_IDLE);
        });
        fsm.onStateUpdate(State.GO_HOME, () -> {
            if (!follower.isBusy()) {
                shooter.command(Shooter.Command.TOGGLE_DEAD);
            }
        });

        fsm.init();
    }

    protected void startFSM() {
        fsm.onStateUpdate(State.INIT, () -> State.START_POSITION);
    }
    protected void updateFSM() { fsm.update(); }

    private void setShoot3Artifacts(State startState, State nextState) {
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
