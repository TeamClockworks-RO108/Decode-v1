package org.firstinspires.ftc.teamcode.opmodes;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathBuilder;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Shooter.Shooter;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.util.StateMachine;

@Autonomous(name = "auto BLUE")
public class Auto extends OpMode {
    private Shooter shooter;
    private Follower follower;

    private PathChain goToShooting;

    public Pose startPositon = new Pose(0,0, Math.toRadians(45)),
                shootPosition= new Pose( 27, 27, Math.toRadians(40));

    private StateMachine<State> fsm = new StateMachine<>(State.START_POSITION);

    enum State {
        START_POSITION,
        SHOOT_POSITION,
        SHOOT_1, SHOOT_2, SHOOT_3
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
            if(!follower.isBusy()){
                return State.SHOOT_POSITION;
            }
            return null;
        });

        fsm.onStateEnter(State.SHOOT_POSITION, () -> {
            shooter.command(Shooter.Command.TOGGLE_SHOOTING);
        });
        fsm.onStateUpdate(State.SHOOT_POSITION, (current, timeSinceTransition) -> {
            if(timeSinceTransition > 1000){
                return State.SHOOT_1;
            }
            return null;
        });

        fsm.onStateEnter(State.SHOOT_1, () -> {
            shooter.command(Shooter.Command.LAUNCH);
        });
        fsm.onStateUpdate(State.SHOOT_1, (current, timeSinceTransition) -> {
            if (timeSinceTransition > 1200) {
                return State.SHOOT_2;
            }
            return null;
        });

        fsm.onStateEnter(State.SHOOT_2, () -> {
            shooter.command(Shooter.Command.LAUNCH);
        });
        fsm.onStateUpdate(State.SHOOT_2, (current, timeSinceTransition) -> {
            if (timeSinceTransition > 1200) {
                return State.SHOOT_3;
            }
            return null;
        });

        fsm.onStateEnter(State.SHOOT_3, () -> {
            shooter.command(Shooter.Command.LAUNCH);
        });
//        fsm.onStateUpdate(State.SHOOT_3, (current, timeSinceTransition) -> {
//            if (timeSinceTransition > 1200) {
//                return State.SHOOT_2;
//            }
//            return null;
//        });

        fsm.init();
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
