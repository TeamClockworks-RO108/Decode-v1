package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Robot.Shooter;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.util.StateMachine;

@Autonomous(name = "Auto BLUE")
public class AutoBlue extends OpMode {
    private Shooter shooter;
    private Follower follower;

    private PathChain goShoot,
            goFirstIntake, goShootFirstIntake,
            goSecondIntake, goShootSecondIntake,
            goHome;

    protected final double firstIntakeX = 49.6;
    protected final double secondIntakeX = 73.6;
    protected final double launchAngle = 45;

    protected Pose startPosition = new Pose(10,10, Math.toRadians(45)),
                shootPosition = new Pose( 43.4, 43.4, Math.toRadians(launchAngle)),
                firstIntakePosition = new Pose(firstIntakeX, 42, Math.toRadians(-90)),
                firstIntakeTakePosition = new Pose(firstIntakeX, -3, Math.toRadians(-90)),
                secondIntakePosition = new Pose(secondIntakeX, 42, Math.toRadians(-90)),
                secondIntakeTakePosition = new Pose(secondIntakeX, -6, Math.toRadians(-90)),
                homePosition = new Pose(45, 15, Math.toRadians(-90));

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
        follower.setStartingPose(startPosition);
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
        // handle preload
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

        // handle first intake
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
        setShootArtifact(State.SHOOT_B3, State.SECOND_INTAKE);

        // handle second intake
        fsm.onStateEnter(State.SECOND_INTAKE, () -> {
            follower.followPath(goSecondIntake);
            shooter.command(Shooter.Command.TOGGLE_INTAKE);
        });
        fsm.onStateUpdate(State.SECOND_INTAKE, () -> {
            if (!follower.isBusy())
                return State.SHOOT_SECOND_INTAKE;
            return null;
        });

        fsm.onStateEnter(State.SHOOT_SECOND_INTAKE, () -> {
            follower.followPath(goShootSecondIntake);
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
            follower.followPath(goHome);
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

    public void setupPaths(){
         goShoot = follower.pathBuilder()
                .addPath(new BezierCurve(startPosition, shootPosition))
                .setLinearHeadingInterpolation(startPosition.getHeading(), shootPosition.getHeading())
                .build();

         goFirstIntake = follower.pathBuilder()
                 .addPath(new BezierCurve(shootPosition, firstIntakePosition))
                 .setLinearHeadingInterpolation(shootPosition.getHeading(), firstIntakePosition.getHeading())
                 .addPath(new BezierLine(firstIntakePosition, firstIntakeTakePosition))
                 .build();

        goShootFirstIntake = follower.pathBuilder()
                .addPath(new BezierCurve(firstIntakeTakePosition, shootPosition))
                .setLinearHeadingInterpolation(firstIntakeTakePosition.getHeading(), shootPosition.getHeading())
                .build();

        goSecondIntake = follower.pathBuilder()
                .addPath(new BezierCurve(shootPosition, secondIntakePosition))
                .setLinearHeadingInterpolation(shootPosition.getHeading(), secondIntakePosition.getHeading())
                .addPath(new BezierLine(secondIntakePosition, secondIntakeTakePosition))
                .build();

        goShootSecondIntake = follower.pathBuilder()
                .addPath(new BezierCurve(secondIntakeTakePosition, shootPosition))
                .setLinearHeadingInterpolation(secondIntakeTakePosition.getHeading(), shootPosition.getHeading())
                .build();

        goHome = follower.pathBuilder()
                .addPath(new BezierCurve(shootPosition, homePosition))
                .setLinearHeadingInterpolation(shootPosition.getHeading(), homePosition.getHeading())
                .build();
    }
}
