package org.firstinspires.ftc.teamcode.opModes;

import android.util.Log;
import android.view.View;

import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.PathBuilder;
import com.pedropathing.pathgen.Point;
import com.pedropathing.util.Constants;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.pedroPathing.constants.FConstants;
import org.firstinspires.ftc.pedroPathing.constants.LConstants;
import org.firstinspires.ftc.teamcode.shooter.Shooter;
import org.firstinspires.ftc.teamcode.util.StateMachine;

@Autonomous(name = "Sample Auto")

public class SampleAuto extends OpMode {


    private Gamepad idleGamepad;

    private Shooter shooter;

    private Follower follower;

    private Pose initPose = new Pose(0, 0, Math.toRadians(0));

    private Pose goalPose = new Pose(-17, 0, Math.toRadians(0));

    private Pose humanPose = new Pose(-170, 0, Math.toRadians(0));

    private Pose humanPose2 = new Pose(-200, -10, Math.toRadians(45));

    private enum State {
        GO_GOAL_POSITION,
        LAUNCH_1,
        LAUNCH_2,
        LAUNCH_3,
        LAUNCH_4,
        GO_TO_HUMAN,
        GO_TO_HUMAN_2,
        WAIT_FOR_HUMAN,
        GO_FROM_HUMAN_TO_GOAL

    }

    public final double PRELOAD_TO_GOAL = 2500;
    public double TIME_BETWEEN_LAUNCHES = 1200;

    public double TIME_TO_GET_TO_HUMAN = 3000;
    private StateMachine<State> fsm = new StateMachine<>(State.GO_GOAL_POSITION);

    private PathBuilder preloadToGoal, goToHuman, goToHuman2;

    @Override
    public void init() {
        Constants.setConstants(FConstants.class, LConstants.class);
        follower = new Follower(hardwareMap);
        follower.setStartingPose(initPose);
        componentInit();
        setupPaths();
        fsmInit();
    }

    @Override
    public void loop() {
        follower.update();
        fsmUpdate();
        componentUpdate();
    }


    public void componentInit() {
        shooter = new Shooter(hardwareMap, true);
        shooter.setupStateMachine();
    }

    public void fsmInit() {

        fsm.onStateEnter(State.GO_GOAL_POSITION, () -> {
            follower.followPath(preloadToGoal.build());
            shooter.command(Shooter.Command.START_ROTATION);
        });

        fsm.onStateUpdate(State.GO_GOAL_POSITION, (current, timeSinceTransition) -> {
            if (timeSinceTransition > 2000) {
                return State.LAUNCH_1;
            }
            return null;
        });

        fsm.onStateEnter(State.LAUNCH_1, () -> {
            shooter.command(Shooter.Command.LAUNCH);
        });

        fsm.onStateUpdate(State.LAUNCH_1, (current, timeSinceTransition) -> {
            if (timeSinceTransition > TIME_BETWEEN_LAUNCHES) {
                return State.LAUNCH_2;
            }
            return null;
        });

        fsm.onStateEnter(State.LAUNCH_2, () -> {
            shooter.command(Shooter.Command.LAUNCH);
        });

        fsm.onStateUpdate(State.LAUNCH_2, (current, timeSinceTransition) -> {
            if (timeSinceTransition > TIME_BETWEEN_LAUNCHES) {
                return State.LAUNCH_3;
            }
            return null;
        });

        fsm.onStateEnter(State.LAUNCH_3, () -> {
            shooter.command(Shooter.Command.LAUNCH);
        });

        fsm.onStateUpdate(State.LAUNCH_3, (current, timeSinceTransition) -> {
            if (timeSinceTransition > TIME_BETWEEN_LAUNCHES) {
                return State.LAUNCH_4;
            }
            return null;
        });

        fsm.onStateEnter(State.LAUNCH_4, () -> {
            shooter.command(Shooter.Command.LAUNCH);
        });

        fsm.onStateUpdate(State.LAUNCH_4, (current, timeSinceTransition) -> {
            if (timeSinceTransition > TIME_BETWEEN_LAUNCHES) {
                return State.GO_TO_HUMAN;
            }
            return null;
        });

        fsm.onStateEnter(State.GO_TO_HUMAN, () -> {
            follower.followPath(goToHuman.build());
            shooter.command(Shooter.Command.START_ROTATION);
        });

        fsm.onStateUpdate(State.GO_TO_HUMAN, (current, timeSinceTransition) -> {
            if (!follower.isBusy()) {
                return State.GO_TO_HUMAN_2;
            }
            return null;
        });

        fsm.onStateEnter(State.GO_TO_HUMAN_2, () -> {
            follower.followPath(goToHuman2.build());
        });

        fsm.onStateUpdate(State.GO_TO_HUMAN_2,  (current, timeSinceTransition) -> {
            if(!follower.isBusy()){
                return State.WAIT_FOR_HUMAN;
            }
            return null;


        });

        fsm.init();

    }

    public void fsmUpdate() {
        fsm.update();
        Log.d("auto: ", "" + fsm.getCurrentState());
    }

    public void componentUpdate() {
        shooter.updateStateMachine();

    }

    public void setupPaths() {
        preloadToGoal = follower.pathBuilder()
                .addBezierLine(new Point(initPose), new Point(goalPose))
                .setLinearHeadingInterpolation(initPose.getHeading(), goalPose.getHeading());

        goToHuman = follower.pathBuilder()
                .addBezierLine(new Point(goalPose), new Point(humanPose))
                .setLinearHeadingInterpolation(goalPose.getHeading(), goalPose.getHeading());
        goToHuman2 = follower.pathBuilder()
                .addBezierLine(new Point(humanPose), new Point(humanPose2))
                .setLinearHeadingInterpolation(humanPose.getHeading(), humanPose2.getHeading());
    }
}


