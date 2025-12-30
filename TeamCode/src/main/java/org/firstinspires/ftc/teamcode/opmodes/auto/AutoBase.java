package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeRegistrar;

import org.firstinspires.ftc.teamcode.movement.PedroMovement;
import org.firstinspires.ftc.teamcode.robot.Shooter;
import org.firstinspires.ftc.teamcode.opmodes.AutoPaths;
import org.firstinspires.ftc.teamcode.opmodes.AutoPoses;
import org.firstinspires.ftc.teamcode.opmodes.TeamColor;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.util.StateMachine;

public abstract class AutoBase extends OpMode {

    protected TeamColor color;
    protected PedroMovement movement;
    protected Shooter shooter;

    protected AutoPoses poses;
    protected AutoPaths paths;
    protected Pose startingPose;

    protected final long grabTime = 250;

    @Override
    public void init() {
        setColor();
        poses = new AutoPoses(color);
        setStartingPose();

        movement = new PedroMovement(hardwareMap, telemetry, startingPose);
        paths = new AutoPaths(movement.getFollower(), poses);

        shooter = new Shooter(hardwareMap, telemetry, true);
        shooter.setupShooter();

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
        movement.update();
        shooter.updateShooter();
        telemetry.update();
    }

    protected abstract void setColor();
    protected abstract void setStartingPose();
    protected abstract void setupFSM();
    protected abstract void startFSM();
    protected abstract void updateFSM();
}

