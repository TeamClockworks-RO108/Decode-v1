package org.firstinspires.ftc.teamcode.opmodes;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
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

    private PathChain goToShooting;

    public Pose startPositon = new Pose(10,10, Math.toRadians(45)),
                shootPosition= new Pose( 41, 41, Math.toRadians(40)),
                firstBatchPosition = new Pose(50, 40, Math.toRadians(90));

    private StateMachine<State> fsm = new StateMachine<>(State.INIT);

    enum State {
        INIT,
        START_POSITION,
        SHOOT_POSITION,
        SHOOT_A1, SHOOT_A2, SHOOT_A3, // preload
        GO_TO_FIRST_SET
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
            follower.followPath(goToShooting);
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
        setShootArtifact(State.SHOOT_A3, State.GO_TO_FIRST_SET);



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
         goToShooting = follower.pathBuilder()
                .addPath(new BezierCurve(startPositon, shootPosition))
                .setLinearHeadingInterpolation(startPositon.getHeading(), shootPosition.getHeading())
                .build();
    }
}
