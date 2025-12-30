package org.firstinspires.ftc.teamcode.movement;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.util.StateMachine;

public class PedroMovement {
    private final Follower follower;
    private final Telemetry telemetry;

    private Vision vision;
    private boolean isRobotCentric = false;

    private enum TeleopStates {
        TELEOP,
        AIMING
    }

    public enum Command {
        START_AIMING
    }

    private Command unexecutedCommand = null;

    private final StateMachine<TeleopStates> fsm  = new StateMachine<>(TeleopStates.TELEOP);

    public PedroMovement(HardwareMap hardwareMap, Telemetry telemetry, Pose startingPose) {
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(startingPose);
        follower.update();
        setupTeleopFSM();

        vision = new Vision(hardwareMap, telemetry);

        this.telemetry = telemetry;
    }

    public void command(Command command) {
        unexecutedCommand = command;
    }

    // teleop functions
    public void resetPose() {
        follower.setPose(new Pose(0, 0, 0));
    }

    public void startTeleop() {
        follower.startTeleOpDrive();
    }

    private void setTeleop(double y, double x, double heading) {
        follower.setTeleOpDrive(y, x, heading, isRobotCentric);
    }

    public void updateTeleOp(Gamepad gamepad1, Gamepad gamepad2) {
        telemetry.addData("x angle", Math.toRadians(vision.getAngleX()));
        update();

        double finePower = 0.3;

        double y = -gamepad1.left_stick_y - gamepad2.left_stick_y * finePower;
        double x = -gamepad1.left_stick_x - gamepad2.left_stick_x * finePower;
        double heading = -gamepad1.right_stick_x - gamepad2.right_stick_x * finePower;
        setTeleop(y, x, heading);

        fsm.update();
        vision.update();
    }

    public Follower getFollower() {
        return follower;
    }
    public boolean isBusy() {
        return follower.isBusy();
    }

    public void followPath(PathChain path) {
        follower.followPath(path);
    }

    public void rotateToGoal() {
        Pose startPose = follower.getPose();
        Pose endPose = new Pose(startPose.getX() + 1, startPose.getY() + 1, startPose.getHeading() - Math.toRadians(vision.getAngleX()));

        follower.followPath(follower.pathBuilder()
                .addPath(new BezierLine(startPose, endPose))
                .setLinearHeadingInterpolation(startPose.getHeading(), endPose.getHeading())
                .build());
    }

    private void setupTeleopFSM(){
        fsm.onStateEnter(TeleopStates.TELEOP, () -> {
            startTeleop();
        });
        fsm.onStateUpdate(TeleopStates.TELEOP, () -> {
            if (unexecutedCommand == Command.START_AIMING){
                unexecutedCommand = null;
                return TeleopStates.AIMING;
            }
            return null;
        });

        fsm.onStateEnter(TeleopStates.AIMING, () -> {
//            follower.pausePathFollowing();
            rotateToGoal();
        });
        fsm.onStateUpdate(TeleopStates.AIMING, () -> {
            if (!follower.isBusy())
                return TeleopStates.TELEOP;
            return null;
        });
        fsm.onStateExit(TeleopStates.AIMING, () -> {
//            follower.resumePathFollowing();
//            follower.breakFollowing();
        });

        fsm.init();
    }

    public void update() {
        follower.update();
        // telemetry
        telemetry.addData("x", follower.getPose().getX());
        telemetry.addData("y", follower.getPose().getY());
        telemetry.addData("heading", Math.toDegrees(follower.getPose().getHeading()));
    }
}
