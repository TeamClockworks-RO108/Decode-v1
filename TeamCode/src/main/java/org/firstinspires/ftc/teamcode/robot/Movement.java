package org.firstinspires.ftc.teamcode.robot;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierPoint;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.command.Subsystem;
import org.firstinspires.ftc.teamcode.opmodes.TeamColor;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

public class Movement implements Subsystem {
    private static final double FINE_POWER = 0.25;

    private final Follower follower;
    private final Telemetry telemetry;

    private boolean areControlsFlipped = false;

    public Movement(HardwareMap hardwareMap, Telemetry telemetry, Pose startingPose) {
        this.telemetry = telemetry;

        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(startingPose);
        follower.update();
    }

    public void update() {
        follower.update();

        // telemetry
        telemetry.addData("x", follower.getPose().getX());
        telemetry.addData("y", follower.getPose().getY());
        telemetry.addData("heading", Math.toDegrees(follower.getPose().getHeading()));
    }
    // TeleOp specific update
    public void update(Gamepad gamepad1, Gamepad gamepad2) {
        update();

        double y = -gamepad1.left_stick_y - gamepad2.left_stick_y * FINE_POWER;
        double x = -gamepad1.left_stick_x - gamepad2.left_stick_x * FINE_POWER;
        double heading = -gamepad1.right_stick_x - gamepad2.right_stick_x * FINE_POWER;
        setTeleop(y, x, heading);
    }

    public void resetHeading(double heading) {
        follower.setPose(follower.getPose().setHeading(heading));
    }

    public void goToPose(Pose newPose) {
        follower.followPath(follower.pathBuilder()
                .addPath(new BezierCurve(follower.getPose(), newPose))
                .setLinearHeadingInterpolation(follower.getHeading(), newPose.getHeading())
                .build());
    }

    public void flipControls() {
        areControlsFlipped = true;
    }

    public void breakFollowing() {
        follower.breakFollowing();
        follower.startTeleopDrive();
    }

    public Follower getFollower() {
        return follower;
    }

    private void setTeleop(double y, double x, double heading) {
        if (!areControlsFlipped)
            follower.setTeleOpDrive(-y, -x, heading, false);
        else
            follower.setTeleOpDrive(y, x, heading, false);
    }
}
