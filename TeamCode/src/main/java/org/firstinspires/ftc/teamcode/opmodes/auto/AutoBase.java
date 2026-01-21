package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.movement.PedroMovement;
import org.firstinspires.ftc.teamcode.robot.Brakes;
import org.firstinspires.ftc.teamcode.robot.Shooter;
import org.firstinspires.ftc.teamcode.opmodes.positions.AutoPaths;
import org.firstinspires.ftc.teamcode.opmodes.positions.PosesAuto;
import org.firstinspires.ftc.teamcode.opmodes.TeamColor;

public abstract class AutoBase extends OpMode {

    protected TeamColor color;

    private Brakes brakes;
    protected PedroMovement movement;
    protected Shooter shooter;


    protected PosesAuto poses;
    protected AutoPaths paths;
    protected Pose startingPose;

    protected final long grabTime = 300;

    @Override
    public void init() {
        setColor();
        poses = new PosesAuto(color);
        setStartingPose();

        movement = new PedroMovement(hardwareMap, telemetry, startingPose);
        paths = new AutoPaths(movement.getFollower(), poses);

        shooter = new Shooter(hardwareMap, telemetry, true);
        brakes = new Brakes(hardwareMap);

        shooter.setupShooter();

        setupFSM();
    }

    @Override
    public void start() {
        shooter.command(Shooter.Command.TOGGLE_IDLE);
        brakes.off();

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

