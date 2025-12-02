package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Shooter.Shooter;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.util.StateMachine;

@Autonomous(name = "Parking BLUE")
public class ParkingBlue extends OpMode {
    private Shooter shooter;
    private Follower follower;

    private PathChain goParking;

    protected Pose startPosition = new Pose(0,0, Math.toRadians(0)),
                parkingPosition = new Pose(24, 0, Math.toRadians(0));

    private StateMachine<State> fsm = new StateMachine<>(State.INIT);

    enum State {
        INIT,
        GO_PARKING
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
        fsm.onStateUpdate(State.INIT, () -> State.GO_PARKING);
    }

    @Override
    public void loop() {
        updateFSM();
        follower.update();
        shooter.updateShooter();
    }

    public void setupFSM(){
        // go to parking position
        fsm.onStateEnter(State.GO_PARKING, () -> {
            follower.followPath(goParking);
        });
        fsm.onStateUpdate(State.GO_PARKING, () -> {
            if (!follower.isBusy()) {
                shooter.command(Shooter.Command.TOGGLE_DEAD);
            }
        });

        fsm.init();
    }

    public void updateFSM() {
        fsm.update();
    }

    public void setupPaths(){
        goParking = follower.pathBuilder()
                .addPath(new BezierCurve(startPosition, parkingPosition))
                .setLinearHeadingInterpolation(startPosition.getHeading(), parkingPosition.getHeading())
                .build();
    }
}
