package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.robot.Movement;
import org.firstinspires.ftc.teamcode.robot.Brakes;
import org.firstinspires.ftc.teamcode.opmodes.positions.AutoPaths;
import org.firstinspires.ftc.teamcode.opmodes.positions.PosesAuto;
import org.firstinspires.ftc.teamcode.opmodes.TeamColor;

public abstract class AutoBase extends OpMode {

    protected TeamColor color;
    protected Movement movement;

    protected PosesAuto poses;
    protected AutoPaths paths;
    protected Pose startingPose;
    protected Brakes brakes;

    protected final long grabTime = 50;

    @Override
    public void init() {
        setColor();
        poses = new PosesAuto(color);
        setStartingPose();

        movement = new Movement(hardwareMap, telemetry, startingPose);
        paths = new AutoPaths(movement.getFollower(), poses);

        brakes = new Brakes(hardwareMap);
    }

    @Override
    public void start() {

    }

    @Override
    public void loop() {
        movement.update();
        telemetry.update();
    }

    protected abstract void setColor();
    protected abstract void setStartingPose();
}

