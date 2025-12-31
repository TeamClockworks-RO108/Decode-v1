package org.firstinspires.ftc.teamcode.movement;

import com.pedropathing.follower.Follower;
import com.pedropathing.ftc.PoseConverter;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.BezierPoint;
import com.pedropathing.geometry.CoordinateSystem;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.util.StateMachine;

public class PedroMovement {
    private final Follower follower;
    private final Telemetry telemetry;

    private Pose endPose;

    private Vision vision;
    private boolean isRobotCentric = false;

    private enum TeleopStates {
        TELEOP,
        AIMING,
        HOLD_AIM,
    }

    public enum Command {
        START_AIMING,

        RELEASE_AIM
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
    public void setHomePose() {
        follower.setPose(new Pose(9, 144-12, Math.toRadians(180)));
    }

    public void startTeleop() {
        follower.startTeleOpDrive();
    }

    private void setTeleop(double y, double x, double heading) {
        follower.setTeleOpDrive(y, x, heading, isRobotCentric);
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
        endPose = new Pose(144-47, 47, Math.toRadians(135));

        follower.followPath(follower.pathBuilder()
                .addPath(new BezierCurve(follower.getPose(), endPose))
                .setLinearHeadingInterpolation(follower.getHeading(), endPose.getHeading())
                .build());
    }

//    public Pose processVisionPose() {
//        if (vision.getLastResult() == null) return null;
//
//        Pose3D pose = vision.getLastResult().getBotpose();
//        Pose pose2 = new Pose(
//                pose.getPosition().x * 39.37008 + 144.0/2,
//                -pose.getPosition().y * 39.37008 + 144.0/2,
//                pose.getOrientation().getYaw(AngleUnit.RADIANS) + Math.PI * 3/2
//        );
//
//        telemetry.addData("Pedro Pose", pose2);
//
//        return pose2;
//    }

    public void update() {
        follower.update();
        // telemetry
        telemetry.addData("x, y", follower.getPose().getX() + " " + follower.getPose().getY());
        telemetry.addData("heading", Math.toDegrees(follower.getPose().getHeading()));
    }
    public void updateTeleOp(Gamepad gamepad1, Gamepad gamepad2) {
        vision.update();
        telemetry.addData("x angle", vision.getAngleX());
        vision.processVisionPose();

        update();

        final double finePower = 0.3;
        double y = -gamepad1.left_stick_y - gamepad2.left_stick_y * finePower;
        double x = -gamepad1.left_stick_x - gamepad2.left_stick_x * finePower;
        double heading = -gamepad1.right_stick_x - gamepad2.right_stick_x * finePower;
        setTeleop(y, x, heading);

        fsm.update();
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
            rotateToGoal();
        });
        fsm.onStateUpdate(TeleopStates.AIMING, () -> {
            if (!follower.isBusy())
                return TeleopStates.HOLD_AIM;
            return null;
        });

        fsm.onStateEnter(TeleopStates.HOLD_AIM, () -> {
            follower.holdPoint( new BezierPoint(endPose.getX(), endPose.getY()), endPose.getHeading(), true );
        });

        fsm.onStateUpdate(TeleopStates.HOLD_AIM, () -> {
            if (unexecutedCommand == Command.RELEASE_AIM ){
                unexecutedCommand = null;
                return TeleopStates.TELEOP;

            }
            return null;
        });


        fsm.init();
    }
}
