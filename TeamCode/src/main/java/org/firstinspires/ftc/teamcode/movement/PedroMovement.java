package org.firstinspires.ftc.teamcode.movement;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

public class PedroMovement {
    private final Follower follower;
    private final Vision vision;
    private final Telemetry telemetry;
    private boolean isRobotCentric = false;
    private boolean isFineTuning = false;

    public PedroMovement(HardwareMap hardwareMap, Telemetry telemetry, Pose startingPose) {
        vision = new Vision(hardwareMap, telemetry);
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(startingPose);
        follower.update();

        this.telemetry = telemetry;
    }

    // teleop functions
    public void toggleFineTuning() {
        isFineTuning = !isFineTuning;
    }
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
        update();

        double finePower = 0.3;

        double y = -gamepad1.left_stick_y - gamepad2.left_stick_y * finePower;
        double x = -gamepad1.left_stick_x - gamepad2.left_stick_x * finePower;
        double heading = -gamepad1.right_stick_x - gamepad2.right_stick_x * finePower;

        if (isFineTuning)
            setTeleop(y * 0.25, x * 0.25, heading * 0.25);
        else
            setTeleop(y, x, heading);
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

    public void update() {
        follower.update();
        vision.update();
        // telemetry
        telemetry.addData("x", follower.getPose().getX());
        telemetry.addData("y", follower.getPose().getY());
        telemetry.addData("heading", follower.getPose().getHeading() * 180 / 3.14159);
        telemetry.update();
    }
}
