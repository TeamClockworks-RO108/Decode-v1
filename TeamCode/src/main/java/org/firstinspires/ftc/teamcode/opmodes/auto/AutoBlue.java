package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Robot.Shooter;
import org.firstinspires.ftc.teamcode.opmodes.AutoPaths;
import org.firstinspires.ftc.teamcode.opmodes.AutoPoses;
import org.firstinspires.ftc.teamcode.opmodes.TeamColor;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.util.StateMachine;

@Autonomous(name = "Auto BLUE")
public class AutoBlue extends OpMode {
    protected TeamColor color = TeamColor.BLUE;
    private Shooter shooter;
    private Follower follower;

    private AutoPoses poses;
    private AutoPaths paths;

    private StateMachine<State> fsm = new StateMachine<>(State.INIT);

    enum State {
        INIT,
        START_POSITION,
        SHOOT_POSITION,
        SHOOT_A1, SHOOT_A2, SHOOT_A3, // preload
        FIRST_INTAKE,
        SHOOT_FIRST_INTAKE,
        SHOOT_B1, SHOOT_B2, SHOOT_B3,
        SECOND_INTAKE,
        SHOOT_SECOND_INTAKE,
        SHOOT_C1, SHOOT_C2, SHOOT_C3,
        GO_HOME
    }

    @Override
    public void init() {
        shooter = new Shooter(hardwareMap, true);
        shooter.setupShooter();

        follower = Constants.createFollower(hardwareMap);

        poses = new AutoPoses(color);
        paths = new AutoPaths(follower, poses);

        follower.setStartingPose(poses.goalStart);
        follower.update();

        setupFSM();
    }

    @Override
    public void start() {
        shooter.command(Shooter.Command.TOGGLE_IDLE);
        fsm.onStateUpdate(State.INIT, () -> State.START_POSITION);
    }

    @Override
    public void loop() {
        updateFSM();
        follower.update();
        shooter.updateShooter();
    }

    public void setupFSM(){
        // handle preload
        fsm.onStateEnter(State.START_POSITION, () -> {
            follower.followPath(paths.goShootPreload);
        });
        fsm.onStateUpdate(State.START_POSITION, () -> {
            if(!follower.isBusy()) {
                return State.SHOOT_POSITION;
            }
            return null;
        });

        fsm.onStateEnter(State.SHOOT_POSITION, () -> {
            shooter.command(Shooter.Command.TOGGLE_SHOOTING);
        });
        fsm.onStateUpdate(State.SHOOT_POSITION, (current, timeSinceTransition) -> {
            if(timeSinceTransition > 1000){
                return State.SHOOT_A1;
            }
            return null;
        });

        setShootArtifact(State.SHOOT_A1, State.SHOOT_A2);
        setShootArtifact(State.SHOOT_A2, State.SHOOT_A3);
        setShootArtifact(State.SHOOT_A3, State.FIRST_INTAKE);

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
                return State.SHOOT_B1;
            return null;
        });

        setShootArtifact(State.SHOOT_B1, State.SHOOT_B2);
        setShootArtifact(State.SHOOT_B2, State.SHOOT_B3);
        setShootArtifact(State.SHOOT_B3, State.SECOND_INTAKE);

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
                return State.SHOOT_C1;
            return null;
        });

        setShootArtifact(State.SHOOT_C1, State.SHOOT_C2);
        setShootArtifact(State.SHOOT_C2, State.SHOOT_C3);
        setShootArtifact(State.SHOOT_C3, State.GO_HOME);

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

    private void setShootArtifact(State startState, State nextState) {
        fsm.onStateEnter(startState, () -> {
            shooter.command(Shooter.Command.FIRE);
        });
        fsm.onStateUpdate(startState, (current, timeSinceTransition) -> {
            if (timeSinceTransition > 1200) {
                return nextState;
            }
            return null;
        });
    }

    public void updateFSM() {
        fsm.update();
    }
}
