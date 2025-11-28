package org.firstinspires.ftc.teamcode.opmodes;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Shooter.Shooter;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.util.StateMachine;

@Autonomous(name = "Auto BLUE")
public class Auto extends OpMode {
    private Shooter shooter;
    private Follower follower;

    private PathChain goShoot, goFirstIntake, goShootFirstIntake, goHome;

    public Pose startPositon = new Pose(10,10, Math.toRadians(45)),
                shootPosition= new Pose( 41, 41, Math.toRadians(40)),
                firstIntakePosition = new Pose(51, 42, Math.toRadians(-90)),
                firstIntakeTake = new Pose(51, -3, Math.toRadians(-90)),
                homePosition = new Pose(45, 15, Math.toRadians(0));

    private StateMachine<State> fsm = new StateMachine<>(State.INIT);

    enum State {
        INIT,
        START_POSITION,
        SHOOT_POSITION,
        SHOOT_A1, SHOOT_A2, SHOOT_A3, // preload
        FIRST_INTAKE,
        SHOOT_FIRST_INTAKE,
        SHOOT_B1, SHOOT_B2, SHOOT_B3,
        GO_HOME
    }

    @Override
    public void init() {
        shooter = new Shooter(hardwareMap, true);
        shooter.setupShooter();

        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(startPositon);
        follower.update();

        setupPaths();
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
        fsm.onStateEnter(State.START_POSITION, () -> {
            follower.followPath(goShoot);
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

        fsm.onStateEnter(State.FIRST_INTAKE, () -> {
            follower.followPath(goFirstIntake);
            shooter.command(Shooter.Command.TOGGLE_INTAKE);
        });
        fsm.onStateUpdate(State.FIRST_INTAKE, () -> {
            if (!follower.isBusy())
                return State.SHOOT_FIRST_INTAKE;
            return null;
        });

        fsm.onStateEnter(State.SHOOT_FIRST_INTAKE, () -> {
            follower.followPath(goShootFirstIntake);
            shooter.command(Shooter.Command.TOGGLE_SHOOTING);
        });
        fsm.onStateUpdate(State.SHOOT_FIRST_INTAKE, () -> {
            if (!follower.isBusy())
                return State.SHOOT_B1;
            return null;
        });

        setShootArtifact(State.SHOOT_B1, State.SHOOT_B2);
        setShootArtifact(State.SHOOT_B2, State.SHOOT_B3);
        setShootArtifact(State.SHOOT_B3, State.GO_HOME);

        fsm.onStateEnter(State.GO_HOME, () -> {
            follower.followPath(goHome);
            shooter.command(Shooter.Command.TOGGLE_IDLE);
        });

        fsm.init();
    }

    private void setShootArtifact(State startState, State nextState) {
        fsm.onStateEnter(startState, () -> {
            shooter.command(Shooter.Command.LAUNCH);
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

    public void setupPaths(){
         goShoot = follower.pathBuilder()
                .addPath(new BezierCurve(startPositon, shootPosition))
                .setLinearHeadingInterpolation(startPositon.getHeading(), shootPosition.getHeading())
                .build();

         goFirstIntake = follower.pathBuilder()
                 .addPath(new BezierCurve(shootPosition, firstIntakePosition))
                 .setLinearHeadingInterpolation(shootPosition.getHeading(), firstIntakeTake.getHeading())
                 .addPath(new BezierLine(firstIntakePosition, firstIntakeTake))
                 .build();

        goShootFirstIntake = follower.pathBuilder()
                .addPath(new BezierCurve(firstIntakeTake, shootPosition))
                .setLinearHeadingInterpolation(firstIntakePosition.getHeading(), shootPosition.getHeading())
                .build();

        goHome = follower.pathBuilder()
                .addPath(new BezierCurve(shootPosition, homePosition))
                .setLinearHeadingInterpolation(shootPosition.getHeading(), homePosition.getHeading())
                .build();
    }
}
