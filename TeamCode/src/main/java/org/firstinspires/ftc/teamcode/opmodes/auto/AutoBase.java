package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.robot.Shooter;
import org.firstinspires.ftc.teamcode.opmodes.AutoPaths;
import org.firstinspires.ftc.teamcode.opmodes.AutoPoses;
import org.firstinspires.ftc.teamcode.opmodes.TeamColor;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

public abstract class AutoBase extends OpMode {
    protected TeamColor color;
    protected Shooter shooter;
    protected Follower follower;

    protected AutoPoses poses;
    protected AutoPaths paths;

    @Override
    public void init() {
        setColor();
        shooter = new Shooter(hardwareMap, telemetry, true);
        shooter.setupShooter();

        follower = Constants.createFollower(hardwareMap);

        poses = new AutoPoses(color);
        setStartingPose();

        paths = new AutoPaths(follower, poses);
        follower.update();

        setupFSM();
    }

    @Override
    public void start() {
        shooter.command(Shooter.Command.TOGGLE_IDLE);
        startFSM();
    }

    @Override
    public void loop() {
        updateFSM();
        follower.update();
        shooter.updateShooter();

        // telemetry
        telemetry.addData("x", follower.getPose().getX());
        telemetry.addData("y", follower.getPose().getY());
        telemetry.addData("heading", follower.getPose().getHeading());
        telemetry.update();
    }

    protected abstract void setColor();
    protected abstract void setStartingPose();
    protected abstract void setupFSM();
    protected abstract void startFSM();
    protected abstract void updateFSM();
}

